package ro.ds.monitoring_MM.dtos.builders;

import ro.ds.monitoring_MM.dtos.HourMeasure;
import ro.ds.monitoring_MM.entities.Measurement;

public class RawBuilder {

    private RawBuilder() {
    }

    public static HourMeasure toRawBuilder(Measurement measurement) {
        return new HourMeasure(measurement.getMeasure(), measurement.getTimestamp().toLocalDateTime().toLocalTime());
    }

}
