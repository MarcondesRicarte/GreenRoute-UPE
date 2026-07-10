package br.upe.greenroute.view.gui;

public interface IVehicleGui {
    String[] getVehicleInputs();
    void setVehicleData(String Type, String model, String maximumAutonomy, String currentBatteryCharge, String consumptionKWhPerKm, String fullRechargeTime, String connectorType, String fastCharging, String fuelTankCapacity, String fuelConsumption, String fuelType);
    boolean isConfirmed();

}
