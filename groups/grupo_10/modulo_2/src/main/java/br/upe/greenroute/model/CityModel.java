package br.upe.greenroute.model;

import br.upe.greenroute.exceptions.InvalidInputDataException;

public class CityModel {
    private int id;
    private String name;
    private String state;
    private double capitalDistance;

    public CityModel(int id, String name, String state, double capitalDistance) {
        this(name, state, capitalDistance);
        this.id = id;
    }
    public CityModel(String name, String state, double capitalDistance) {
        this.setName(name);
        this.setState(state);
        this.setCapitalDistance(capitalDistance);
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
    public String getName() {
        return name;
    }
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidInputDataException("O nome não pode ser nulo ou vazio!");
        }
        this.name = name;
    }
    public String getState() {
        return state;
    }
    public void setState(String state) {
        if (state == null || state.trim().isEmpty()) {
            throw new InvalidInputDataException("O estado não pode ser nulo ou vazio!");
        }
        if (state.length() != 2) {
            throw new InvalidInputDataException("O estado deve conter exatamente 2 letras");
        }
        this.state = state;
    }
    public double getCapitalDistance() {
        return capitalDistance;
    }
    public void setCapitalDistance(double capitalDistance) {
        if (capitalDistance < 0) {
            throw new InvalidInputDataException("A distância até a capital não pode ser negativa!");
        }        this.capitalDistance = capitalDistance;
    }
}