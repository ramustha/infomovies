package com.ramusthastudio.infomovies.controller;

import com.google.gson.Gson;
import com.linecorp.bot.client.LineSignatureValidator;
import com.linecorp.bot.model.response.BotApiResponse;
import com.ramusthastudio.infomovies.model.DiscoverMovies;
import com.ramusthastudio.infomovies.model.DiscoverTvs;
import com.ramusthastudio.infomovies.model.Events;
import com.ramusthastudio.infomovies.model.FindMovies;
import com.ramusthastudio.infomovies.model.Message;
import com.ramusthastudio.infomovies.model.Payload;
import com.ramusthastudio.infomovies.model.Postback;
import com.ramusthastudio.infomovies.model.ResultMovieDetail;
import com.ramusthastudio.infomovies.model.ResultMovies;
import com.ramusthastudio.infomovies.model.ResultTvs;
import com.ramusthastudio.infomovies.model.ResultTvsDetail;
import com.ramusthastudio.infomovies.model.ResultTvsVideo;
import com.ramusthastudio.infomovies.model.ResultVideo;
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
import static com.ramusthastudio.infomovies.util.BotHelper.IMG_HOLDER;
import static com.ramusthastudio.infomovies.util.BotHelper.KW_DETAIL;
import static com.ramusthastudio.infomovies.util.BotHelper.KW_DETAIL_OVERVIEW;
import static com.ramusthastudio.infomovies.util.BotHelper.KW_FIND;
import static com.ramusthastudio.infomovies.util.BotHelper.KW_NOW_PLAYING;
import static com.ramusthastudio.infomovies.util.BotHelper.KW_PANDUAN;
import static com.ramusthastudio.infomovies.util.BotHelper.KW_POPULAR;
import static com.ramusthastudio.infomovies.util.BotHelper.KW_TOP_RATED;
import static com.ramusthastudio.infomovies.util.BotHelper.KW_TV_AIRING_TODAY;
import static com.ramusthastudio.infomovies.util.BotHelper.KW_TV_DETAIL;
import static com.ramusthastudio.infomovies.util.BotHelper.KW_TV_DETAIL_OVERVIEW;
import static com.ramusthastudio.infomovies.util.BotHelper.KW_TV_DETAIL_TRAILER_OVERVIEW;
import static com.ramusthastudio.infomovies.util.BotHelper.KW_TV_FIND;
import static com.ramusthastudio.infomovies.util.BotHelper.KW_TV_ON_AIR;
import static com.ramusthastudio.infomovies.util.BotHelper.KW_TV_POPULAR;
import static com.ramusthastudio.infomovies.util.BotHelper.KW_TV_TOP_RATED;
import static com.ramusthastudio.infomovies.util.BotHelper.KW_UPCOMING;
import static com.ramusthastudio.infomovies.util.BotHelper.MESSAGE;
import static com.ramusthastudio.infomovies.util.BotHelper.MESSAGE_TEXT;
import static com.ramusthastudio.infomovies.util.BotHelper.POSTBACK;
import static com.ramusthastudio.infomovies.util.BotHelper.SOURCE_GROUP;
import static com.ramusthastudio.infomovies.util.BotHelper.SOURCE_ROOM;
import static com.ramusthastudio.infomovies.util.BotHelper.SOURCE_USER;
import static com.ramusthastudio.infomovies.util.BotHelper.buttonMessage;
import static com.ramusthastudio.infomovies.util.BotHelper.buttonMessageTv;
import static com.ramusthastudio.infomovies.util.BotHelper.carouselMessage;
import static com.ramusthastudio.infomovies.util.BotHelper.carouselMessageTv;
import static com.ramusthastudio.infomovies.util.BotHelper.confirmMessage;
import static com.ramusthastudio.infomovies.util.BotHelper.createDetailOverview;
import static com.ramusthastudio.infomovies.util.BotHelper.createDetailOverviewTv;
import static com.ramusthastudio.infomovies.util.BotHelper.getAiringTodayTvs;
import static com.ramusthastudio.infomovies.util.BotHelper.getDetailMovie;
import static com.ramusthastudio.infomovies.util.BotHelper.getDetailTvs;
import static com.ramusthastudio.infomovies.util.BotHelper.getDetailTvsVideo;
import static com.ramusthastudio.infomovies.util.BotHelper.getNowPlayingMovies;
import static com.ramusthastudio.infomovies.util.BotHelper.getOnAirTvs;
import static com.ramusthastudio.infomovies.util.BotHelper.getPopularMovies;
import static com.ramusthastudio.infomovies.util.BotHelper.getPopularTvs;
import static com.ramusthastudio.infomovies.util.BotHelper.getSearchMovies;
import static com.ramusthastudio.infomovies.util.BotHelper.getSearchTvs;
import static com.ramusthastudio.infomovies.util.BotHelper.getTopRatedMovies;
import static com.ramusthastudio.infomovies.util.BotHelper.getTopRatedTvs;
import static com.ramusthastudio.infomovies.util.BotHelper.getUpcomingMoviesMovies;
import static com.ramusthastudio.infomovies.util.BotHelper.greetingMessage;
import static com.ramusthastudio.infomovies.util.BotHelper.pushMessage;
import static com.ramusthastudio.infomovies.util.BotHelper.stickerMessage;
import static com.ramusthastudio.infomovies.util.BotHelper.unrecognizedMessage;
import static com.ramusthastudio.infomovies.util.BotHelper.videoMessage;

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
  @Qualifier("getApiKey")
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
      String sourceType = source.type();

      // try {
      //   Response<UserProfileResponse> profileResp = getUserProfile(fChannelAccessToken, userId);
      //   UserProfileResponse profile = profileResp.body();
      //   LOG.info("profileResp code {} message {}", profileResp.code(), profileResp.message());
      // } catch (IOException ignored) { }

      switch (sourceType) {
        case SOURCE_USER:
          sourceUserProccess(eventType, replayToken, timestamp, message, postback, userId);
          break;
        case SOURCE_GROUP:
          // sourceGroupProccess(eventType, replayToken, postback, message, source);
          break;
        case SOURCE_ROOM:
          // sourceGroupProccess(eventType, replayToken, postback, message, source);
          break;
      }

    }

    return new ResponseEntity<>(HttpStatus.OK);
  }
  private void sourceUserProccess(String aEventType, String aReplayToken, long aTimestamp, Message aMessage, Postback aPostback, String aUserId) {
    Response<DiscoverMovies> discoverMovies;
    Response<DiscoverTvs> discoverTvs;
    FindMovies findMovies;
    try {
      switch (aEventType) {
        case FOLLOW:
          greetingMessage(fChannelAccessToken, aUserId);
          pushMessage(fChannelAccessToken, aUserId, "Popular movies..");

          findMovies = newFindMovies().withPage(1).withMax(0).withFlag(KW_POPULAR);
          LOG.info("findMovies findMovies {}", findMovies);

          discoverMovies = getPopularMovies(fBaseUrl, fApiKey, findMovies);
          LOG.info("Popular movies code {} message {}", discoverMovies.code(), discoverMovies.message());

          buildMessage(discoverMovies, aUserId, findMovies);
          break;
        case MESSAGE:
          if (aMessage.type().equals(MESSAGE_TEXT)) {
            String text = aMessage.text();
            if (text.toLowerCase().startsWith(KW_NOW_PLAYING.toLowerCase())) {
              String region = text.substring(KW_NOW_PLAYING.length(), text.length()).trim();
              findMovies = newFindMovies().withPage(1).withMax(0).withRegion(region).withFlag(KW_NOW_PLAYING);
              LOG.info("findMovies findMovies {}", findMovies);

              discoverMovies = getNowPlayingMovies(fBaseUrl, fApiKey, findMovies);
              LOG.info("NowPlayingMovies code {} message {}", discoverMovies.code(), discoverMovies.message());

              buildMessage(discoverMovies, aUserId, findMovies);
            } else if (text.toLowerCase().startsWith(KW_POPULAR.toLowerCase())) {
              String region = text.substring(KW_POPULAR.length(), text.length()).trim();
              findMovies = newFindMovies().withPage(1).withMax(0).withRegion(region).withFlag(KW_POPULAR);
              LOG.info("findMovies findMovies {}", findMovies);

              discoverMovies = getPopularMovies(fBaseUrl, fApiKey, findMovies);
              LOG.info("PopularMovies code {} message {}", discoverMovies.code(), discoverMovies.message());

              buildMessage(discoverMovies, aUserId, findMovies);
            } else if (text.toLowerCase().startsWith(KW_TV_POPULAR.toLowerCase())) {
              String region = text.substring(KW_TV_POPULAR.length(), text.length()).trim();
              findMovies = newFindMovies().withPage(1).withMax(0).withRegion(region).withFlag(KW_TV_POPULAR);
              LOG.info("findTv findTvs {}", findMovies);

              discoverTvs = getPopularTvs(fBaseUrl, fApiKey, findMovies);
              LOG.info("PopularTvs code {} message {}", discoverTvs.code(), discoverTvs.message());

              buildMessageTvs(discoverTvs, aUserId, findMovies);
            } else if (text.toLowerCase().startsWith(KW_UPCOMING.toLowerCase())) {
              String region = text.substring(KW_UPCOMING.length(), text.length()).trim();
              findMovies = newFindMovies().withPage(1).withMax(0).withRegion(region).withFlag(KW_UPCOMING);
              LOG.info("findMovies findMovies {}", findMovies);

              discoverMovies = getUpcomingMoviesMovies(fBaseUrl, fApiKey, findMovies);
              LOG.info("ComingSoonMovies code {} message {}", discoverMovies.code(), discoverMovies.message());

              buildMessage(discoverMovies, aUserId, findMovies);
            } else if (text.toLowerCase().startsWith(KW_TOP_RATED.toLowerCase())) {
              String region = text.substring(KW_TOP_RATED.length(), text.length()).trim();
              findMovies = newFindMovies().withPage(1).withMax(0).withRegion(region).withFlag(KW_TOP_RATED);
              LOG.info("findMovies findMovies {}", findMovies);

              discoverMovies = getTopRatedMovies(fBaseUrl, fApiKey, findMovies);
              LOG.info("TopRatedMovies code {} message {}", discoverMovies.code(), discoverMovies.message());

              buildMessage(discoverMovies, aUserId, findMovies);
            } else if (text.toLowerCase().startsWith(KW_TV_TOP_RATED.toLowerCase())) {
              String region = text.substring(KW_TV_TOP_RATED.length(), text.length()).trim();
              findMovies = newFindMovies().withPage(1).withMax(0).withRegion(region).withFlag(KW_TV_TOP_RATED);
              LOG.info("findTvs findTvs {}", findMovies);

              discoverTvs = getTopRatedTvs(fBaseUrl, fApiKey, findMovies);
              LOG.info("TopRatedTvs code {} message {}", discoverTvs.code(), discoverTvs.message());

              buildMessageTvs(discoverTvs, aUserId, findMovies);
            } else if (text.toLowerCase().startsWith(KW_TV_AIRING_TODAY.toLowerCase())) {
              String region = text.substring(KW_TV_AIRING_TODAY.length(), text.length()).trim();
              findMovies = newFindMovies().withPage(1).withMax(0).withRegion(region).withFlag(KW_TV_AIRING_TODAY);
              LOG.info("findTvs findTvs {}", findMovies);

              discoverTvs = getAiringTodayTvs(fBaseUrl, fApiKey, findMovies);
              LOG.info("AiringTodayTvs code {} message {}", discoverTvs.code(), discoverTvs.message());

              buildMessageTvs(discoverTvs, aUserId, findMovies);
            } else if (text.toLowerCase().startsWith(KW_TV_ON_AIR.toLowerCase())) {
              String region = text.substring(KW_TV_ON_AIR.length(), text.length()).trim();
              findMovies = newFindMovies().withPage(1).withMax(0).withRegion(region).withFlag(KW_TV_ON_AIR);
              LOG.info("findTvs findTvs {}", findMovies);

              discoverTvs = getOnAirTvs(fBaseUrl, fApiKey, findMovies);
              LOG.info("OnAirTvs code {} message {}", discoverTvs.code(), discoverTvs.message());

              buildMessageTvs(discoverTvs, aUserId, findMovies);
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

              buildMessage(discoverMovies, aUserId, findMovies);
            } else if (text.toLowerCase().startsWith(KW_TV_FIND.toLowerCase())) {
              String keyword = text.substring(KW_TV_FIND.length(), text.length());
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
                  .withTitle(data[0]).withYear(year).withPage(1).withFlag(KW_FIND);
              LOG.info("findTvs findTv {}", findMovies);

              discoverTvs = getSearchTvs(fBaseUrl, fApiKey, findMovies);
              LOG.info("SearchTvs code {} message {}", discoverTvs.code(), discoverTvs.message());

              buildMessageTvs(discoverTvs, aUserId, findMovies);
            } else if (text.toLowerCase().startsWith(KW_PANDUAN.toLowerCase())) {
              unrecognizedMessage(fChannelAccessToken, aUserId);
            } else { unrecognizedMessage(fChannelAccessToken, aUserId); }
          }
          break;
        case POSTBACK:
          String text = aPostback.data();
          if (text.toLowerCase().startsWith(KW_DETAIL.toLowerCase())) {
            String strId = text.substring(KW_DETAIL.length(), text.length());
            LOG.info("Movie id {}", strId.trim());
            int id = Integer.parseInt(strId.trim());
            Response<ResultMovieDetail> detailMovieResp = getDetailMovie(fBaseUrl, id, fApiKey);
            LOG.info("Message code {} message {}", detailMovieResp.code(), detailMovieResp.message());

            if (detailMovieResp.isSuccessful()) {
              ResultMovieDetail movie = detailMovieResp.body();
              Response<BotApiResponse> detail = buttonMessage(fChannelAccessToken, fBaseImdbUrl, fBaseImgUrl, aUserId, movie);
              LOG.info("Message code {} message {}", detail.code(), detail.message());
            }

          } else if (text.toLowerCase().startsWith(KW_TV_DETAIL.toLowerCase())) {
            String strId = text.substring(KW_TV_DETAIL.length(), text.length());
            LOG.info("Tv id {}", strId.trim());
            int id = Integer.parseInt(strId.trim());
            Response<ResultTvsDetail> detailTvResp = getDetailTvs(fBaseUrl, id, fApiKey);
            LOG.info("Message code {} message {}", detailTvResp.code(), detailTvResp.message());

            if (detailTvResp.isSuccessful()) {
              ResultTvsDetail tv = detailTvResp.body();
              Response<BotApiResponse> detail = buttonMessageTv(fChannelAccessToken, fBaseImdbUrl, fBaseImgUrl, aUserId, tv);
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
              Response<BotApiResponse> detail = pushMessage(fChannelAccessToken, aUserId, overview);
              LOG.info("Postback code {} message {}", detail.code(), detail.message());
            }

          } else if (text.toLowerCase().startsWith(KW_TV_DETAIL_OVERVIEW.toLowerCase())) {
            String strId = text.substring(KW_TV_DETAIL_OVERVIEW.length(), text.length());
            LOG.info("Tv id {}", strId.trim());
            int id = Integer.parseInt(strId.trim());
            Response<ResultTvsDetail> detailTvResp = getDetailTvs(fBaseUrl, id, fApiKey);
            LOG.info("Postback code {} message {}", detailTvResp.code(), detailTvResp.message());

            if (detailTvResp.isSuccessful()) {
              ResultTvsDetail tv = detailTvResp.body();
              String overview = createDetailOverviewTv(tv);
              Response<BotApiResponse> detail = pushMessage(fChannelAccessToken, aUserId, overview);
              LOG.info("Postback code {} message {}", detail.code(), detail.message());
            }

          } else if (text.toLowerCase().startsWith(KW_TV_DETAIL_TRAILER_OVERVIEW.toLowerCase())) {
            String strId = text.substring(KW_TV_DETAIL_TRAILER_OVERVIEW.length(), text.length());
            LOG.info("Tv id {}", strId.trim());
            int id = Integer.parseInt(strId.trim());
            Response<ResultTvsVideo> detailTvVideoResp = getDetailTvsVideo(fBaseUrl, id, fApiKey);
            LOG.info("Postback code {} message {}", detailTvVideoResp.code(), detailTvVideoResp.message());

            if (detailTvVideoResp.isSuccessful()) {
              ResultTvsVideo tvVideo = detailTvVideoResp.body();
              int videoId = tvVideo.getId();
              List<ResultVideo> resultVideo = tvVideo.getResultVideo();
              for (ResultVideo video : resultVideo) {
                String videoUrl = fBaseVideoUrl + video.getKey();
                pushMessage(fChannelAccessToken, aUserId, video.getName());
                Response<BotApiResponse> detail = videoMessage(fChannelAccessToken, aUserId, IMG_HOLDER, videoUrl);
                LOG.info("Postback code {} message {} {}", detail.code(), detail.message(), videoUrl);
              }
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

            buildMessage(discoverMovies, aUserId, findMovies);

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

            buildMessage(discoverMovies, aUserId, findMovies);
          } else if (text.toLowerCase().startsWith(KW_TV_POPULAR.toLowerCase())) {
            String strPageMax = text.substring(KW_TV_POPULAR.length(), text.length());
            String[] pageMax = strPageMax.split(",");

            int page = Integer.parseInt(pageMax[0].trim());
            int max = Integer.parseInt(pageMax[1].trim());
            int year = Integer.parseInt(pageMax[2].trim());
            if (pageMax.length == 4) {
              String region = pageMax[3].trim();
              findMovies = newFindMovies().withPage(page).withMax(max).withYear(year).withRegion(region).withFlag(KW_TV_POPULAR);
            } else {
              findMovies = newFindMovies().withPage(page).withMax(max).withYear(year).withFlag(KW_TV_POPULAR);
            }
            LOG.info("findTvs findTvs {}", findMovies);

            discoverTvs = getPopularTvs(fBaseUrl, fApiKey, findMovies);
            LOG.info("Popular tv code {} message {}", discoverTvs.code(), discoverTvs.message());

            buildMessageTvs(discoverTvs, aUserId, findMovies);
          } else if (text.toLowerCase().startsWith(KW_UPCOMING.toLowerCase())) {
            String strPageMax = text.substring(KW_UPCOMING.length(), text.length());
            String[] pageMax = strPageMax.split(",");

            int page = Integer.parseInt(pageMax[0].trim());
            int max = Integer.parseInt(pageMax[1].trim());
            int year = Integer.parseInt(pageMax[2].trim());
            if (pageMax.length == 4) {
              String region = pageMax[3].trim();
              findMovies = newFindMovies().withPage(page).withMax(max).withYear(year).withRegion(region).withFlag(KW_UPCOMING);
            } else {
              findMovies = newFindMovies().withPage(page).withMax(max).withYear(year).withFlag(KW_UPCOMING);
            }
            LOG.info("findMovies findMovies {}", findMovies);

            discoverMovies = getUpcomingMoviesMovies(fBaseUrl, fApiKey, findMovies);
            LOG.info("Coming soon movies code {} message {}", discoverMovies.code(), discoverMovies.message());

            buildMessage(discoverMovies, aUserId, findMovies);
          } else if (text.toLowerCase().startsWith(KW_TOP_RATED.toLowerCase())) {
            String strPageMax = text.substring(KW_TOP_RATED.length(), text.length());
            String[] pageMax = strPageMax.split(",");

            int page = Integer.parseInt(pageMax[0].trim());
            int max = Integer.parseInt(pageMax[1].trim());
            int year = Integer.parseInt(pageMax[2].trim());
            if (pageMax.length == 4) {
              String region = pageMax[3].trim();
              findMovies = newFindMovies().withPage(page).withMax(max).withYear(year).withRegion(region).withFlag(KW_TOP_RATED);
            } else {
              findMovies = newFindMovies().withPage(page).withMax(max).withYear(year).withFlag(KW_TOP_RATED);
            }
            LOG.info("findMovies findMovies {}", findMovies);

            discoverMovies = getTopRatedMovies(fBaseUrl, fApiKey, findMovies);
            LOG.info("Top Rated movies code {} message {}", discoverMovies.code(), discoverMovies.message());

            buildMessage(discoverMovies, aUserId, findMovies);
          } else if (text.toLowerCase().startsWith(KW_TV_TOP_RATED.toLowerCase())) {
            String strPageMax = text.substring(KW_TV_TOP_RATED.length(), text.length());
            String[] pageMax = strPageMax.split(",");

            int page = Integer.parseInt(pageMax[0].trim());
            int max = Integer.parseInt(pageMax[1].trim());
            int year = Integer.parseInt(pageMax[2].trim());
            if (pageMax.length == 4) {
              String region = pageMax[3].trim();
              findMovies = newFindMovies().withPage(page).withMax(max).withYear(year).withRegion(region).withFlag(KW_TOP_RATED);
            } else {
              findMovies = newFindMovies().withPage(page).withMax(max).withYear(year).withFlag(KW_TV_TOP_RATED);
            }
            LOG.info("findTvs findTvs {}", findMovies);

            discoverTvs = getTopRatedTvs(fBaseUrl, fApiKey, findMovies);
            LOG.info("Top Rated tvs code {} message {}", discoverTvs.code(), discoverTvs.message());

            buildMessageTvs(discoverTvs, aUserId, findMovies);
          } else if (text.toLowerCase().startsWith(KW_TV_AIRING_TODAY.toLowerCase())) {
            String strPageMax = text.substring(KW_TV_AIRING_TODAY.length(), text.length());
            String[] pageMax = strPageMax.split(",");

            int page = Integer.parseInt(pageMax[0].trim());
            int max = Integer.parseInt(pageMax[1].trim());
            int year = Integer.parseInt(pageMax[2].trim());
            if (pageMax.length == 4) {
              String region = pageMax[3].trim();
              findMovies = newFindMovies().withPage(page).withMax(max).withYear(year).withRegion(region).withFlag(KW_TV_AIRING_TODAY);
            } else {
              findMovies = newFindMovies().withPage(page).withMax(max).withYear(year).withFlag(KW_TV_AIRING_TODAY);
            }
            LOG.info("findTvs findTvs {}", findMovies);

            discoverTvs = getAiringTodayTvs(fBaseUrl, fApiKey, findMovies);
            LOG.info("Airing Today tvs code {} message {}", discoverTvs.code(), discoverTvs.message());

            buildMessageTvs(discoverTvs, aUserId, findMovies);
          } else if (text.toLowerCase().startsWith(KW_TV_ON_AIR.toLowerCase())) {
            String strPageMax = text.substring(KW_TV_ON_AIR.length(), text.length());
            String[] pageMax = strPageMax.split(",");

            int page = Integer.parseInt(pageMax[0].trim());
            int max = Integer.parseInt(pageMax[1].trim());
            int year = Integer.parseInt(pageMax[2].trim());
            if (pageMax.length == 4) {
              String region = pageMax[3].trim();
              findMovies = newFindMovies().withPage(page).withMax(max).withYear(year).withRegion(region).withFlag(KW_TV_ON_AIR);
            } else {
              findMovies = newFindMovies().withPage(page).withMax(max).withYear(year).withFlag(KW_TV_ON_AIR);
            }
            LOG.info("findTvs findTvs {}", findMovies);

            discoverTvs = getOnAirTvs(fBaseUrl, fApiKey, findMovies);
            LOG.info("On Air tvs code {} message {}", discoverTvs.code(), discoverTvs.message());

            buildMessageTvs(discoverTvs, aUserId, findMovies);
          } else {
            stickerMessage(fChannelAccessToken, aUserId, "1", "407");
            unrecognizedMessage(fChannelAccessToken, aUserId);
          }
          break;
      }
    } catch (IOException ignored) {}
  }

  private void buildMessage(Response<DiscoverMovies> aDiscoverMovies, String aUserId,
      FindMovies aFindMovies) throws IOException {
    if (aDiscoverMovies.isSuccessful()) {
      int size = aDiscoverMovies.body().getTotalResults();
      int max = aFindMovies.getMax() + 5;
      List<ResultMovies> movies = aDiscoverMovies.body().getResultMovies();
      carouselMessage(fChannelAccessToken, aUserId, fBaseImgUrl, movies, aFindMovies.getMax());

      LOG.info("buildMessage size {} max {}", size, max);
      if (max < size || !aFindMovies.getFlag().equalsIgnoreCase(KW_FIND)) {
        confirmMessage(fChannelAccessToken, aUserId, aFindMovies);
      }
    } else {
      stickerMessage(fChannelAccessToken, aUserId, "1", "407");
    }
  }

  private void buildMessageTvs(Response<DiscoverTvs> aDiscoverTvs, String aUserId,
      FindMovies aFindMovies) throws IOException {
    if (aDiscoverTvs.isSuccessful()) {
      int size = aDiscoverTvs.body().getTotalResults();
      int max = aFindMovies.getMax() + 5;
      List<ResultTvs> tvs = aDiscoverTvs.body().getResultTvs();
      carouselMessageTv(fChannelAccessToken, aUserId, fBaseImgUrl, tvs, aFindMovies.getMax());

      LOG.info("buildMessage size {} max {}", size, max);
      if (max < size || !aFindMovies.getFlag().equalsIgnoreCase(KW_FIND)) {
        confirmMessage(fChannelAccessToken, aUserId, aFindMovies);
      }
    } else {
      stickerMessage(fChannelAccessToken, aUserId, "1", "407");
    }
  }

}
