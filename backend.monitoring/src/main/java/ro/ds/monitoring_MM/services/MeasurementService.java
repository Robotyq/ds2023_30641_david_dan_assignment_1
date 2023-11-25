package ro.ds.monitoring_MM.services;

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
        long initialConsumption = hourMeasures.get(0).getMeasure();
        long consumption = 0;
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
}
