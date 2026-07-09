package br.upe.greenroute.controller;

import br.upe.greenroute.exceptions.AIServiceException;
import br.upe.greenroute.exceptions.EntityNotFoundException;
import br.upe.greenroute.exceptions.GreenRouteException;
import br.upe.greenroute.model.CityModel;
import br.upe.greenroute.repository.ChargingStationRepository;
import br.upe.greenroute.repository.CityRepository;
import br.upe.greenroute.view.ViewManager;
import iaService.AIPlannerService;

import java.util.List;

public class CityController extends BaseController{
    private final CityRepository repository;
    private final ViewManager view;
    private final ChargingStationRepository stationRepository;
    private final AIPlannerService aiService;
    public CityController (CityRepository repository, ViewManager view, ChargingStationRepository stationRepository, AIPlannerService aiService) {
        this.repository = repository;
        this.view = view;
        this.stationRepository = stationRepository;
        this.aiService = aiService;
    }
    public void fastAddCity() {
        String userText = view.showAiFormDialog();
        if (userText != null && !userText.trim().isEmpty()) {
            try{
               String[] aiData = aiService.parseCity(userText);
               if (aiData != null && aiData.length != 0) {
                   addCity(aiData);
               }else {
                   view.displayError("A IA não conseguiu compreender o texto informado!");
               }
            } catch (AIServiceException e) {
                view.displayError("Erro ao processar dados com IA!"+e.getMessage());
            } catch (GreenRouteException e) {
                view.displayError(e.getMessage());
            } catch (Exception e) {
                view.displayError("Erro inesperado: "+e.getMessage());
            }
        }
    }
    public void addCity (String[] aiData) {
        String[] data = view.showCityFormDialog(aiData);
        if (data == null) {
            return;
        }
        String name = data[0];
        String state = data[1].toUpperCase();
        String capitalDistanceStr = data[2];
        try {
            isAnyBlank(name, state, capitalDistanceStr);
            name = trimText(name);
            state = trimText(state);
            double capitalDistance = parseCleanDouble(capitalDistanceStr,"A distância da capital deve ser um número válido!");
            CityModel city = new CityModel(name, state, capitalDistance);
            repository.add(city);
            view.displaySuccess("Cidade cadastrada com sucesso!");
            view.refreshCityList(listCities());
        } catch (GreenRouteException e) {
            view.displayError(e.getMessage());
        }
    }
    public void filterCities() {
        String searchType = view.getSearchtype();
        String input = view.getSearchinput();
        if (input.isEmpty() || searchType.equals("Todos")) {
            view.refreshCityList(listCities());
            return;
        }
        if (searchType.equals("ID")) {
            try {
                int id = parseCleanInt(input,"O ID informado deve ser um número inteiro válido!");
                CityModel city = repository.searchById(id);
                view.refreshCityList(List.of(city));
            } catch (EntityNotFoundException e) {
                view.refreshCityList(List.of());
                view.displayError(e.getMessage());
            } catch (GreenRouteException e) {
                view.displayError(e.getMessage());
            }
        } else if (searchType.equals("Estado (UF)")) {
            try {
                String stateInput = trimText(input).toUpperCase();
                List<CityModel> filteredCities = repository.searchByState(stateInput);
                view.refreshCityList(filteredCities);
                if (filteredCities.isEmpty()) {
                    view.displayError("Nenhuma cidade encontrada no estado: " + stateInput);
                }
            } catch (GreenRouteException e) {
                view.displayError(e.getMessage());
            }
        }
    }
    public void updateCity() {
        int selectedRow = view.getSelectedRow();
        if (selectedRow == -1) {
            view.displayError("Por favor, selecione uma cidade na tabela para editar!");
            return;
        }
        try {
            int id = view.getSelectedId();
            CityModel cityToEdit = repository.searchById(id);
            String[] currentData = new String[] {
                    cityToEdit.getName(),
                    cityToEdit.getState(),
                    String.valueOf(cityToEdit.getCapitalDistance())
            };
            String[] datas = view.showCityFormDialog(currentData);
            if (datas == null) {
                return;
            }
            String name = datas[0];
            String state = datas[1];
            String capitalDistanceStr = datas[2];
            if (name != null && !name.isBlank()) {
                name = trimText(name);
                cityToEdit.setName(name);
            }
            if (state != null && !state.isBlank()) {
                state = trimText(state);
                cityToEdit.setState(state);
            }
            if (capitalDistanceStr != null && !capitalDistanceStr.isBlank()) {
                double capitalDistance = parseCleanDouble(capitalDistanceStr, "A distância da capital deve ser um numeral!");
                if (capitalDistance >= 0) {
                        cityToEdit.setCapitalDistance(capitalDistance);
                    }else {
                        view.displayError("A distância da capital deve ser maior que zero! (Será mantido o valor anterior)");
                    }
                }
            repository.update(cityToEdit);
            view.displaySuccess("Cidade atualizada com sucesso!");
            view.refreshCityList(listCities());
        } catch (GreenRouteException e) {
            view.displayError(e.getMessage());
        } catch (Exception e) {
            view.displayError("Erro inesperado no sistema: "+e.getMessage());
            }
    }
    public void deleteCityById() {
        int selectedRow = view.getSelectedRow();
        if (selectedRow == -1) {
            view.displayError("Por favor, selecione uma cidade na tabela para excluir!");
            return;
        }
        try {
            int id = view.getSelectedId();
            CityModel cityToEdit = repository.searchById(id);
            int stationsCount = stationRepository.countStationsByCityId(id);
            String message = getDelMessage(stationsCount, cityToEdit);
            boolean confirmation = view.confirmAction("Confirmar Exclusão", message);
            if (confirmation) {
                if (stationsCount > 0) {
                    stationRepository.deleteStationsByCityId(id);
                    view.refreshStationList(stationRepository.getStations());
                    }
                }
                repository.deleteById(id);
                view.displaySuccess("Cidade deletada com sucesso!");
                view.refreshCityList(listCities());
            } catch (GreenRouteException e) {
                view.displayError(e.getMessage());
            } catch (Exception e) {
                view.displayError("Erro inesperado no sistema: "+ e.getCause());
            }
    }

    private static String getDelMessage(int stationsCount, CityModel cityToEdit) {
        String message;
        if (stationsCount > 0) {
            message = "Tem certeza que deseja excluir a cidade: " + cityToEdit.getName() + "?\n"
                    + "ATENÇÃO: Esta cidade possui " + stationsCount + " eletroposto(s) "
                    + "vinculado(s) que também sera/serão excluído(s) do sistema!";
        } else {
            message = "Tem certeza que deseja excluir a cidade: " + cityToEdit.getName() + "?\n"
                    + "(Não há eletropostos vinculados a esta cidade).";
        }
        return message;
    }

    public List<CityModel> listCities() {
        return repository.getCities();
    }
}