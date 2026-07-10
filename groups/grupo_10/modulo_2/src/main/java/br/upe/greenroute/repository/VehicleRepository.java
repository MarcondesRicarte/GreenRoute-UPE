package br.upe.greenroute.repository;

import br.upe.greenroute.exceptions.EntityNotFoundException;
import br.upe.greenroute.model.VehicleModel;
import java.util.List;
import java.util.ArrayList;
public class VehicleRepository {

    private final List<VehicleModel> vehicles;
    private int id;

    public VehicleRepository() {
        vehicles = new ArrayList<>();
        id = 1;
    }

    public void add(VehicleModel vehicle) {
        vehicle.setId(this.id);
        vehicles.add(vehicle);
        this.id++;
    }

    public VehicleModel searchById(int id) {
        for (VehicleModel vehicle : vehicles) {
            if (vehicle.getId() == id) {
                return vehicle;
            }
        }
        throw new EntityNotFoundException("Veículo",id);
    }

    public void update(VehicleModel updateVehicle) {
        for (int i = 0; i < vehicles.size(); i++) {
            if (vehicles.get(i).getId() == updateVehicle.getId()) {
                vehicles.set(i, updateVehicle);
                return;
            }
        }
        throw new EntityNotFoundException("Veículo",id);
    }
    public void deleteById(int id) {
        boolean removed = vehicles.removeIf(vehicle -> vehicle.getId() == id);
        if (!removed) {
            throw new EntityNotFoundException("Veículo",id);
        }
    }
    public List<VehicleModel> getVehicles() {
        return vehicles;
    }
    public boolean isEmpty() {
        return vehicles.isEmpty();
    }
}