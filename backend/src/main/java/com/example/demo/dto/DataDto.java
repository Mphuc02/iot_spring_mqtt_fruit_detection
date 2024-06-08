package com.example.demo.dto;

import com.example.demo.model.Data;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.ObjectUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataDto {
    private Long id;
    private Float temperature;
    private Float humidity;
    private Float mq3;
    private String time;
    private Float mq4;
    public DataDto(Data data)
    {
        if(!ObjectUtils.isEmpty(data))
        {
            this.id = data.getId();
            this.temperature = data.getTemperature();
            this.humidity = data.getHumidity();
            this.mq3 = data.getMq3();
            this.mq4 = data.getMq4();

            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
            this.time = sdf.format(new Date(data.getTime()));
        }
    }
}
