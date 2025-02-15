package com.ameda.works.slack_integration.api;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/slack")
public class SlackApi {


    @Value("${slack.token}")
    private String slackToken;

    private final RestTemplate restTemplate;

    @Autowired
    public SlackApi(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/message")
    public ResponseEntity<?> sendMessage(@RequestParam String channel,
                                         @RequestParam String text){
        String url = "https://slack.com/api/chat.postMessage";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(slackToken);

        Map<String,String> payload = new HashMap<>();
        payload.put("channel",channel);
        payload.put("text",text);

        HttpEntity<Map<String,String>> request = new HttpEntity<>(payload,headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url,request,String.class);
        return response;
    }

}
