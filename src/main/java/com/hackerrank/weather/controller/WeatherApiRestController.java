package com.hackerrank.weather.controller;


import com.hackerrank.weather.model.Weather;
import com.hackerrank.weather.repository.WeatherRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Comparator;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("weather")
@RequiredArgsConstructor
@CrossOrigin
public class WeatherApiRestController {

  private final WeatherRepository weatherRepository;

  @PostMapping
  public ResponseEntity saveWeatherData(@RequestBody @Valid Weather data) {
    return ResponseEntity.status(HttpStatus.CREATED).body(weatherRepository.save(data));
  }

  @GetMapping(path = "/{id}")
  public ResponseEntity getWeatherDataById(@PathVariable(required = false) @NonNull Integer id) {



          Optional<Weather> weather = weatherRepository.findById(id);
          if (weather.isPresent()) {
              return ResponseEntity.ok(weather);
          }

      return new ResponseEntity(HttpStatus.NOT_FOUND);
  }

    @GetMapping
    public ResponseEntity getWeatherData(@RequestParam(required=false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<Date> date,
                                         @RequestParam(required = false) Optional<String> city,
                                         @RequestParam(required = false) Optional<String> sort) {

        if(date.isPresent()) {
            return ResponseEntity.ok(weatherRepository.findByDate(date.get()));
        }

        if(city.isPresent()) {
            return ResponseEntity.ok(weatherRepository.findAll().stream().filter(wData -> Stream.of(city.get().split(",")).anyMatch(cityStr ->cityStr.equalsIgnoreCase(wData.getCity()))).collect(Collectors.toList()));
        }

        if(sort.isPresent()) {
            if(sort.get().equalsIgnoreCase("date")){
                return ResponseEntity.ok(weatherRepository.findAll().stream().sorted(Comparator.comparing(Weather::getDate)).collect(Collectors.toList()));
            }
            if(sort.get().equalsIgnoreCase("-date")) {
                return ResponseEntity.ok(weatherRepository.findAll().stream().sorted(Comparator.comparing(Weather::getDate).reversed()).collect(Collectors.toList()));
            }
        }

        return ResponseEntity.ok(weatherRepository.findAll().stream().sorted(Comparator.comparing(Weather::getId)).collect(Collectors.toList()));
    }

}
