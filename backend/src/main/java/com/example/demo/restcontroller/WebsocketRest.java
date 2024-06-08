package com.example.demo.restcontroller;

import com.example.demo.dto.DataDto;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WebsocketRest {
    private final SimpMessagingTemplate messagingTemplate;
    private final Gson gson;

    public void sendData(DataDto message){
        messagingTemplate.convertAndSend("/topic/data-sensor", gson.toJson(message));
    }
}