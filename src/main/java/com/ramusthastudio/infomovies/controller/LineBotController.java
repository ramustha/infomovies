package com.ramusthastudio.infomovies.controller;

import com.google.gson.Gson;
import com.linecorp.bot.client.LineSignatureValidator;
import com.linecorp.bot.model.profile.UserProfileResponse;
import com.linecorp.bot.model.response.BotApiResponse;
import com.ramusthastudio.infomovies.model.DiscoverMovies;
import com.ramusthastudio.infomovies.model.Events;
import com.ramusthastudio.infomovies.model.FindMovies;
import com.ramusthastudio.infomovies.model.Message;
import com.ramusthastudio.infomovies.model.Payload;
import com.ramusthastudio.infomovies.model.Postback;
import com.ramusthastudio.infomovies.model.ResultMovieDetail;
import com.ramusthastudio.infomovies.model.ResultMovies;
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

import static com.ramusthastudio.infomovies.model.FindMovies.newFindMovies;
import static com.ramusthastudio.infomovies.util.BotHelper.FOLLOW;
import static com.ramusthastudio.infomovies.util.BotHelper.KW_DETAIL;
import static com.ramusthastudio.infomovies.util.BotHelper.KW_DETAIL_OVERVIEW;
import static com.ramusthastudio.infomovies.util.BotHelper.KW_FIND;
import static com.ramusthastudio.infomovies.util.BotHelper.KW_NOW_PLAYING;
import static com.ramusthastudio.infomovies.util.BotHelper.KW_PANDUAN;
import static com.ramusthastudio.infomovies.util.BotHelper.KW_POPULAR;
import static com.ramusthastudio.infomovies.util.BotHelper.MESSAGE;
import static com.ramusthastudio.infomovies.util.BotHelper.MESSAGE_TEXT;
import static com.ramusthastudio.infomovies.util.BotHelper.POSTBACK;
import static com.ramusthastudio.infomovies.util.BotHelper.buttonMessage;
import static com.ramusthastudio.infomovies.util.BotHelper.carouselMessage;
import static com.ramusthastudio.infomovies.util.BotHelper.confirmMessage;
import static com.ramusthastudio.infomovies.util.BotHelper.createDetailOverview;
import static com.ramusthastudio.infomovies.util.BotHelper.getDetailMovie;
import static com.ramusthastudio.infomovies.util.BotHelper.getNowPlayingMovies;
import static com.ramusthastudio.infomovies.util.BotHelper.getPopularMovies;
import static com.ramusthastudio.infomovies.util.BotHelper.getSearchMovies;
import static com.ramusthastudio.infomovies.util.BotHelper.getUserProfile;
import static com.ramusthastudio.infomovies.util.BotHelper.greetingMessage;
import static com.ramusthastudio.infomovies.util.BotHelper.pushMessage;
import static com.ramusthastudio.infomovies.util.BotHelper.stickerMessage;
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
      Response<DiscoverMovies> discoverMovies;
      FindMovies findMovies;

      try {
        Response<UserProfileResponse> profileResp = getUserProfile(fChannelAccessToken, userId);
        UserProfileResponse profile = profileResp.body();
        LOG.info("profileResp code {} message {}", profileResp.code(), profileResp.message());
      } catch (IOException ignored) { }

      try {
        switch (eventType) {
          case FOLLOW:
            greetingMessage(fChannelAccessToken, userId);
            pushMessage(fChannelAccessToken, userId, "Popular movies..");

            findMovies = newFindMovies().withPage(1).withMax(0).withFlag(KW_POPULAR);
            LOG.info("findMovies findMovies {}", findMovies);

            discoverMovies = getPopularMovies(fBaseUrl, fApiKey, findMovies);
            LOG.info("Popular movies code {} message {}", discoverMovies.code(), discoverMovies.message());

            buildMessage(discoverMovies, userId, findMovies);
            break;
          case MESSAGE:
            if (message.type().equals(MESSAGE_TEXT)) {
              String text = message.text();
              if (text.toLowerCase().startsWith(KW_NOW_PLAYING.toLowerCase())) {
                String region = text.substring(KW_NOW_PLAYING.length(), text.length()).trim();
                findMovies = newFindMovies().withPage(1).withMax(0).withRegion(region).withFlag(KW_NOW_PLAYING);
                LOG.info("findMovies findMovies {}", findMovies);

                discoverMovies = getNowPlayingMovies(fBaseUrl, fApiKey, findMovies);
                LOG.info("NowPlayingMovies code {} message {}", discoverMovies.code(), discoverMovies.message());

                buildMessage(discoverMovies, userId, findMovies);
              } else if (text.toLowerCase().startsWith(KW_POPULAR.toLowerCase())) {
                String region = text.substring(KW_POPULAR.length(), text.length()).trim();
                findMovies = newFindMovies().withPage(1).withMax(0).withRegion(region).withFlag(KW_POPULAR);
                LOG.info("findMovies findMovies {}", findMovies);

                discoverMovies = getPopularMovies(fBaseUrl, fApiKey, findMovies);
                LOG.info("PopularMovies code {} message {}", discoverMovies.code(), discoverMovies.message());

                buildMessage(discoverMovies, userId, findMovies);
              } else if (text.toLowerCase().startsWith(KW_FIND.toLowerCase())) {
                String keyword = text.substring(KW_FIND.length(), text.length());
                String[] data = keyword.split(",");
                int year = 0;
                String region = "";
                if (data.length == 2 && data[1].length() > 1 && !data[1].isEmpty()) {
                  year = data[1].length() == 4 ? Integer.parseInt(data[1]) : 0;
                  region = data[1].length() == 2 ? data[1] : "";
                } else if (data.length == 3 && !data[1].isEmpty() && !data[2].isEmpty()) {
                  year = data[1].length() == 4 ? Integer.parseInt(data[1]) : 0;
                  region = data[1].length() == 2 ? data[1] : "";
                }
                findMovies = newFindMovies()
                    .withTitle(data[0]).withYear(year).withPage(1).withRegion(region).withFlag(KW_FIND);
                LOG.info("findMovies findMovies {}", findMovies);

                discoverMovies = getSearchMovies(fBaseUrl, fApiKey, findMovies);
                LOG.info("SearchMovies code {} message {}", discoverMovies.code(), discoverMovies.message());

                buildMessage(discoverMovies, userId, findMovies);
              } else if (text.toLowerCase().startsWith(KW_PANDUAN.toLowerCase())) {
                unrecognizedMessage(fChannelAccessToken, userId);
              } else { unrecognizedMessage(fChannelAccessToken, userId); }
            } else { unrecognizedMessage(fChannelAccessToken, userId); }
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
                Response<BotApiResponse> detail = buttonMessage(fChannelAccessToken, fBaseImdbUrl, fBaseImgUrl, userId, movie);
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
                Response<BotApiResponse> detail = pushMessage(fChannelAccessToken, userId, overview);
                LOG.info("Postback code {} message {}", detail.code(), detail.message());
              }

            } else if (text.toLowerCase().startsWith(KW_NOW_PLAYING.toLowerCase())) {
              String strPageMax = text.substring(KW_NOW_PLAYING.length(), text.length());
              String[] pageMax = strPageMax.split(",");

              int page = Integer.parseInt(pageMax[0].trim());
              int max = Integer.parseInt(pageMax[1].trim());
              int year = Integer.parseInt(pageMax[2].trim());
              if (pageMax.length == 4) {
                String region = pageMax[3].trim();
                findMovies = newFindMovies().withPage(page).withMax(max).withYear(year).withRegion(region).withFlag(KW_NOW_PLAYING);
              } else {
                findMovies = newFindMovies().withPage(page).withMax(max).withYear(year).withFlag(KW_NOW_PLAYING);
              }
              LOG.info("findMovies findMovies {}", findMovies);

              discoverMovies = getNowPlayingMovies(fBaseUrl, fApiKey, findMovies);
              LOG.info("Popular movies code {} message {}", discoverMovies.code(), discoverMovies.message());

              buildMessage(discoverMovies, userId, findMovies);

            } else if (text.toLowerCase().startsWith(KW_POPULAR.toLowerCase())) {
              String strPageMax = text.substring(KW_POPULAR.length(), text.length());
              String[] pageMax = strPageMax.split(",");

              int page = Integer.parseInt(pageMax[0].trim());
              int max = Integer.parseInt(pageMax[1].trim());
              int year = Integer.parseInt(pageMax[2].trim());
              if (pageMax.length == 4) {
                String region = pageMax[3].trim();
                findMovies = newFindMovies().withPage(page).withMax(max).withYear(year).withRegion(region).withFlag(KW_POPULAR);
              } else {
                findMovies = newFindMovies().withPage(page).withMax(max).withYear(year).withFlag(KW_POPULAR);
              }
              LOG.info("findMovies findMovies {}", findMovies);

              discoverMovies = getPopularMovies(fBaseUrl, fApiKey, findMovies);
              LOG.info("Popular movies code {} message {}", discoverMovies.code(), discoverMovies.message());

              buildMessage(discoverMovies, userId, findMovies);
            } else {
              stickerMessage(fChannelAccessToken, userId, "1", "407");
              unrecognizedMessage(fChannelAccessToken, userId);
            }
            break;
        }
      } catch (IOException ignored) {}

    }

    return new ResponseEntity<>(HttpStatus.OK);
  }

  private void buildMessage(Response<DiscoverMovies> aDiscoverMovies, String aUserId,
      FindMovies aFindMovies) throws IOException {
    if (aDiscoverMovies.isSuccessful()) {
      int size = aDiscoverMovies.body().getTotalResults();
      List<ResultMovies> movies = aDiscoverMovies.body().getResultMovies();
      carouselMessage(fChannelAccessToken, aUserId, fBaseImgUrl, movies, aFindMovies.getMax());

      LOG.info("buildMessage size {}", size, aFindMovies.getMax());
      if (size < aFindMovies.getMax() || !aFindMovies.getFlag().equalsIgnoreCase(KW_FIND)) {
        confirmMessage(fChannelAccessToken, aUserId, aFindMovies);
      }
    } else {
      stickerMessage(fChannelAccessToken, aUserId, "1", "407");
    }
  }

}
