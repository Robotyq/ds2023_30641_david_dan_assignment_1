package ro.ds.monitoring_MM.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.ds.monitoring_MM.dtos.HourMeasure;
import ro.ds.monitoring_MM.services.MeasurementService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    public ResponseEntity<List<HourMeasure>> getHistory(@PathVariable("device_id") UUID deviceId,
                                                        @RequestParam String dateString) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = dateFormat.parse(dateString);
        List<HourMeasure> history = measurementService.getConsumptionForDate(deviceId, date);
        return new ResponseEntity<>(history, HttpStatus.OK);
    }

}
