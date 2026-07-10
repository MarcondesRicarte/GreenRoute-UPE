package br.upe.greenroute.view.gui;

import br.upe.greenroute.model.ChargingStationModel;
import br.upe.greenroute.model.CityModel;
import br.upe.greenroute.model.VehicleModel;

import java.util.List;

public interface IMainGui {
    void errorMessage(String message);
    void successMessage(String message);
    String getSearchType();
    String getSearchInput();
    int getSelectedItemRow();
    int getSelectedItemId();
    boolean confirmAction(String title, String message);

    void updateCityTable(List<CityModel> cities);
    String[] showCityFormDialog(String[] existingData);
    void updateStationTable(List<ChargingStationModel> stations);
    String[] showStationFormDialog(String[] existingData);
    void updateVehicleTable(List<VehicleModel> vehicles);
    String[] showVehicleFormDialog(String type, String[] existingData);
    String showAiDialog();
    int getSelectedVehicleId();
    int getSelectedCityId();
    void showTripReportDialog(String report);
}
