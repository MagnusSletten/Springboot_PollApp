package com.example.backend.Controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PollControllerTest {

    String pollJSONSTRING = """
    {
      "title": "Pizza preference",
      "question": "Do you hate pizza?",
      "voteOptions": [
        { "caption": "yes" },
        { "caption": "no" }
      ],
    }
    """;
   

    @LocalServerPort int port;
    RestClient client;

    @BeforeEach
    void setUp() {
        client = RestClient.builder()
                .baseUrl("http://localhost:" + port)
                .build();
    }

    @Test
    void createPollTest() throws JSONException{
        var resp = createPoll(getExpectedPoll_1());
        assertTrue(resp.getBody().contains("Succesfully")); 
    }

    @Test
    void getPollTest() throws JSONException {
        createPoll(getExpectedPoll_1());
        String pollReturn = client.get().uri("/polls/1").retrieve() 
	    .body(String.class);
        JSONObject pollReturnJSon = new JSONObject(pollReturn);
        assertTrue(pollReturnJSon.has("pollID"));
        JSONObject pollReturnSimple = removeDynamicFields(pollReturnJSon);
        JSONAssert.assertEquals(getExpectedPoll_1(), pollReturnSimple,true);  
          
    }

  
    ResponseEntity<String> createPoll(JSONObject poll) throws JSONException {
        var resp = client.post()
        .uri("/polls")   
        .contentType(APPLICATION_JSON)
        .body(poll.toString())
        .retrieve()
        .toEntity(String.class);
        return resp;
    
        }
    JSONObject getExpectedPoll_1() throws JSONException{
        JSONObject pollJSON = new JSONObject();

        pollJSON.put("title", "Pizza preference");
        pollJSON.put("question", "Do you hate pizza?");

        JSONArray voteOptions = new JSONArray();
        voteOptions.put(new JSONObject().put("caption", "yes"));
        voteOptions.put(new JSONObject().put("caption", "no"));
        pollJSON.put("voteOptions", voteOptions);
        pollJSON.put("publishedAt",JSONObject.NULL);
        pollJSON.put("validUntil",JSONObject.NULL);

        return pollJSON;
    }

    JSONObject removeDynamicFields(JSONObject poll){
        poll.remove("pollID");
      //  poll.remove("publishedAt");
      //  poll.remove("validUntil");
        return poll; 
    }


}
