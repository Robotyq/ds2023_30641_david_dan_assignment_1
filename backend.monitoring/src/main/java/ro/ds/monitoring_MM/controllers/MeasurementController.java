package ro.ds.monitoring_MM.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.ds.monitoring_MM.dtos.HourMeasure;
import ro.ds.monitoring_MM.services.MeasurementService;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping(value = "/measurement")
public class MeasurementController {

    private final MeasurementService measurementService;

    @Autowired
    public MeasurementController(MeasurementService measurementService) {
        this.measurementService = measurementService;
    }

    @GetMapping(value = "/{device_id}")
    public ResponseEntity<List<HourMeasure>> getHistory(@PathVariable("device_id") UUID deviceId) {
        Date date = new Date(123, 11, 24);
        List<HourMeasure> history = measurementService.getConsumptionForDate(deviceId, date);
        return new ResponseEntity<>(history, HttpStatus.OK);
    }
}
