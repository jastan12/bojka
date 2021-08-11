package pl.bojka.model;


import com.opencsv.bean.CsvBindByPosition;
import lombok.Data;

import java.time.LocalTime;

@Data
public class BuoyDTO {
    @CsvBindByPosition(position = 0)
    String measurementTime;

    @CsvBindByPosition(position = 1)
    double voltage;

    @CsvBindByPosition(position = 2)
    double temperature;

    @CsvBindByPosition(position = 3)
    double oxygen;


    @Override
    public String toString() {
        return "measurementTime= " + measurementTime +
                ", temperature= " + temperature +
                ", oxygen[mg/dm3]= " + oxygen;
    }
}
