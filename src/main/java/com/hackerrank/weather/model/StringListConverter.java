package com.hackerrank.weather.model;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class StringListConverter implements
        AttributeConverter<List<Double>, String> {

    private static final String SEPARATOR = ", ";

    @Override
    public String convertToDatabaseColumn(List<Double> temperatures) {
        if (temperatures == null) {
            return null;
        }
      return   temperatures.stream().map(String::valueOf).collect(Collectors.joining(SEPARATOR));
    }

    @Override
    public List<Double> convertToEntityAttribute(String temperatures) {
        if (temperatures == null || temperatures.isEmpty()) {
            return null;
        }

        String[] pieces = temperatures.split(SEPARATOR);

        if (pieces == null || pieces.length == 0) {
            return null;
        }

        return Arrays.asList(pieces).stream().map(strTemp ->Double.parseDouble(strTemp)).collect(Collectors.toList());
    }
}
