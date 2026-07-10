package br.upe.greenroute.model;

import br.upe.greenroute.exceptions.InvalidInputDataException;

import java.util.List;

public class ChargingStationModel {
    private int id;
    private String name;
    private String location;
    private int cityId;
    private List<String> availableConnectorsType;
    private double chargingPowerKW;
    private double pricePerKWh;
    private int availableVacancies;

    public ChargingStationModel(int id, String name, String location, int cityId, List<String> availableConnectorsType, double chargingPowerKW, double pricePerKWh, int availableVacancies) {
        this(name, location, cityId, availableConnectorsType, chargingPowerKW, pricePerKWh, availableVacancies);
        this.id = id;
    }
    public ChargingStationModel(String name, String location, int cityId, List<String> availableConnectorsType, double chargingPowerKW, double pricePerKWh, int availableVacancies) {
        this.setName(name);
        this.setLocation(location);
        this.setCityId(cityId);
        this.setAvailableConnectorsType(availableConnectorsType);
        this.setChargingPowerKW(chargingPowerKW);
        this.setPricePerKWh(pricePerKWh);
        this.setAvailableVacancies(availableVacancies);
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        if (id < 0) {
            throw new InvalidInputDataException("O ID não pode ser negativo!");
        }
        this.id = id;
    }    public String getName() {
        return name;
    }
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidInputDataException("O nome não pode ser nulo ou vazio!");
        }
        this.name = name;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        if (location == null || location.trim().isEmpty()) {
            throw new InvalidInputDataException("A localização não pode ser nula ou vazia!");
        }
        this.location = location;
    }
    public int getCityId() {
        return cityId;
    }
    public void setCityId(int cityId) {
        if (cityId < 0) {
            throw new InvalidInputDataException("O ID da cidade não pode ser negativo!");
        }
        this.cityId = cityId;
    }
    public List<String> getAvailableConnectorsType() {
        return availableConnectorsType;
    }
    public void setAvailableConnectorsType(List<String> availableConnectorsType) {
        if (availableConnectorsType == null || availableConnectorsType.isEmpty()) {
            throw new InvalidInputDataException("Deve haver pelo menos um tipo de conector disponível!");
        }
        this.availableConnectorsType = availableConnectorsType;
    }
    public double getChargingPowerKW() {
        return chargingPowerKW;
    }
    public void setChargingPowerKW(double chargingPowerKW)
    {
        if (chargingPowerKW <= 0) {
            throw new InvalidInputDataException("A potência de carregamento deve ser maior que zero!");
        }
        this.chargingPowerKW = chargingPowerKW;
    }
    public double getPricePerKWh() {
        return pricePerKWh;
    }
    public void setPricePerKWh(double pricePerKWh) {
        if (pricePerKWh < 0) {
            throw new InvalidInputDataException("O preço por kWh não pode ser negativo!");
        }        this.pricePerKWh = pricePerKWh;
    }
    public int getAvailableVacancies() {
        return availableVacancies;
    }
    public void setAvailableVacancies(int availableVacancies) {
        if (availableVacancies < 0) {
            throw new InvalidInputDataException("O número de vagas disponíveis não pode ser negativo!");
        }
        this.availableVacancies = availableVacancies;
    }
}