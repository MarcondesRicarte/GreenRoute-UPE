package br.upe.greenroute.model;

import br.upe.greenroute.exceptions.InvalidInputDataException;

public class HybridVehicleModel extends VehicleModel {
    private double fuelTankCapacity;
    private double fuelConsumption;
    private String fuelType;

    public HybridVehicleModel(int id, String model, double maximumAutonomy, double currentBatteryCharge, double consumptionKWhPerKm, int fullRechargeTime, double fuelTankCapacity, double fuelConsumption, String fuelType) {
        this(model, maximumAutonomy, currentBatteryCharge, consumptionKWhPerKm, fullRechargeTime,fuelTankCapacity, fuelConsumption, fuelType);
        this.id = id;
    }
    public HybridVehicleModel(String model, double maximumAutonomy, double currentBatteryCharge, double consumptionKWhPerKm, int fullRechargeTime, double fuelTankCapacity, double fuelConsumption, String fuelType) {
        super ( model, maximumAutonomy, currentBatteryCharge, consumptionKWhPerKm, fullRechargeTime);
        this.setFuelTankCapacity(fuelTankCapacity);
        this.setFuelConsumption(fuelConsumption);
        this.setFuelType(fuelType);
    }

    public double getFuelTankCapacity() {
        return fuelTankCapacity;
    }
    public void setFuelTankCapacity(double fuelTankCapacity) {
        if (fuelTankCapacity <= 0) {
            throw new InvalidInputDataException("A capacidade do tanque de combustível deve ser maior que zero!");
        }
        this.fuelTankCapacity = fuelTankCapacity;
    }
    public double getFuelConsumption() {
        return fuelConsumption;
    }
    public void setFuelConsumption(double fuelConsumption) {
        if (fuelConsumption <= 0) {
            throw new InvalidInputDataException("O consumo de combustível deve ser maior que zero!");
        }
        this.fuelConsumption = fuelConsumption;
    }
    public String getFuelType() {
        return fuelType;
    }
    public void setFuelType(String fuelType) {
        if (fuelType == null || fuelType.trim().isEmpty()) {
            throw new InvalidInputDataException("O tipo de combustível não pode ser nulo ou vazio!");
        }
        this.fuelType = fuelType;
    }
}