package com.ramusthastudio.infomovies.controller;

import com.google.gson.Gson;
import com.linecorp.bot.client.LineSignatureValidator;
import com.ramusthastudio.infomovies.model.Payload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/linebot")
public class LineBotController {
  @Autowired
  @Qualifier("com.linecorp.channel_secret")
  String fChannelSecret;

  @Autowired
  @Qualifier("com.linecorp.channel_access_token")
  String fChannelAccessToken;

  @RequestMapping(value = "/callback", method = RequestMethod.POST)
  public ResponseEntity<String> callback(
      @RequestHeader("X-Line-Signature") String aXLineSignature,
      @RequestBody String aPayload) {

    System.out.printf("The Signature is: %s", (aXLineSignature != null && aXLineSignature.length() > 0) ? aXLineSignature : "N/A");
    final boolean valid = new LineSignatureValidator(fChannelSecret.getBytes()).validateSignature(aPayload.getBytes(), aXLineSignature);
    System.out.println("The signature is: " + (valid ? "valid" : "tidak valid"));

    Gson gson = new Gson();

    if (aPayload != null && aPayload.length() > 0) {
      Payload payload = gson.fromJson(aPayload, Payload.class);
      System.out.println("payload: " + payload);
    }

    return new ResponseEntity<>(HttpStatus.OK);
  }

}
