package iaService;

public interface AIPlannerService {
    String[] parseVehicle(String text);
    String[] parseCity(String text);
    String[] parseStation(String text);
    String planSmartRoute(String vehicleData, String cityData, String chargindStationData);
}
