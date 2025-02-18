package com.ameda.works.slack_integration.api;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

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
    @GetMapping("/read-message")
    public ResponseEntity<?> readMessage(@RequestParam String channel) {
        String url = "https://slack.com/api/conversations.list";

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(slackToken);  // Using Bearer token for Authorization

        // Use UriComponentsBuilder to add query parameters
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("channel", channel);

        // Create the request entity with headers (no payload for GET request)
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        // Make the GET request
        ResponseEntity<String> response = restTemplate.exchange(
                uriBuilder.toUriString(), // URL with query parameter
                HttpMethod.GET, // HTTP method
                requestEntity, // Request with headers
                String.class // Expected response type
        );

        // Return the response from Slack API
        return response;
    }

    @GetMapping("/user")
    public ResponseEntity<?> userRead(@RequestParam String user) {
        String url = "https://slack.com/api/users.info?user=" + user; // Include the user parameter in the URL

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(slackToken);  // Set the Bearer token for authentication

        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        // Make the GET request to Slack API with the URL containing the user query parameter
        ResponseEntity<String> response = restTemplate.postForEntity(url,requestEntity,String.class);
        return response;

    }



}
