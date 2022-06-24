package com.example.pointapi.event;

import com.example.pointapi.event.domain.Event;
import com.example.pointapi.event.domain.EventType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Getter
@Component
public class EventMapper {

//    @Autowired
//    private ObjectMapper objectMapper;


    private final Map<EventType, Event> platFormOEmbededMapper = new HashMap<>();

    public EventMapper() {
        platFormOEmbededMapper.put(DomainInfo.YOUTUBE_DOMAIN, new YoutubePlatform());
    }

    public void findOembededResponseByPlatform(String hostUrl, String searchUrl) {
        if (!platFormOEmbededMapper.containsKey(hostUrl)) {
            throw new NotCollectDataException();
        }

        Platform platform = platFormOEmbededMapper.get(hostUrl);
        return platform.generatePlatformOembeded(searchUrl, objectMapper);
    }
}
