package br.upe.greenroute.controller;

import br.upe.greenroute.exceptions.InvalidInputDataException;

import java.util.ArrayList;
import java.util.List;

public class BaseController {
    protected void isAnyBlank(String... values) {
        for (String value : values) {
            if (value == null || value.isBlank()) {
                throw new InvalidInputDataException("Nenhum dado pode estar vazio!");
            }
        }
    }
    protected String trimText(String input) {
        if (input == null) return "";
        return input.trim();
    }
    protected int parseCleanInt (String value, String message) {
        String clean = trimText(value).replaceAll("[^0-9]","");
        if (clean.isEmpty()) {
            throw new InvalidInputDataException(message);
        }
        try {
            return Integer.parseInt(clean);
        } catch (NumberFormatException e) {
            throw new InvalidInputDataException(message);
        }
    }
    protected double parseCleanDouble (String value, String message) {
        String clean = trimText(value).replace(",",".").replaceAll("[^0-9.]","");
        if (clean.isEmpty()) {
            throw new InvalidInputDataException(message);
        }
        try {
            return Double.parseDouble(clean);
        } catch (NumberFormatException e) {
            throw new InvalidInputDataException(message);
        }
    }
    protected List<String> cleanStringToList (String value) {
        List<String> stringList = new ArrayList<>();
        if (value == null || value.isBlank()) {
            return stringList;
        }
        String[] items = value.split(",");
        for (String item : items) {
            String cleanItem = item.trim();
            if (!cleanItem.isEmpty()) {
                stringList.add(cleanItem);
            }
        }
        return stringList;
    }
}
