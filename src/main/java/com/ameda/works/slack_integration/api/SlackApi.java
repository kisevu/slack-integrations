package com.ameda.works.slack_integration.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
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
        HttpHeaders headers = setHeadersJson(slackToken);
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

        HttpHeaders headers = setHeadersJson(slackToken);

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

        HttpHeaders headers = setHeadersJson(slackToken);

        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        // Make the GET request to Slack API with the URL containing the user query parameter
        ResponseEntity<String> response = restTemplate.postForEntity(url,requestEntity,String.class);
        return response;

    }

    @GetMapping("/members")
    public ResponseEntity<?> getMembers(){
        String url = "https://slack.com/api/users.list";
        HttpHeaders headers = setHeadersJson(slackToken);

        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        // Make the GET request to Slack API with the URL containing the user query parameter
        ResponseEntity<String> response = restTemplate.postForEntity(url,requestEntity,String.class);
        return response;
    }

    @PostMapping(value = "/upload-profile", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> uploadProfile(@RequestParam("image") MultipartFile image) {
        // Slack's API URL for setting a user's profile photo
        String url = "https://slack.com/api/users.setPhoto";

        HttpHeaders headers = setHeadersMedia(slackToken);

        // Create a MultiValueMap to hold the form data
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", image.getResource());  // Use the file's resource as input (this is what Slack expects)

        // Create the HttpEntity object with the body and headers
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // Make the POST request to the Slack API with the correct content type and file
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

        // Return the response from Slack API
        return response;
    }

    private HttpHeaders setHeadersJson(String slackToken){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(slackToken);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }

    private HttpHeaders setHeadersMedia(String slackToken){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(slackToken);
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
        return httpHeaders;
    }


}
