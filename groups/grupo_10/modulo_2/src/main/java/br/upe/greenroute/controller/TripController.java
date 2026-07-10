package br.upe.greenroute.controller;

import br.upe.greenroute.exceptions.AIServiceException;
import br.upe.greenroute.exceptions.GreenRouteException;
import br.upe.greenroute.model.*;
import br.upe.greenroute.repository.ChargingStationRepository;
import br.upe.greenroute.repository.CityRepository;
import br.upe.greenroute.repository.VehicleRepository;
import br.upe.greenroute.view.ViewManager;
import iaService.AIPlannerService;
import java.util.List;

public class TripController {
    private final AIPlannerService aiService;
    private final VehicleRepository vehicleRepository;
    private final CityRepository cityRepository;
    private final ChargingStationRepository chargingStationRepository;
    private final ViewManager view;

    public TripController(VehicleRepository vehicleRepository, CityRepository cityRepository, ChargingStationRepository chargingStationRepository, AIPlannerService aiService, ViewManager view) {
        this.view = view;
        this.chargingStationRepository = chargingStationRepository;
        this.cityRepository = cityRepository;
        this.vehicleRepository = vehicleRepository;
        this.aiService = aiService;
    }

    public void executeTripSimulation() {
        if (vehicleRepository.isEmpty() || cityRepository.isEmpty()) {
            view.displayError("É necessário ter pelo menos um veículo e uma cidade cadastrados para simular uma viagem!");
            return;
        }
        int vehicleId = view.getSelectedVehicleId();
        int cityId = view.getSelectedCityId();
        if (vehicleId == -1 || cityId == -1) {
            view.displayError("Por favor, selecione um veículo e uma cidade nas abas.");
            return;
        }
        VehicleModel vehicle = vehicleRepository.searchById(vehicleId);
        CityModel destination = cityRepository.searchById(cityId);
        try {
            String type;
            if (vehicle instanceof ElectricVehicleModel) {
                type = "Elétrico";
            } else {
                type = "Híbrido";
            }
            StringBuilder workVehicleData = new StringBuilder();
            workVehicleData.append(String.format("Vehicle %s: %s, Current Autonomy: %.2f Km, current batery: %.2f, CkWh per Km Consume: %.2f, total batery Recharge Time %d", type, vehicle.getModel(), vehicle.calculateCurrentAutonomy(), vehicle.getCurrentBatteryCharge(), vehicle.getConsumeKwhPerKm(), vehicle.getFullRechargeTime()));
            if (type.equals("Elétrico")) {
                workVehicleData.append(String.format(", Recharge Time in High-Power Connectors: %d, Connector Type: %s", ((ElectricVehicleModel) vehicle).getFastCharging(), ((ElectricVehicleModel) vehicle).getConnectorType()));
            }
            String vehicleData = workVehicleData.toString();
            String destinationData = String.format("Destination: %s - %S, Distance from Capital: %.2f", destination.getName(), destination.getState(), destination.getCapitalDistance());
            String validStationsToStop = availableStations(vehicle,destination);
            String tripReport = aiService.planSmartRoute(vehicleData, destinationData, validStationsToStop);
            view.showTripReport(tripReport);
        } catch (AIServiceException e) {
            view.displayError("Erro ao processar dados com IA!"+e.getMessage());
        } catch (GreenRouteException e) {
            view.displayError(e.getMessage());
        } catch (Exception e) {
            view.displayError("Erro inesperado: "+e.getCause());
        }
    }

    private String availableStations(VehicleModel vehicle,CityModel destination) {
        if (vehicle instanceof HybridVehicleModel) {
            return "The selected vehicle is a hybrid. It does not plug into charging stations. Do not recommend charging stations for this simulation.";
        }
        String vehicleConnector = ((ElectricVehicleModel)vehicle).getConnectorType();
        List<ChargingStationModel> allStations = chargingStationRepository.getStations();
        StringBuilder validStations = new StringBuilder("Available charging stations: ");
        boolean foundAny = false;
        if (allStations.isEmpty()) {
            validStations.append("no registered charging stations");
            return validStations.toString();
        }
        for (ChargingStationModel station : allStations) {
            CityModel cityOfStation = cityRepository.searchById(station.getCityId());
            boolean compatibleConnector = station.getAvailableConnectorsType().contains(vehicleConnector);
            if (cityOfStation != null && compatibleConnector && station.getAvailableVacancies() > 0 && destination.getState().equalsIgnoreCase(cityOfStation.getState())) {
                validStations.append(String.format("- station '%s' located in %s - %s (Distance from Capital: %.2f). Connectors: %s\n", station.getName(), cityOfStation.getName(), cityOfStation.getState(), cityOfStation.getCapitalDistance(), String.join(", ", station.getAvailableConnectorsType())));
                foundAny = true;
            }
        }
        if (!foundAny) {
            validStations.append("none found with available vacancies in the destination state.");
        }
        return validStations.toString();
    }
}