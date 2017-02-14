package com.ramusthastudio.infomovies.controller;

import com.google.gson.Gson;
import com.linecorp.bot.client.LineSignatureValidator;
import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.action.URIAction;
import com.linecorp.bot.model.message.template.CarouselColumn;
import com.linecorp.bot.model.profile.UserProfileResponse;
import com.linecorp.bot.model.response.BotApiResponse;
import com.ramusthastudio.infomovies.model.DiscoverMovies;
import com.ramusthastudio.infomovies.model.Events;
import com.ramusthastudio.infomovies.model.Message;
import com.ramusthastudio.infomovies.model.Payload;
import com.ramusthastudio.infomovies.model.ResultMovies;
import com.ramusthastudio.infomovies.model.Source;
import com.ramusthastudio.infomovies.util.TheMovieDbService;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.ramusthastudio.infomovies.util.BotHelper.FOLLOW;
import static com.ramusthastudio.infomovies.util.BotHelper.KW_MOVIE_BULAN_INI;
import static com.ramusthastudio.infomovies.util.BotHelper.KW_SERIES_BULAN_INI;
import static com.ramusthastudio.infomovies.util.BotHelper.MESSAGE;
import static com.ramusthastudio.infomovies.util.BotHelper.MESSAGE_TEXT;
import static com.ramusthastudio.infomovies.util.BotHelper.createCarouselMessage;
import static com.ramusthastudio.infomovies.util.BotHelper.createGenres;
import static com.ramusthastudio.infomovies.util.BotHelper.createMessage;
import static com.ramusthastudio.infomovies.util.BotHelper.getUserProfile;
import static com.ramusthastudio.infomovies.util.BotHelper.greetingMessage;
import static com.ramusthastudio.infomovies.util.BotHelper.unrecognizedMessage;

@RestController
@RequestMapping(value = "/linebot")
public class LineBotController {
  private static final Logger LOG = LoggerFactory.getLogger(LineBotController.class);

  @Autowired
  @Qualifier("com.linecorp.channel_secret")
  String fChannelSecret;

  @Autowired
  @Qualifier("com.linecorp.channel_access_token")
  String fChannelAccessToken;

  @Autowired
  @Qualifier("com.themoviedb.api_key")
  String fApiKey;

  @Autowired
  @Qualifier("com.themoviedb.base_url")
  String fBaseUrl;

  @Autowired
  @Qualifier("com.themoviedb.base_img_url")
  String fBaseImgUrl;

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
        Response<UserProfileResponse> profileResp = getUserProfile(fChannelAccessToken, userId);
        UserProfileResponse profile = profileResp.body();
        LOG.info("profileResp code {} message {}", profileResp.code(), profileResp.message());
      } catch (IOException aE) {
        LOG.error("Failed GET UserProfileResponse: {} ", aE);
      }

      try {
        String userId = source.userId();
        if (eventType.equals(FOLLOW)) {
          greetingMessage(fChannelAccessToken, userId);
          createMessage(fChannelAccessToken, userId, "Daftar Movies release tahun ini");

          Retrofit retrofit = new Retrofit.Builder().baseUrl(fBaseUrl)
              .addConverterFactory(GsonConverterFactory.create()).build();
          TheMovieDbService service = retrofit.create(TheMovieDbService.class);

          LocalDate now = LocalDate.now();
          Response<DiscoverMovies> discoverMoviesResp = service.discoverMovies(fApiKey, now.getYear()).execute();
          // Response<DiscoverTvs> discoverTvsResp = service.discoverTvs(fApiKey, now.getYear()).execute();
          // DiscoverTvs discoverTvs = discoverTvsResp.body();

          LOG.info("DiscoverMovies code {} message {}", discoverMoviesResp.code(), discoverMoviesResp.message());
          if (discoverMoviesResp.isSuccessful()) {
            DiscoverMovies discoverMovies = discoverMoviesResp.body();

            List<CarouselColumn> carouselColumn = new ArrayList<>();
            for (ResultMovies resultMovies : discoverMovies.getDiscoverresults()) {

              String desc = "Rating : " + resultMovies.getVoteAverage() + "(" + resultMovies.getVoteCount() + ")\n";
              desc += "Genre : " + createGenres(resultMovies.getGenreIds());

              String overview = "Release date : " + resultMovies.getReleaseDate() + "\n\n";
              overview += resultMovies.getOverview();

              LOG.info("ResultMovies title {} poster {} genre {}",
                  resultMovies.getTitle(),
                  fBaseImgUrl + resultMovies.getPosterPath(),
                  createGenres(resultMovies.getGenreIds()));

              if (carouselColumn.size() < 5) {
                carouselColumn.add(
                    new CarouselColumn(
                        fBaseImgUrl + resultMovies.getPosterPath(),
                        resultMovies.getTitle(),
                        resultMovies.getOriginalTitle(),
                        Arrays.asList(
                            new MessageAction("Overview", resultMovies.getTitle()),
                            new URIAction("Poster", fBaseImgUrl + resultMovies.getPosterPath()),
                            new MessageAction("Detail", String.valueOf(resultMovies.getId())))));
              }
            }

            Response<BotApiResponse> carouselResult = createCarouselMessage(fChannelAccessToken, userId, carouselColumn);
            LOG.info("Carousel Message : code {} message {}", carouselResult.code(), carouselResult.message());
          }

        } else if (eventType.equals(MESSAGE)) {
          if (message.type().equals(MESSAGE_TEXT)) {
            String text = message.text();
            if (text.contains(KW_MOVIE_BULAN_INI)) {
              createMessage(fChannelAccessToken, userId, text);
            } else if (text.contains(KW_SERIES_BULAN_INI)) {
              createMessage(fChannelAccessToken, userId, text);
            } else {
              unrecognizedMessage(fChannelAccessToken, userId);
            }
          } else {
            unrecognizedMessage(fChannelAccessToken, userId);
          }
        }
      } catch (IOException aE) {
        LOG.error("Failed show greeting message: {} ", aE.fillInStackTrace());
      }

    }

    return new ResponseEntity<>(HttpStatus.OK);
  }

}
