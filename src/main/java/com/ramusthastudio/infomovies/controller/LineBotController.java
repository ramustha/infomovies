package com.ramusthastudio.infomovies.controller;

import com.google.gson.Gson;
import com.linecorp.bot.client.LineSignatureValidator;
import com.linecorp.bot.model.message.template.CarouselColumn;
import com.linecorp.bot.model.profile.UserProfileResponse;
import com.linecorp.bot.model.response.BotApiResponse;
import com.ramusthastudio.infomovies.model.DiscoverMovies;
import com.ramusthastudio.infomovies.model.Events;
import com.ramusthastudio.infomovies.model.Message;
import com.ramusthastudio.infomovies.model.Payload;
import com.ramusthastudio.infomovies.model.Postback;
import com.ramusthastudio.infomovies.model.ResultMovieDetail;
import com.ramusthastudio.infomovies.model.Source;
import java.io.IOException;
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

import static com.ramusthastudio.infomovies.util.BotHelper.FOLLOW;
import static com.ramusthastudio.infomovies.util.BotHelper.KW_DETAIL;
import static com.ramusthastudio.infomovies.util.BotHelper.KW_DETAIL_OVERVIEW;
import static com.ramusthastudio.infomovies.util.BotHelper.KW_NEXT_POPULAR;
import static com.ramusthastudio.infomovies.util.BotHelper.KW_NOW_PLAYING;
import static com.ramusthastudio.infomovies.util.BotHelper.KW_PANDUAN;
import static com.ramusthastudio.infomovies.util.BotHelper.KW_SEARCH;
import static com.ramusthastudio.infomovies.util.BotHelper.MESSAGE;
import static com.ramusthastudio.infomovies.util.BotHelper.MESSAGE_TEXT;
import static com.ramusthastudio.infomovies.util.BotHelper.POSTBACK;
import static com.ramusthastudio.infomovies.util.BotHelper.buildButtonDetailMovie;
import static com.ramusthastudio.infomovies.util.BotHelper.buildCarouselResultMovies;
import static com.ramusthastudio.infomovies.util.BotHelper.createCarouselMessage;
import static com.ramusthastudio.infomovies.util.BotHelper.createConfirmMessage;
import static com.ramusthastudio.infomovies.util.BotHelper.createDetailOverview;
import static com.ramusthastudio.infomovies.util.BotHelper.createMessage;
import static com.ramusthastudio.infomovies.util.BotHelper.createSticker;
import static com.ramusthastudio.infomovies.util.BotHelper.getDetailMovie;
import static com.ramusthastudio.infomovies.util.BotHelper.getNowPlayingMovies;
import static com.ramusthastudio.infomovies.util.BotHelper.getPopularMovies;
import static com.ramusthastudio.infomovies.util.BotHelper.getSearchMovies;
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
  @Qualifier("com.themoviedb.base_imdb_url")
  String fBaseImdbUrl;

  @Autowired
  @Qualifier("com.themoviedb.base_video_url")
  String fBaseVideoUrl;

  @Autowired
  @Qualifier("com.themoviedb.base_img_url")
  String fBaseImgUrl;

  @RequestMapping(value = "/callback", method = RequestMethod.POST)
  public ResponseEntity<String> callback(
      @RequestHeader("X-Line-Signature") String aXLineSignature,
      @RequestBody String aPayload) {

    LOG.info("XLineSignature: {} ", aXLineSignature);
    LOG.info("Payload: {} ", aPayload);

    LOG.info("The Signature is: {} ", (aXLineSignature != null && aXLineSignature.length() > 0) ? aXLineSignature : "N/A");
    final boolean valid = new LineSignatureValidator(fChannelSecret.getBytes()).validateSignature(aPayload.getBytes(), aXLineSignature);
    LOG.info("The Signature is: {} ", valid ? "valid" : "tidak valid");

    if (aPayload != null && aPayload.length() > 0) {
      Gson gson = new Gson();
      Payload payload = gson.fromJson(aPayload, Payload.class);

      Events event = payload.events()[0];

      String eventType = event.type();
      String replayToken = event.replyToken();
      Source source = event.source();
      long timestamp = event.timestamp();
      Message message = event.message();
      Postback postback = event.postback();

      String userId = source.userId();
      try {
        Response<UserProfileResponse> profileResp = getUserProfile(fChannelAccessToken, userId);
        UserProfileResponse profile = profileResp.body();
        LOG.info("profileResp code {} message {}", profileResp.code(), profileResp.message());
      } catch (IOException ignored) { }

      try {
        switch (eventType) {
          case FOLLOW:
            createSticker(fChannelAccessToken, userId, "1", "125");
            greetingMessage(fChannelAccessToken, userId);
            createMessage(fChannelAccessToken, userId, "Popular movies..");

            Response<DiscoverMovies> discoverMoviesResp = getPopularMovies(fBaseUrl, fApiKey, 1);
            LOG.info("Popular movies code {} message {}", discoverMoviesResp.code(), discoverMoviesResp.message());

            if (discoverMoviesResp.isSuccessful()) {
              DiscoverMovies discoverMovies = discoverMoviesResp.body();
              List<CarouselColumn> carouselColumn = buildCarouselResultMovies(fBaseImgUrl, discoverMovies.getResultMovies(), 0);
              createCarouselMessage(fChannelAccessToken, userId, carouselColumn);
            }

            createConfirmMessage(fChannelAccessToken, userId, "Lihat Popular movie lainnya ?", 1, 0);

            break;
          case MESSAGE:
            if (message.type().equals(MESSAGE_TEXT)) {
              String text = message.text();
              if (text.toLowerCase().contains(KW_NOW_PLAYING.toLowerCase())) {

                discoverMoviesResp = getNowPlayingMovies(fBaseUrl, fApiKey, 1);
                LOG.info("Now Playing code {} message {}", discoverMoviesResp.code(), discoverMoviesResp.message());

                if (discoverMoviesResp.isSuccessful()) {
                  DiscoverMovies discoverMovies = discoverMoviesResp.body();
                  List<CarouselColumn> carouselColumn = buildCarouselResultMovies(fBaseImgUrl, discoverMovies.getResultMovies(), 0);
                  createCarouselMessage(fChannelAccessToken, userId, carouselColumn);
                }

                createConfirmMessage(fChannelAccessToken, userId, "Lihat Now Playing movie lainnya ?", 1, 0);

              } else if (text.toLowerCase().startsWith(KW_SEARCH.toLowerCase())) {
                String keyword = text.substring(KW_SEARCH.length(), text.length());
                String title;
                int year = 0;
                if (keyword.contains(",")) {
                  String[] keySplit = keyword.split(",");
                  title = keySplit[0];
                  year = Integer.parseInt(keySplit[1].trim());
                } else {
                  title = keyword;
                }

                LOG.info("Keyword title {} year {}", title, year);

                Response<DiscoverMovies> searchMovies = getSearchMovies(fBaseUrl, fApiKey, title, year);
                LOG.info("SearchMovies code {} message {}", searchMovies.code(), searchMovies.message());

                if (searchMovies.isSuccessful()) {
                  DiscoverMovies discoverMovies = searchMovies.body();
                  List<CarouselColumn> carouselColumn = buildCarouselResultMovies(fBaseImgUrl, discoverMovies.getResultMovies(), 0);
                  createCarouselMessage(fChannelAccessToken, userId, carouselColumn);
                }

                createConfirmMessage(fChannelAccessToken, userId, "Lihat movie lainnya ?", 1, 0);

              } else if (text.toLowerCase().contains(KW_PANDUAN.toLowerCase())) {
                unrecognizedMessage(fChannelAccessToken, userId);
              } else {
                unrecognizedMessage(fChannelAccessToken, userId);
              }
            } else {
              unrecognizedMessage(fChannelAccessToken, userId);
            }
            break;
          case POSTBACK:
            String text = postback.data();
            if (text.toLowerCase().startsWith(KW_DETAIL.toLowerCase())) {
              String strId = text.substring(KW_DETAIL.length(), text.length());
              LOG.info("Movie id {}", strId.trim());
              int id = Integer.parseInt(strId.trim());
              Response<ResultMovieDetail> detailMovieResp = getDetailMovie(fBaseUrl, id, fApiKey);
              LOG.info("Message code {} message {}", detailMovieResp.code(), detailMovieResp.message());

              if (detailMovieResp.isSuccessful()) {
                ResultMovieDetail movie = detailMovieResp.body();
                Response<BotApiResponse> detail = buildButtonDetailMovie(fChannelAccessToken, fBaseImdbUrl, fBaseImgUrl, userId, movie);
                LOG.info("Message code {} message {}", detail.code(), detail.message());
              }

            } else if (text.toLowerCase().startsWith(KW_DETAIL_OVERVIEW.toLowerCase())) {
              String strId = text.substring(KW_DETAIL_OVERVIEW.length(), text.length());
              LOG.info("Movie id {}", strId.trim());
              int id = Integer.parseInt(strId.trim());
              Response<ResultMovieDetail> detailMovieResp = getDetailMovie(fBaseUrl, id, fApiKey);
              LOG.info("Postback code {} message {}", detailMovieResp.code(), detailMovieResp.message());

              if (detailMovieResp.isSuccessful()) {
                ResultMovieDetail movie = detailMovieResp.body();
                String overview = createDetailOverview(movie, fBaseImdbUrl);
                Response<BotApiResponse> detail = createMessage(fChannelAccessToken, userId, overview);
                LOG.info("Postback code {} message {}", detail.code(), detail.message());
              }

            } else if (text.toLowerCase().startsWith(KW_NEXT_POPULAR.toLowerCase())) {
              String strPageMax = text.substring(KW_NEXT_POPULAR.length(), text.length());
              String[] pageMax = strPageMax.split(",");

              int page = Integer.parseInt(pageMax[0].trim());
              int max = Integer.parseInt(pageMax[1].trim());

              LOG.info("paging page {} max {}", page, max);

              discoverMoviesResp = getPopularMovies(fBaseUrl, fApiKey, page);
              LOG.info("Popular movies code {} message {}", discoverMoviesResp.code(), discoverMoviesResp.message());

              if (discoverMoviesResp.isSuccessful()) {
                DiscoverMovies discoverMovies = discoverMoviesResp.body();
                List<CarouselColumn> carouselColumn = buildCarouselResultMovies(fBaseImgUrl, discoverMovies.getResultMovies(), max);
                createCarouselMessage(fChannelAccessToken, userId, carouselColumn);
              }

              createConfirmMessage(fChannelAccessToken, userId, "Lihat Popular movie lainnya ?", page, max);
            } else {
              createSticker(fChannelAccessToken, userId, "1", "407");
              unrecognizedMessage(fChannelAccessToken, userId);
            }
            break;
        }
      } catch (IOException ignored) {}

    }

    return new ResponseEntity<>(HttpStatus.OK);
  }

}
