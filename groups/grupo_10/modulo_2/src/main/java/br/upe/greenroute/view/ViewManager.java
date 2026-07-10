package br.upe.greenroute.view;

import br.upe.greenroute.model.ChargingStationModel;
import br.upe.greenroute.model.CityModel;
import br.upe.greenroute.model.VehicleModel;
import br.upe.greenroute.view.gui.IAiGui;
import br.upe.greenroute.view.gui.IMainGui;
import br.upe.greenroute.view.gui.ICityGui;

import java.util.List;

public class ViewManager {
    private final IMainGui mainGui;

    public ViewManager(IMainGui mainGui) {
        this.mainGui = mainGui;
    }
    public void displayError (String message) {
        mainGui.errorMessage(message);
    }
    public void displaySuccess (String message) {
        mainGui.successMessage(message);
    }
    public String getSearchtype() {
        return mainGui.getSearchType();
    }
    public String getSearchinput() {
        return mainGui.getSearchInput();
    }
    public int getSelectedRow() {
        return mainGui.getSelectedItemRow();
    }
    public int getSelectedId() {
        return mainGui.getSelectedItemId();
    }
    public boolean confirmAction(String title, String message) {
        return mainGui.confirmAction(title, message);
    }

    public void refreshCityList (List<CityModel> cities) {
        mainGui.updateCityTable(cities);
    }
    public String[] showCityFormDialog(String[] existingData) {
        return mainGui.showCityFormDialog(existingData);
    }

    public void refreshStationList (List<ChargingStationModel> stations) {
        mainGui.updateStationTable(stations);
    }
    public String[] showStationFormDialog(String[] existingData) {
        return mainGui.showStationFormDialog(existingData);
    }

    public void refreshVehicleList (List<VehicleModel> vehicles) {
        mainGui.updateVehicleTable(vehicles);
    }
    public String[] showVehicleFormDialog(String type, String[] existingData) {
        return mainGui.showVehicleFormDialog(type, existingData);
    }
    public String showAiFormDialog() {
        return mainGui.showAiDialog();
    }
    public int getSelectedVehicleId() {
        return mainGui.getSelectedVehicleId();
    }
    public int getSelectedCityId() {
        return mainGui.getSelectedCityId();
    }
    public void showTripReport(String report) {
        mainGui.showTripReportDialog(report);
    }
}