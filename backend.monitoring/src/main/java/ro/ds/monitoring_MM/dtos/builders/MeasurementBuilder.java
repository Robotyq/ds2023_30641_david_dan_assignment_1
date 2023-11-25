package ro.ds.monitoring_MM.dtos.builders;

import ro.ds.monitoring_MM.dtos.MeasureJSON;
import ro.ds.monitoring_MM.entities.Device;
import ro.ds.monitoring_MM.entities.Measurement;

public class MeasurementBuilder {

    private MeasurementBuilder() {
    }

    public static Measurement getMeasurement(MeasureJSON dto) {
        Device mockDevice = new Device();
        mockDevice.setId(dto.getDevice_id());
        return new Measurement(null, mockDevice, dto.getTimestamp(), dto.getMeasure());
    }

}
