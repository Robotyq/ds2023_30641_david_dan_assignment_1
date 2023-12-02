package ro.ds.monitoring_MM.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.ds.monitoring_MM.dtos.HourMeasure;
import ro.ds.monitoring_MM.dtos.LiveConsumption;
import ro.ds.monitoring_MM.dtos.builders.RawBuilder;
import ro.ds.monitoring_MM.entities.Device;
import ro.ds.monitoring_MM.entities.Measurement;
import ro.ds.monitoring_MM.repositories.DeviceRepository;
import ro.ds.monitoring_MM.repositories.MeasurementRepository;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MeasurementService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MeasurementService.class);
    private final MeasurementRepository measurementRepository;
    private final DeviceRepository deviceRepository;
    private final WebsocketService socketService;


    @Autowired
    public MeasurementService(MeasurementRepository measurementRepository, DeviceRepository deviceRepository, WebsocketService socketService) {
        this.measurementRepository = measurementRepository;
        this.deviceRepository = deviceRepository;
        this.socketService = socketService;
    }

    public List<HourMeasure> getConsumptionForDate(UUID deviceId, Date date) {
        List<Measurement> measurements = measurementRepository.findByDeviceAndDate(deviceId, new java.sql.Date(date.getTime()));
        ArrayList<HourMeasure> hourMeasures = measurements
                .stream()
                .map(RawBuilder::toRawBuilder)
                .sorted(Comparator.comparing(HourMeasure::getHour))
                .collect(Collectors.toCollection(ArrayList::new));
        List<HourMeasure> hourConsumption = new ArrayList<>();
        LocalTime hour = LocalTime.of(0, 59, 59);

        if (hourMeasures.size() == 0) {
            return hourConsumption;
        }
        double lastConsumption = hourMeasures.get(0).getMeasure();
        double consumption = 0;
        for (int i = 1; i < hourMeasures.size(); i++) {
            HourMeasure m = hourMeasures.get(i);
            while (hour.isBefore(m.getHour())) {
                hourConsumption.add(new HourMeasure(consumption, hour.minusSeconds(59 * 61)));
                consumption = 0;
                hour = hour.plusHours(1);
            }
            double delta = m.getMeasure() - lastConsumption;
            if (delta >= 0) {
                consumption += delta;
            } else {
                consumption += m.getMeasure();
            }

            lastConsumption = m.getMeasure();
        }
        hourConsumption.add(new HourMeasure(consumption, hour.minusSeconds(59 * 61)));
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
        Device device = deviceRepository.findById(saved.getDevice().getId()).get();
        if (liveConsumption > device.getMaxConsumption())
            sendToUI(liveConsumption, saved.getDevice());
    }

    private double computeLiveConsumption(Device device) {
        List<Measurement> fromLastHour = measurementRepository.getFromLastHour(device.getId());
        double consumption = 0;
        ArrayList<HourMeasure> hourMeasures = fromLastHour
                .stream()
                .map(RawBuilder::toRawBuilder)
                .sorted(Comparator.comparing(HourMeasure::getHour))
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

    private void sendToUI(double liveConsumption, Device device) {
        LiveConsumption consumptionDTO = new LiveConsumption(device.getId().toString(), liveConsumption);
        LOGGER.warn("Live consumption: {}", consumptionDTO);
        socketService.sendConsumption(consumptionDTO);
    }

    public void startChat(UUID chatId) {
        socketService.startChat(chatId);
    }
}
