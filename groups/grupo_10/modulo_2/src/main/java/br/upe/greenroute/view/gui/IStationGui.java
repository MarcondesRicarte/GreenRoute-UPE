package br.upe.greenroute.view.gui;

public interface IStationGui {
    String[] getStationInputs();
    void setStationData(String name,String location,String cityId,String availableConnectorsType,String chargingPowerKW,String pricePerKWh,String availableVacancies);
    boolean isConfirmed();
}
