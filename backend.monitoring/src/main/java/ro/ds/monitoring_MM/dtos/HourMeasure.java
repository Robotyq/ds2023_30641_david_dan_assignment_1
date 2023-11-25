package ro.ds.monitoring_MM.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Time;

@AllArgsConstructor
public class HourMeasure {

    @Getter
    private double measure;
    @Getter
    private Time timestamp;
}
