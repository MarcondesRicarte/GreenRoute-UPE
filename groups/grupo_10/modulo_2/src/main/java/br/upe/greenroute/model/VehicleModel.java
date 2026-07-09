package br.upe.greenroute.model;

import br.upe.greenroute.exceptions.InvalidInputDataException;

public abstract class VehicleModel {
    protected int id;
    protected String model;
    protected double maximumAutonomy;
    protected double currentBatteryCharge;
    protected double consumeKwhPerKm;
    protected int fullRechargeTime;

    public VehicleModel(int id, String model, double maximumAutonomy, double currentBatteryCharge, double consumeKwhPerKm, int fullRechargeTime) {
        this(model, maximumAutonomy, currentBatteryCharge, consumeKwhPerKm, fullRechargeTime);
        this.id = id;
    }
    public VehicleModel (String model, double maximumAutonomy, double currentBatteryCharge, double consumeKwhPerKm, int fullRechargeTime) {
        this.setModel(model);
        this.setMaximumAutonomy(maximumAutonomy);
        this.setCurrentBatteryCharge(currentBatteryCharge);
        this.setConsumeKwhPerKm(consumeKwhPerKm);
        this.setFullRechargeTime(fullRechargeTime);
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        if (id < 0) {
            throw new InvalidInputDataException("O ID não pode ser negativo!");
        }
        this.id = id;
    }
    public String getModel() {
        return model;
    }
    public void setModel(String model) {
        if (model == null || model.trim().isEmpty()) {
            throw new InvalidInputDataException("O modelo não pode ser nulo ou vazio!");
        }
        this.model = model;
    }
    public double getMaximumAutonomy() {
        return maximumAutonomy;
    }
    public void setMaximumAutonomy(double maximumAutonomy) {
        if (maximumAutonomy <= 0) {
            throw new InvalidInputDataException("A autonomia máxima deve ser maior que zero!");
        }
        this.maximumAutonomy = maximumAutonomy;
    }
    public double getCurrentBatteryCharge() {
        return currentBatteryCharge;
    }
    public void setCurrentBatteryCharge(double currentBatteryCharge) {
        if (currentBatteryCharge < 0 || currentBatteryCharge > 100) {
            throw new InvalidInputDataException("A carga atual da bateria deve estar entre 0 e 100!");
        }
        this.currentBatteryCharge = currentBatteryCharge;
    }
    public double getConsumeKwhPerKm() {
        return consumeKwhPerKm;
    }
    public void setConsumeKwhPerKm(double consumeKwhPerKm) {
        if (consumeKwhPerKm <= 0) {
            throw new InvalidInputDataException("O consumo por km deve ser maior que zero!");
        }
        this.consumeKwhPerKm = consumeKwhPerKm;
    }
    public int getFullRechargeTime() {
        return fullRechargeTime;
    }
    public void setFullRechargeTime(int fullRechargeTime) {
        if (fullRechargeTime <= 0) {
            throw new InvalidInputDataException("O tempo de recarga total deve ser maior que zero!");
        }
        this.fullRechargeTime = fullRechargeTime;
    }
    public double calculateCurrentAutonomy() {
        return maximumAutonomy * (currentBatteryCharge / 100);
    }
}
