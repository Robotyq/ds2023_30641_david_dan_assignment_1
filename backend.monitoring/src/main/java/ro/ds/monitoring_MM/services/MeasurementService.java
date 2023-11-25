package ro.ds.monitoring_MM.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.ds.monitoring_MM.dtos.HourMeasure;
import ro.ds.monitoring_MM.dtos.builders.RawBuilder;
import ro.ds.monitoring_MM.entities.Device;
import ro.ds.monitoring_MM.entities.Measurement;
import ro.ds.monitoring_MM.repositories.DeviceRepository;
import ro.ds.monitoring_MM.repositories.MeasurementRepository;

import java.sql.Time;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MeasurementService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MeasurementService.class);
    private final MeasurementRepository measurementRepository;
    private final DeviceRepository deviceRepository;

    @Autowired
    public MeasurementService(MeasurementRepository measurementRepository, DeviceRepository deviceRepository) {
        this.measurementRepository = measurementRepository;
        this.deviceRepository = deviceRepository;
    }

    public List<HourMeasure> getConsumptionForDate(UUID deviceId, Date date) {
        List<Measurement> measurements = measurementRepository.findByDeviceAndDate(deviceId, new java.sql.Date(date.getTime()));
        ArrayList<HourMeasure> hourMeasures = measurements
                .stream()
                .map(RawBuilder::toRawBuilder)
                .sorted(Comparator.comparing(HourMeasure::getTimestamp))
                .collect(Collectors.toCollection(ArrayList::new));
        List<HourMeasure> hourConsumption = new ArrayList<>();
        Time hour = new Time(1, 0, 0);
        if (hourMeasures.size() == 0) {
            return hourConsumption;
        }
        double initialConsumption = hourMeasures.get(0).getMeasure();
        double consumption = 0;
        for (int i = 0; i < hourMeasures.size(); i++) {
            HourMeasure m = hourMeasures.get(i);
            if (m.getTimestamp().before(hour)) {
                if (m.getMeasure() < initialConsumption) {
                    consumption += hourMeasures.get(i - 1).getMeasure() - initialConsumption;
                    initialConsumption = m.getMeasure();
                }
            } else {
                consumption += hourMeasures.get(i - 1).getMeasure() - initialConsumption;
                hourConsumption.add(new HourMeasure(consumption, hour));
                consumption = 0;
                hour.setTime(hour.getTime() + 3600000);
                initialConsumption = m.getMeasure();
            }
        }
        return hourConsumption;
    }

    public void insertOrUpdate(Device updatedDevice) {
        deviceRepository.save(updatedDevice);
    }

    public void delete(UUID id) {
        deviceRepository.deleteById(id);
    }

    public void insert(Measurement meas) {
        Measurement saved = measurementRepository.save(meas);
        LOGGER.info("Measurement saved: {}", saved);
        double liveConsumption = computeLiveConsumption(saved.getDevice());
        sendToUI(liveConsumption);
    }

    private double computeLiveConsumption(Device device) {
        List<Measurement> fromLastHour = measurementRepository.getFromLastHour(device.getId());
        double consumption = 0;
        ArrayList<HourMeasure> hourMeasures = fromLastHour
                .stream()
                .map(RawBuilder::toRawBuilder)
                .sorted(Comparator.comparing(HourMeasure::getTimestamp))
                .collect(Collectors.toCollection(ArrayList::new));
        if (hourMeasures.size() == 0) {
            return 0;
        }
        double lastConsumption = hourMeasures.get(0).getMeasure();
        for (int i = 1; i < hourMeasures.size(); i++) {
            double measure = hourMeasures.get(i).getMeasure();
            double delta = measure - lastConsumption;
            if (delta >= 0) {
                consumption += delta;
            } else {
                consumption += measure;
            }
            lastConsumption = measure;
        }
        return consumption;
    }

    private void sendToUI(double liveConsumption) {
        //TODO: send to UI via websocket
        LOGGER.warn("Live consumption: {}", liveConsumption);
    }
}
