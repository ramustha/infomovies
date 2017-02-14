package com.ramusthastudio.infomovies.controller;

import com.google.gson.Gson;
import com.linecorp.bot.client.LineSignatureValidator;
import com.linecorp.bot.model.profile.UserProfileResponse;
import com.linecorp.bot.model.response.BotApiResponse;
import com.ramusthastudio.infomovies.model.Events;
import com.ramusthastudio.infomovies.model.Message;
import com.ramusthastudio.infomovies.model.Payload;
import com.ramusthastudio.infomovies.model.Source;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static com.ramusthastudio.infomovies.util.BotHelper.getUserProfile;
import static com.ramusthastudio.infomovies.util.BotHelper.pushMessage;

@RestController
@RequestMapping(value = "/linebot")
public class LineBotController {
  private Logger LOG = LoggerFactory.getLogger(LineBotController.class);

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

    LOG.info("The Signature is: {} ", (aXLineSignature != null && aXLineSignature.length() > 0) ? aXLineSignature : "N/A");
    final boolean valid = new LineSignatureValidator(fChannelSecret.getBytes()).validateSignature(aPayload.getBytes(), aXLineSignature);
    LOG.info("The Signature is: {} ", valid ? "valid" : "tidak valid");

    Gson gson = new Gson();

    if (aPayload != null && aPayload.length() > 0) {
      Payload payload = gson.fromJson(aPayload, Payload.class);
      LOG.info("payload: {} ", payload);

      Events event = payload.events()[0];

      String eventType = event.type();
      String replayToken = event.replyToken();
      Source source = event.source();
      long timestamp = event.timestamp();
      Message message = event.message();

      try {
        String userId = source.userId();
        UserProfileResponse userProfile = getUserProfile(fChannelAccessToken, userId);
        LOG.info("UserProfileResponse: {} ", userProfile);

        BotApiResponse pushResult = pushMessage(fChannelAccessToken, userId, "Hi " + userProfile.getDisplayName());
        LOG.info("BotApiResponse: Message {} Details {} ", pushResult.getMessage(), pushResult.getDetails());
      } catch (IOException aE) {
        LOG.error("Failed GET UserProfileResponse: {} ", aE);
      }
    }

    return new ResponseEntity<>(HttpStatus.OK);
  }

}
