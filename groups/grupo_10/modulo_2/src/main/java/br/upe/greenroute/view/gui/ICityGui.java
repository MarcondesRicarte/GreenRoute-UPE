package br.upe.greenroute.view.gui;

public interface ICityGui {
    String[] getCityInputs();
    void setCityData(String name, String state, String distance);
    boolean isConfirmed();
}