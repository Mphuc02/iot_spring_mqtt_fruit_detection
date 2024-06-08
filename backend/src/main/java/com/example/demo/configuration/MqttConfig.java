package com.example.demo.configuration;

import com.example.demo.dto.DataDto;
import com.example.demo.model.Data;
import com.example.demo.repository.DataRepository;
import com.example.demo.restcontroller.WebsocketRest;
import com.example.demo.service.DataService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.Date;

@Configuration
@RequiredArgsConstructor
public class MqttConfig
{
    private final DataRepository dataRepository;

    @Bean
    public MqttPahoClientFactory mqttPahoClientFactory() throws UnknownHostException {
        String ip = Inet4Address.getLocalHost().getHostAddress();

        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(new String[]{String.format("tcp://%s:1883", ip)});
        options.setUserName("phuc");
        String password = "2002";
        options.setPassword(password.toCharArray());
        options.setCleanSession(true);

        factory.setConnectionOptions(options);
        return factory;
    }

    @Bean
    public MessageChannel mqttInputChannel()
    {
        return new DirectChannel();
    }

    @Bean
    public MessageProducer inbound() throws UnknownHostException {
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter("serverIn", mqttPahoClientFactory(), "#");
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(2);
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler()
    {
        return new MessageHandler()
        {
            @Autowired WebsocketRest websocketRest;
            @Autowired DataService dataService;
            @Override
            public void handleMessage(Message<?> message) throws MessagingException
            {
                Gson gson = new Gson();
                String topic = message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC).toString();
                if(topic.equals("sensor/data"))
                {
                    System.out.println("nhận dữ liệu thành công");
                    Data data = gson.fromJson(message.getPayload().toString(), Data.class);
                    data.setTime(new Date().getTime());
                    this.dataService.saveData(data);
                    websocketRest.sendData(new DataDto(data));
                }
                System.out.println(message.getPayload());
            }
        };
    }

    @Bean
    public MessageChannel mqttOutBoundChannel()
    {
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttOutBoundChannel")
    public MessageHandler mqttOutBound() throws UnknownHostException {
        System.out.println(123);
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler("serverOut", mqttPahoClientFactory());
        messageHandler.setAsync(true);
        messageHandler.setDefaultTopic("outTopic");
        return messageHandler;
    }

    @Bean
    public Data getLastData(){
        Data data = dataRepository.getLastestResult();
        if(data == null)
            data = Data.builder()
                    .mq3(0f)
                    .mq4(0f)
                    .humidity(0f)
                    .temperature(0f)
                    .build();
        return data;
    }
}