package br.upe.greenroute.repository;

import br.upe.greenroute.exceptions.EntityNotFoundException;
import br.upe.greenroute.model.CityModel;
import java.util.List;
import java.util.ArrayList;

public class CityRepository {
    private final List<CityModel> cities;
    private int id;

    public CityRepository() {
        cities = new ArrayList<>();
        id = 1;
    }

    public void add(CityModel city) {
        city.setId(this.id);
        cities.add(city);
        this.id++;
    }

    public CityModel searchById(int id) {
        for (CityModel city : cities) {
            if (city.getId() == id) {
                return city;
            }
        }
        throw new EntityNotFoundException("Cidade",id);
    }
    public List<CityModel> searchByState(String state) {
        List<CityModel> citiesByState = new ArrayList<>();
        for (CityModel city : cities) {
            if (city.getState().equalsIgnoreCase(state)) {
                citiesByState.add(city);
            }
        }
        return citiesByState;
    }
    public void update(CityModel updateCity) {
        for (int i = 0; i < cities.size(); i++) {
            if (cities.get(i).getId()==updateCity.getId()) {
                cities.set(i, updateCity);
                return;
            }
        }
        throw new EntityNotFoundException("Cidade",id);
    }
    public void deleteById(int id) {
        boolean removed = cities.removeIf(city -> city.getId() == id);
        if (!removed) {
            throw new EntityNotFoundException("Cidade",id);
        }
    }
    public List<CityModel> getCities() {
        return cities;
    }
    public boolean isEmpty() {
        return cities.isEmpty();
    }
}