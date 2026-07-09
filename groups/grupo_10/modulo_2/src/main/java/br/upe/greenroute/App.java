package br.upe.greenroute;

import br.upe.greenroute.controller.ChargingStationController;
import br.upe.greenroute.controller.CityController;
import br.upe.greenroute.controller.TripController;
import br.upe.greenroute.controller.VehicleController;
import br.upe.greenroute.repository.ChargingStationRepository;
import br.upe.greenroute.repository.CityRepository;
import br.upe.greenroute.repository.VehicleRepository;
import br.upe.greenroute.view.ViewManager;
import br.upe.greenroute.view.gui.GreenRouteView;
import br.upe.greenroute.view.gui.MainView;
import iaService.AIPlannerService;
import iaService.ConexaoGemini;

import javax.swing.SwingUtilities;
import java.awt.*;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AIPlannerService aiService = new ConexaoGemini();
            CityRepository cityRepository = new CityRepository();
            ChargingStationRepository stationRepository = new ChargingStationRepository();
            VehicleRepository vehicleRepository = new VehicleRepository();
            MainView mainView = new MainView();
            ViewManager viewManager = new ViewManager(mainView);
            CityController cityController = new CityController(cityRepository, viewManager, stationRepository, aiService);
            ChargingStationController stationController = new ChargingStationController(stationRepository, cityRepository, viewManager, aiService);
            VehicleController vehicleController = new VehicleController(vehicleRepository, viewManager, aiService);
            TripController tripController = new TripController(vehicleRepository, cityRepository, stationRepository, aiService, viewManager);
            mainView.getBtnAddCity().addActionListener(e -> {
                cityController.addCity(null);
            });
            mainView.getBtnUpdtCity().addActionListener(e -> {
                cityController.updateCity();
            });
            mainView.getBtnDelCity().addActionListener(e -> {
                cityController.deleteCityById();
            });
            mainView.getBtnCityFilter().addActionListener(e -> {
                cityController.filterCities();
            });
            mainView.getCbSearchCity().addActionListener(e -> {
                String selected = (String) mainView.getCbSearchCity().getSelectedItem();
                if ("Todos".equals(selected)) cityController.filterCities();
            });
            mainView.getBtnAddStation().addActionListener(e -> {
                stationController.addChargingStation(null);
            });
            mainView.getBtnUpdStation().addActionListener(e -> {
                stationController.updateChargingStation();
            });
            mainView.getBtnDelStation().addActionListener(e -> {
                stationController.deleteChargingStationById();
            });
            mainView.getBtnStationFilter().addActionListener(e -> {
                stationController.filterChargingStations();
            });
            mainView.getCbSearchStation().addActionListener(e -> {
                String selected = (String) mainView.getCbSearchStation().getSelectedItem();
                if ("Todos".equals(selected)) stationController.filterChargingStations();
            });
            mainView.getBtnAddVehicle().addActionListener(e -> {
                vehicleController.addVehicle(null);
            });
            mainView.getBtnUpdVehicle().addActionListener(e -> {
                vehicleController.updateVehicle();
            });
            mainView.getBtnDelVehicle().addActionListener(e -> {
                vehicleController.deleteVehicleById();
            });
            mainView.getBtnVehicleFilter().addActionListener(e -> {
                vehicleController.filterVehicles();
            });
            mainView.getCbSearchVehicle().addActionListener(e -> {
                String selected = (String) mainView.getCbSearchVehicle().getSelectedItem();
                if ("Todos".equals(selected)) vehicleController.filterVehicles();
            });
            mainView.getBtnFastAddCity().addActionListener(e -> {
                mainView.getBtnFastAddCity().setEnabled(false);
                mainView.getBtnFastAddCity().setText("Processando...");
                mainView.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                new Thread(() -> {
                    try {
                        cityController.fastAddCity();
                    }finally {
                        javax.swing.SwingUtilities.invokeLater(() -> {
                            mainView.getBtnFastAddCity().setEnabled(true);
                            mainView.getBtnFastAddCity().setText("Adição Rápida");
                            mainView.setCursor(Cursor.getDefaultCursor());
                        });
                    }
                }).start();
            });
            mainView.getBtnFastAddStation().addActionListener(e -> {
                mainView.getBtnFastAddStation().setEnabled(false);
                mainView.getBtnFastAddStation().setText("Processando...");
                mainView.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                new Thread(() -> {
                    try {
                        stationController.fastAddStation();
                    }finally {
                        javax.swing.SwingUtilities.invokeLater(() -> {
                            mainView.getBtnFastAddStation().setEnabled(true);
                            mainView.getBtnFastAddStation().setText("Adição Rápida");
                            mainView.setCursor(Cursor.getDefaultCursor());
                        });
                    }
                }).start();
            });
            mainView.getBtnFastAddVehicle().addActionListener(e -> {
                mainView.getBtnFastAddVehicle().setEnabled(false);
                mainView.getBtnFastAddVehicle().setText("Processando...");
                mainView.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                new Thread(() -> {
                    try {
                        vehicleController.fastAddVehicle();
                    }finally {
                        javax.swing.SwingUtilities.invokeLater(() -> {
                            mainView.getBtnFastAddVehicle().setEnabled(true);
                            mainView.getBtnFastAddVehicle().setText("Adição Rápida");
                            mainView.setCursor(Cursor.getDefaultCursor());
                        });
                    }
                }).start();
            });
            mainView.getBtnGenerateRoute().addActionListener(e -> {
                mainView.getBtnGenerateRoute().setEnabled(false);
                mainView.getBtnGenerateRoute().setText("Em andamento...");
                mainView.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                new Thread(() -> {
                    try {
                    tripController.executeTripSimulation();
                    }finally {
                        javax.swing.SwingUtilities.invokeLater(() -> {
                            mainView.getBtnGenerateRoute().setEnabled(true);
                            mainView.getBtnGenerateRoute().setText("Gerar Rota");
                            mainView.setCursor(Cursor.getDefaultCursor());
                        });
                    }
                }).start();
            });
            GreenRouteView startFrame = new GreenRouteView(() -> {
                mainView.setVisible(true);
            });
            startFrame.setVisible(true);
        });
    }
}