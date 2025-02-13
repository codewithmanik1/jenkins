package com.wave.matrix.antmedia.antmediaServer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/streams")
public class AntMediaController {

    private final RestTemplate restTemplate;
    private final String AMS_BASE_URL;

    public AntMediaController(RestTemplate restTemplate, @Value("${antmedia.server.url}") String amsBaseUrl) {
        this.restTemplate = restTemplate;
        this.AMS_BASE_URL = amsBaseUrl;
    }

    @GetMapping()
    public ResponseEntity<String> getActiveStreams() {
        String url = AMS_BASE_URL + "/rest/v2/broadcasts/list/0/50";
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching active streams: " + e.getMessage());
        }
    }

    @PostMapping("/create")
    public ResponseEntity<String> createStream() {
        String url = AMS_BASE_URL + "/rest/v2/broadcasts/create";

        try {
            // Set Headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Set Body (empty JSON)
            String requestBody = "{}";
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

            // Send POST request with JSON
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error creating stream: " + e.getMessage());
        }
    }
}
