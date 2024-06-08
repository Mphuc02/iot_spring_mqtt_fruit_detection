package com.example.demo.restcontroller;

import com.example.demo.mqtt.MqttGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mqtt/api")
public class MqttRestController
{
    @Autowired
    private final MqttGateway mqttGateway;;

    @Autowired
    public MqttRestController(MqttGateway mqttGateway)
    {
        this.mqttGateway = mqttGateway;
    }

}
