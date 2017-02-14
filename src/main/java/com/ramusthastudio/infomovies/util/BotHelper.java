package com.ramusthastudio.infomovies.util;

import com.linecorp.bot.client.LineMessagingServiceBuilder;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.action.URIAction;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.template.ButtonsTemplate;
import com.linecorp.bot.model.message.template.CarouselColumn;
import com.linecorp.bot.model.message.template.CarouselTemplate;
import com.linecorp.bot.model.profile.UserProfileResponse;
import com.linecorp.bot.model.response.BotApiResponse;
import com.ramusthastudio.infomovies.controller.TheMovieDbService;
import com.ramusthastudio.infomovies.model.DiscoverMovies;
import com.ramusthastudio.infomovies.model.ResultMovieDetail;
import com.ramusthastudio.infomovies.model.ResultMovies;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class BotHelper {
  private static final Logger LOG = LoggerFactory.getLogger(BotHelper.class);

  public static final String SOURCE_USER = "user";
  public static final String SOURCE_GROUP = "group";
  public static final String SOURCE_ROOM = "room";

  public static final String JOIN = "join";
  public static final String FOLLOW = "follow";
  public static final String UNFOLLOW = "unfollow";
  public static final String MESSAGE = "message";
  public static final String LEAVE = "leave";
  public static final String POSTBACK = "postback";
  public static final String BEACON = "beacon";

  public static final String MESSAGE_TEXT = "text";
  public static final String MESSAGE_IMAGE = "image";
  public static final String MESSAGE_VIDEO = "video";
  public static final String MESSAGE_AUDIO = "audio";
  public static final String MESSAGE_LOCATION = "location";
  public static final String MESSAGE_STICKER = "sticker";

  public static final String KW_DETAIL = "D";
  public static final String KW_MOVIE_BULAN_INI = "Movie bulan ini";
  public static final String KW_SERIES_BULAN_INI = "Series bulan ini";
  public static final String KW_NOW_PLAYING = "Now playing";
  public static final String KW_ON_THE_AIR = "On air";

  public static Response<UserProfileResponse> getUserProfile(String aChannelAccessToken, String aUserId) throws IOException {
    return LineMessagingServiceBuilder
        .create(aChannelAccessToken)
        .build()
        .getProfile(aUserId)
        .execute();
  }

  public static Response<BotApiResponse> pushMessage(String aChannelAccessToken, String aUserId, String aMsg) throws IOException {
    TextMessage textMessage = new TextMessage(aMsg);
    PushMessage pushMessage = new PushMessage(aUserId, textMessage);
    return LineMessagingServiceBuilder
        .create(aChannelAccessToken)
        .build()
        .pushMessage(pushMessage)
        .execute();
  }

  public static void greetingMessage(String aChannelAccessToken, String aUserId) throws IOException {
    UserProfileResponse userProfile = getUserProfile(aChannelAccessToken, aUserId).body();
    String greeting = "Hi " + userProfile.getDisplayName() + ", selamat datang di Info Movies\n";
    greeting += "Terima kasih telah menambahkan saya sebagai teman! \n\n";
    greeting += "Jika kamu menerima terlalu banyak pemberitahuan, ";
    greeting += "silahkan buka pengaturan dari ruang obrolan ini dan matikan Pemberitahuan. ";
    createMessage(aChannelAccessToken, aUserId, greeting);
  }

  public static void unrecognizedMessage(String aChannelAccessToken, String aUserId) throws IOException {
    UserProfileResponse userProfile = getUserProfile(aChannelAccessToken, aUserId).body();
    String greeting = "Hi " + userProfile.getDisplayName() + ", apakah kamu kesulitan ?\n\n";
    greeting += "Now Playing : '" + KW_NOW_PLAYING + "'! \n";
    greeting += "On Air Series : '" + KW_ON_THE_AIR + "'! \n";
    greeting += "Daftar Movie bulan ini : '" + KW_MOVIE_BULAN_INI + "'! \n";
    greeting += "Daftar Series bulan ini : '" + KW_SERIES_BULAN_INI + "! \n";
    createMessage(aChannelAccessToken, aUserId, greeting);
  }

  public static Response<BotApiResponse> createMessage(String aChannelAccessToken, String aUserId,
      String aMsg) throws IOException {
    TextMessage textMessage = new TextMessage(aMsg);
    PushMessage pushMessage = new PushMessage(aUserId, textMessage);
    return LineMessagingServiceBuilder
        .create(aChannelAccessToken)
        .build()
        .pushMessage(pushMessage)
        .execute();
  }

  public static Response<BotApiResponse> createCarouselMessage(String aChannelAccessToken,
      String aUserId, List<CarouselColumn> aCarouselColumns) throws IOException {
    CarouselTemplate carouselTemplate = new CarouselTemplate(aCarouselColumns);
    TemplateMessage templateMessage = new TemplateMessage("Your search result", carouselTemplate);
    PushMessage pushMessage = new PushMessage(aUserId, templateMessage);
    return LineMessagingServiceBuilder
        .create(aChannelAccessToken)
        .build()
        .pushMessage(pushMessage)
        .execute();
  }

  public static Response<BotApiResponse> buildButtonDetailMovie(String aChannelAccessToken,
      String aBaseImgUrl, String aUserId, ResultMovieDetail aMovieDetail) throws IOException {
    String filterTitle = filterTitle(aMovieDetail.getTitle());
    String filterOverview = filterOverview(aMovieDetail.getOverview());

    ButtonsTemplate buttonsTemplate = new ButtonsTemplate(
        aBaseImgUrl + aMovieDetail.getPosterPath(),
        filterTitle,
        filterOverview,
        Collections.singletonList(new URIAction("Homepage", aMovieDetail.getHomepage())));
    TemplateMessage templateMessage = new TemplateMessage(filterTitle, buttonsTemplate);
    PushMessage pushMessage = new PushMessage(aUserId, templateMessage);
    return LineMessagingServiceBuilder
        .create(aChannelAccessToken)
        .build()
        .pushMessage(pushMessage)
        .execute();
  }

  public static String createGenres(List<Integer> aGenreIds) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < aGenreIds.size(); i++) {
      int id = aGenreIds.get(i);
      for (EGenre genre : EGenre.values()) {
        if (genre.getGenre() == id) {
          sb.append(",").append(genre.getDisplayname());
        }
      }
    }

    return sb.toString().replaceFirst(",", "");
  }

  public static Response<DiscoverMovies> getDiscoverMovies(String aBaseUrl, String aApiKey) throws IOException {
    Retrofit retrofit = new Retrofit.Builder().baseUrl(aBaseUrl)
        .addConverterFactory(GsonConverterFactory.create()).build();
    TheMovieDbService service = retrofit.create(TheMovieDbService.class);

    LocalDate now = LocalDate.now();
    return service.discoverMovies(aApiKey, now.getYear()).execute();
  }

  public static Response<DiscoverMovies> getNowPlayingMovies(String aBaseUrl, String aApiKey) throws IOException {
    Retrofit retrofit = new Retrofit.Builder().baseUrl(aBaseUrl)
        .addConverterFactory(GsonConverterFactory.create()).build();
    TheMovieDbService service = retrofit.create(TheMovieDbService.class);

    return service.nowPlayingMovies(aApiKey).execute();
  }

  public static Response<ResultMovieDetail> getDetailMovie(String aBaseUrl, int aMovieId, String aApiKey) throws IOException {
    Retrofit retrofit = new Retrofit.Builder().baseUrl(aBaseUrl)
        .addConverterFactory(GsonConverterFactory.create()).build();
    TheMovieDbService service = retrofit.create(TheMovieDbService.class);

    return service.detailMovies(aMovieId, aApiKey).execute();
  }

  public static List<CarouselColumn> buildCarouselResultMovies(String aBaseImgUrl, List<ResultMovies> discoverMovies) {
    List<CarouselColumn> carouselColumn = new ArrayList<>();
    for (ResultMovies resultMovies : discoverMovies) {

      LOG.info("ResultMovies title {}\n genre {}\n overview {}\n",
          resultMovies.getTitle(),
          createGenres(resultMovies.getGenreIds()),
          resultMovies.getOverview());

      String filterTitle = filterTitle(resultMovies.getTitle());
      String filterOverview = filterOverview(resultMovies.getOverview());

      if (carouselColumn.size() < 5) {
        carouselColumn.add(
            new CarouselColumn(
                aBaseImgUrl + resultMovies.getBackdropPath(),
                filterTitle + " (" + resultMovies.getVoteAverage() + ")",
                createGenres(resultMovies.getGenreIds()),
                Arrays.asList(
                    new URIAction("Poster", aBaseImgUrl + resultMovies.getPosterPath()),
                    new MessageAction("Overview", filterOverview),
                    new MessageAction("Detail", "D" + resultMovies.getId()))));
      }
    }

    return carouselColumn;
  }

  public static String filterTitle(String aTitle) {
    String filterTitle;
    if (aTitle.length() > 40) {
      filterTitle = aTitle.substring(0, 30) + "...";
    } else {
      filterTitle = aTitle;
    }
    return filterTitle;
  }

  public static String filterOverview(String aOverview) {
    String filterOverview;
    if (aOverview.length() > 300) {
      filterOverview = aOverview.substring(0, 250) + "...";
    } else {
      filterOverview = aOverview;
    }
    return filterOverview;
  }
}
