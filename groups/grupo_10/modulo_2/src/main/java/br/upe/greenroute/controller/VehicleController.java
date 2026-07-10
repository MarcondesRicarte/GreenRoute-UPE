package br.upe.greenroute.controller;

import br.upe.greenroute.exceptions.AIServiceException;
import br.upe.greenroute.exceptions.EntityNotFoundException;
import br.upe.greenroute.exceptions.GreenRouteException;
import br.upe.greenroute.model.ElectricVehicleModel;
import br.upe.greenroute.model.HybridVehicleModel;
import br.upe.greenroute.model.VehicleModel;
import br.upe.greenroute.repository.VehicleRepository;
import br.upe.greenroute.view.ViewManager;
import iaService.AIPlannerService;

import java.util.List;

public class VehicleController extends BaseController{
    private final VehicleRepository repository;
    private final ViewManager view;
    private final AIPlannerService aiService;
    public VehicleController(VehicleRepository repository, ViewManager view, AIPlannerService aiService) {
        this.repository = repository;
        this.view = view;
        this.aiService = aiService;
    }
    public void fastAddVehicle() {
        String userText = view.showAiFormDialog();
        if (userText != null && !userText.trim().isEmpty()) {
            try {
                String[] aiData = aiService.parseVehicle(userText);
                if (aiData != null && aiData.length != 0) {
                    addVehicle(aiData);
                } else {
                    view.displayError("A IA não conseguiu compreender o texto informado!");
                }
            }  catch (AIServiceException e) {
                view.displayError("Erro ao processar dados com IA!"+e.getMessage());
            } catch (GreenRouteException e) {
                view.displayError(e.getMessage());
            } catch (Exception e) {
                view.displayError("Erro inesperado: "+e.getMessage());
            }
        }
    }
    public void addVehicle(String[] aiData) {
        String aiType = null;
        if (aiData != null) {
            aiType = aiData[10];
        }
        String[] datas = view.showVehicleFormDialog(aiType, aiData);
        if (datas == null) {
            return;
        }
        String type = datas[0];
        String model = datas[1];
        String maximumAutonomyStr = datas[2];
        String currentBatteryChargeStr = datas[3];
        String consumeKwhPerKmStr = datas[4];
        String fullRechargeTimeStr = datas[5];
        try {
            isAnyBlank(model, maximumAutonomyStr, currentBatteryChargeStr, consumeKwhPerKmStr, fullRechargeTimeStr);
            model = trimText(model);
            double maximumAutonomy = parseCleanDouble(maximumAutonomyStr,"A autonomia máxima deve ser um valor valido!");
            double currentBatteryCharge = parseCleanDouble(currentBatteryChargeStr, "A carga atual da bateria deve ser um valor valido!");
            double consumeKwhPerKm = parseCleanDouble(consumeKwhPerKmStr, "O consumo de energia deve ser um valor valido!");
            int fullRechargeTime = parseCleanInt(fullRechargeTimeStr, "O tempo de recarga completa deve ser um valor valido!");
            switch (type) {
                case "Elétrico" -> addElectricVehicle(datas, model, maximumAutonomy, currentBatteryCharge, consumeKwhPerKm, fullRechargeTime);
                case "Híbrido" -> addHybridVehicle(datas, model, maximumAutonomy, currentBatteryCharge, consumeKwhPerKm, fullRechargeTime);
            }
        } catch (GreenRouteException e) {
            view.displayError(e.getMessage());
        }
    }
    private void addElectricVehicle(String[] data, String model, double maximumAutonomy, double currentBatteryCharge, double consumeKwhPerKm, int fullRechargeTime) {
        String connectorType = data[6].toLowerCase();
        String fastChargingStr = data[7];
        try {
            isAnyBlank(connectorType, fastChargingStr);
            connectorType = trimText(connectorType);
            int fastCharging = parseCleanInt(fastChargingStr, "O tempo de recarga rápida deve se um valor inteiro!");
            VehicleModel vehicle = new ElectricVehicleModel(model, maximumAutonomy, currentBatteryCharge, consumeKwhPerKm, fullRechargeTime, connectorType, fastCharging);
            repository.add(vehicle);
            view.displaySuccess("Veiculo cadastrado no sistema!");
            view.refreshVehicleList(listVehicles());
        } catch (GreenRouteException e) {
            view.displayError(e.getMessage());
        }
    }
    private void addHybridVehicle (String[] data, String model, double maximumAutonomy, double currentBatteryCharge, double consumeKwhPerKm, int fullRechargeTime) {
        String fuelTankCapacityStr = data[8];
        String fuelConsumptionStr = data[9];
        String fuelType = data[10].toLowerCase();
        try {
            isAnyBlank(fuelTankCapacityStr, fuelConsumptionStr, fuelType);
            fuelType = trimText(fuelType);
            double fuelTankCapacity = parseCleanDouble(fuelTankCapacityStr, "A capacidade do tanque de combustivel deve ser um valor valido!");
            double fuelConsumption = parseCleanDouble(fuelConsumptionStr, "O consumo de combustivel do motor deve ser um valor valido!");
            VehicleModel vehicle = new HybridVehicleModel(model, maximumAutonomy, currentBatteryCharge, consumeKwhPerKm, fullRechargeTime, fuelTankCapacity, fuelConsumption, fuelType);
            repository.add(vehicle);
            view.displaySuccess("Veiculo cadastrado no sistema!");
            view.refreshVehicleList(listVehicles());
        } catch (GreenRouteException e) {
            view.displayError(e.getMessage());
        }
    }
    public void filterVehicles() {
        String searchType = view.getSearchtype();
        String input = view.getSearchinput();
        if (input.isEmpty() || searchType.equals("Todos")) {
            view.refreshVehicleList(listVehicles());
            return;
        }
        if (searchType.equals("ID")) {
            try{
                int id = parseCleanInt(input,"O ID informado deve ser um número inteiro válido!");
                VehicleModel vehicle = repository.searchById(id);
                    view.refreshVehicleList(List.of(vehicle));
            } catch (EntityNotFoundException e) {
                view.refreshVehicleList(List.of());
                view.displayError(e.getMessage());
            } catch (GreenRouteException e) {
                view.displayError(e.getMessage());
            }
        }
    }
    public String vehicleType(int id) {
        try {
            VehicleModel vehicleFound = repository.searchById(id);
            if (vehicleFound instanceof ElectricVehicleModel) {
                return "1";
            } else if (vehicleFound instanceof HybridVehicleModel) {
                return "2";
            }
        } catch (EntityNotFoundException e) {
            return "Veículo não encontrado!";
        }
        return "O veiculo selecionado possui uma tipagem não configurada no sistema!";
    }
    public void updateVehicle() {
        int selectedRow = view.getSelectedRow();
        if (selectedRow == -1) {
            view.displayError("Por favor, selecione um veiculo na tabela para editar!");
            return;
        }
        try {
            int id = view.getSelectedId();
            VehicleModel vehicleToEdit = repository.searchById(id);
            String[] currentData = new String[]{
                    vehicleToEdit.getModel(),
                    String.valueOf(vehicleToEdit.getMaximumAutonomy()),
                    String.valueOf(vehicleToEdit.getCurrentBatteryCharge()),
                    String.valueOf(vehicleToEdit.getConsumeKwhPerKm()),
                    String.valueOf(vehicleToEdit.getFullRechargeTime()),
                    null, null, null, null, null
            };
            String type;
            if (vehicleType(id).equals("1")) {
                type = "Elétrico";
                ElectricVehicleModel eVehicle = (ElectricVehicleModel) vehicleToEdit;
                currentData[5] = eVehicle.getConnectorType();
                currentData[6] = String.valueOf(eVehicle.getFastCharging());
            }else if (vehicleType(id).equals("2")) {
                type = "Híbrido";
                HybridVehicleModel hVehicle = (HybridVehicleModel) vehicleToEdit;
                currentData[7] = String.valueOf(hVehicle.getFuelTankCapacity());
                currentData[8] = String.valueOf(hVehicle.getFuelConsumption());
                currentData[9] = hVehicle.getFuelType();
            }else {
                view.displayError(vehicleType(id));
                return;
            }
            String[] datas = view.showVehicleFormDialog(type, currentData);
            if (datas == null) {
                return;
            }
            String model = datas[1];
            String maximumAutonomyStr = datas[2];
            String currentBatteryChargeStr = datas[3];
            String consumeKwhPerKmStr = datas[4];
            String fullRechargeTimeStr = datas[5];
            if (model != null && !model.isBlank()) {
                vehicleToEdit.setModel(model);
            }
            if (maximumAutonomyStr != null && !maximumAutonomyStr.isBlank()) {
                double maximumAutonomy = parseCleanDouble(maximumAutonomyStr,"A autonomia máxima deve ser um valor valido! (Será mantido o valor anterior)");
                if (maximumAutonomy > 0) {
                    vehicleToEdit.setMaximumAutonomy(maximumAutonomy);
                } else {
                    view.displayError("A autonomia máxima deve ser um valor positivo! (Será mantido o valor anterior)");
                }
            }
            if (currentBatteryChargeStr != null && !currentBatteryChargeStr.isBlank()) {
                double currentBatteryCharge = parseCleanDouble(currentBatteryChargeStr,"A carga atual da bateria deve ser um valor valido! (Será mantido o valor anterior)");
                if (currentBatteryCharge >= 0 && currentBatteryCharge <= 100) {
                    vehicleToEdit.setCurrentBatteryCharge(currentBatteryCharge);
                } else {
                    view.displayError("A bateria só pode ir de 0% à 100% (Será mantido o valor anterior)");
                }
            }
            if (consumeKwhPerKmStr != null && !consumeKwhPerKmStr.isBlank()) {
                double consumeKwhPerKm = parseCleanDouble(consumeKwhPerKmStr,"O consumo de energia deve ser um valor valido! (Será mantido o valor anterior)");
                if (consumeKwhPerKm > 0) {
                    vehicleToEdit.setConsumeKwhPerKm(consumeKwhPerKm);
                } else {
                    view.displayError("O consumo de kWh por Km deve ser um valor positivo! (Será mantido o valor anterior)");
                }
            }
            if (fullRechargeTimeStr != null && !fullRechargeTimeStr.isBlank()) {
                int fullrechargeTime = parseCleanInt(fullRechargeTimeStr,"O tempo de recarga completa deve ser um valor valido! (Será mantido o valor anterior)");
                if (fullrechargeTime > 0) {
                    vehicleToEdit.setFullRechargeTime(fullrechargeTime);
                } else {
                    view.displayError("O tempo de recarga da bateria deve ser um valor positivo! (Será mantido o valor anterior)");
                }
            }
            switch (type) {
                case "Elétrico" -> updateElectricVehicle(datas, vehicleToEdit);
                case "Híbrido" -> updateHybridVehicle(datas, vehicleToEdit);
            }
        } catch (EntityNotFoundException e) {
            view.displayError(e.getMessage());
        }
    }
    private void updateElectricVehicle (String[] data, VehicleModel vehicle) {
        ElectricVehicleModel electricVehicle = (ElectricVehicleModel) vehicle;
        String connectorType = data[6].toLowerCase();
        String fastChargingStr = data[7];
        try {
            if (connectorType != null && !connectorType.isBlank()) {
                connectorType = trimText(connectorType);
                electricVehicle.setConnectorType(connectorType);
            }
            if (fastChargingStr != null && !fastChargingStr.isBlank()) {
                int fastCharging = parseCleanInt(fastChargingStr,"O tempo de recarga rápida deve se um valor válido! (Será mantido o valor anterior)");
                if (fastCharging > 0 && fastCharging < electricVehicle.getFullRechargeTime()) {
                    electricVehicle.setFastCharging(fastCharging);
                } else {
                    view.displayError("O tempo de recarga rápida da bateria deve ser um valor positivo! (Será mantido o valor anterior)");
                }
            }
            repository.update(electricVehicle);
            view.displaySuccess("Veiculo atualizado com sucesso!");
            view.refreshVehicleList(listVehicles());
        } catch (EntityNotFoundException e) {
            view.displayError(e.getMessage());
        }
    }
    private void updateHybridVehicle (String[] data, VehicleModel vehicle) {
        HybridVehicleModel hybridVehicle = (HybridVehicleModel) vehicle;
        String fuelTankCapacityStr = data[8];
        String fuelConsumptionStr = data[9];
        String fuelType = data[10].toLowerCase();
        try {
            if (fuelType != null && !fuelType.isBlank()) {
                fuelType = trimText(fuelType);
                hybridVehicle.setFuelType(fuelType);
            }
            if (fuelTankCapacityStr != null && !fuelTankCapacityStr.isBlank()) {
                double fuelTankCapacity = parseCleanDouble(fuelTankCapacityStr,"A capacidade do tanque de combustivel deve ser um valor válido! (Será mantido o valor anterior)");
                if (fuelTankCapacity > 0) {
                    hybridVehicle.setFuelTankCapacity(fuelTankCapacity);
                } else {
                    view.displayError("A capacidade do tanque de combustivel deve ser valor positivo! (Será mantido o valor anterior)");
                }
            }
            if (fuelConsumptionStr != null && !fuelConsumptionStr.isBlank()) {
                double fuelConsumption = parseCleanDouble(fuelConsumptionStr,"O consumo de combustivel do motor deve ser um valor valido! (Será mantido o valor anterior)");
                if (fuelConsumption > 0) {
                    hybridVehicle.setFuelConsumption(fuelConsumption);
                } else {
                    view.displayError("O consumo do combustivel deve ser valor positivo! (Será mantido o valor anterior)");
                }
            }
            repository.update(hybridVehicle);
            view.displaySuccess("Veiculo atualizado com sucesso!");
            view.refreshVehicleList(listVehicles());
            view.displayError("veiculo não pode ser atualizado!");

        } catch (EntityNotFoundException e) {
            view.displayError(e.getMessage());
        }
    }
    public void deleteVehicleById() {
        int selectedRow = view.getSelectedRow();
        if (selectedRow == -1) {
            view.displayError("Por favor, selecione um veiculo na tabela para excluir!");
            return;
        }
        try {
            int id = view.getSelectedId();
            VehicleModel vehicleToEdit = repository.searchById(id);
            String message = "Tem certeza que deseja excluir o veículo: " + vehicleToEdit.getModel() + "?";
            boolean confirmation = view.confirmAction("Confirmar Exclusão", message);
            if (confirmation) {
                repository.deleteById(id);
                view.displaySuccess("Veiculo deletado com sucesso!");
                view.refreshVehicleList(listVehicles());
            }else {
                view.displayError("Ação cancelada, o veiculo não foi removido!");
            }
        } catch (GreenRouteException e) {
            view.displayError(e.getMessage());
        } catch (Exception e) {
            view.displayError("Erro inesperado no sistema: "+ e.getCause());
        }
    }
    public List<VehicleModel> listVehicles() {
        return repository.getVehicles();
    }
}