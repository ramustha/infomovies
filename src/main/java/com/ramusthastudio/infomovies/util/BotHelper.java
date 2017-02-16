package com.ramusthastudio.infomovies.util;

import com.linecorp.bot.client.LineMessagingServiceBuilder;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.action.URIAction;
import com.linecorp.bot.model.message.StickerMessage;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.template.ButtonsTemplate;
import com.linecorp.bot.model.message.template.CarouselColumn;
import com.linecorp.bot.model.message.template.CarouselTemplate;
import com.linecorp.bot.model.message.template.ConfirmTemplate;
import com.linecorp.bot.model.profile.UserProfileResponse;
import com.linecorp.bot.model.response.BotApiResponse;
import com.ramusthastudio.infomovies.controller.TheMovieDbService;
import com.ramusthastudio.infomovies.model.DiscoverMovies;
import com.ramusthastudio.infomovies.model.Genre;
import com.ramusthastudio.infomovies.model.ResultMovieDetail;
import com.ramusthastudio.infomovies.model.ResultMovies;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class BotHelper {
  private static final Logger LOG = LoggerFactory.getLogger(BotHelper.class);
  private static final String DFL_LANGUAGE = "en-US";
  private static final String DFL_REGION = "ID";

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

  public static final String KW_SEARCH = "Find";
  public static final String KW_DETAIL = "Detail";
  public static final String KW_DETAIL_OVERVIEW = "Overview";
  public static final String KW_NOW_PLAYING = "Now Playing";
  public static final String KW_LATEST = "Latest";
  public static final String KW_POPULAR = "Popular";
  public static final String KW_TOP_RATED = "Top Rated";
  public static final String KW_UPCOMING = "Comming Soon";
  public static final String KW_RECOMMEND = "Recommend";
  public static final String KW_SIMILAR = "Similar";
  public static final String KW_VIDEOS = "Video";
  public static final String KW_PANDUAN = "Panduan";
  public static final String KW_NEXT_POPULAR = "NP";

  public static final String IMG_HOLDER = "https://www.themoviedb.org/assets/static_cache/41bdcf10bbf6f84c0fc73f27b2180b95/images/v4/logos/91x81.png";

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
    greeting += "Panduan di Info Movies:\n";
    greeting += "Now Playing : '" + KW_NOW_PLAYING + "' \n";
    greeting += "Cari Movie : '" + KW_SEARCH + " Judul, Tahun(Opsional)'";
    createMessage(aChannelAccessToken, aUserId, greeting);
  }

  public static void unrecognizedMessage(String aChannelAccessToken, String aUserId) throws IOException {
    UserProfileResponse userProfile = getUserProfile(aChannelAccessToken, aUserId).body();
    String greeting = "Hi " + userProfile.getDisplayName() + ", apakah kamu kesulitan ?\n\n";
    greeting += "Panduan Info Movies:\n";
    greeting += "Panduan : '" + KW_PANDUAN + "' \n";
    greeting += "Now Playing : '" + KW_NOW_PLAYING + "' \n";
    greeting += "Cari Movie : '" + KW_SEARCH + " Judul, Tahun(Opsional)' \n";
    // greeting += "Daftar Movie bulan ini : '" + KW_MOVIE_BULAN_INI + "' \n";
    // greeting += "On Air Series : '" + KW_ON_THE_AIR + "'! \n";
    // greeting += "Daftar Series bulan ini : '" + KW_SERIES_BULAN_INI + "! \n";
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

  public static Response<BotApiResponse> createSticker(String aChannelAccessToken, String aUserId,
      String aPackageId, String stickerId) throws IOException {
    StickerMessage stickerMessage = new StickerMessage(aPackageId, stickerId);
    PushMessage pushMessage = new PushMessage(aUserId, stickerMessage);
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

  public static Response<BotApiResponse> createConfirmMessage(String aChannelAccessToken,
      String aUserId, String aMsg, int page) throws IOException {
    ConfirmTemplate confirmTemplate = new ConfirmTemplate(aMsg, Arrays.asList(
        new PostbackAction("Ya", KW_NEXT_POPULAR + " " + ++page),
        new PostbackAction("Panduan", KW_PANDUAN)));

    TemplateMessage templateMessage = new TemplateMessage("Confirm ?", confirmTemplate);
    PushMessage pushMessage = new PushMessage(aUserId, templateMessage);
    return LineMessagingServiceBuilder
        .create(aChannelAccessToken)
        .build()
        .pushMessage(pushMessage)
        .execute();
  }

  public static Response<BotApiResponse> buildButtonDetailMovie(String aChannelAccessToken,
      String aBaseImdbUrl, String aBaseImgUrl, String aUserId, ResultMovieDetail aMovieDetail) throws IOException {
    String filterTitle = filterTitle(aMovieDetail.getTitle());
    String filterTagLine = filterTagLine(createFromGenre(aMovieDetail.getGenres()));
    String hp = aMovieDetail.getHomepage() == null ? "" : aMovieDetail.getHomepage();
    String homepage = hp.length() == 0 ? aBaseImdbUrl + aMovieDetail.getImdbId() : aMovieDetail.getHomepage();

    String backdropImg = aMovieDetail.getBackdropPath() == null ?
        (aMovieDetail.getPosterPath() == null ? IMG_HOLDER : aBaseImgUrl + aMovieDetail.getPosterPath()) :
        aBaseImgUrl + aMovieDetail.getBackdropPath();

    String posterImg = aMovieDetail.getPosterPath() == null ?
        (aMovieDetail.getBackdropPath() == null ? IMG_HOLDER : aBaseImgUrl + aMovieDetail.getBackdropPath()) :
        aBaseImgUrl + aMovieDetail.getPosterPath();

    LOG.info("ResultMovies poster {}\n backdrop {}\n title {}\n genre {}\n homepage {}\n",
        posterImg,
        backdropImg,
        filterTitle + " (" + aMovieDetail.getVoteAverage() + ")",
        filterTagLine,
        homepage);

    ButtonsTemplate buttonsTemplate = new ButtonsTemplate(
        backdropImg,
        filterTitle + " (" + aMovieDetail.getVoteAverage() + ")",
        filterTagLine,
        Arrays.asList(
            new PostbackAction("Overview", KW_DETAIL_OVERVIEW + " " + aMovieDetail.getId()),
            new URIAction("Homepage", homepage)
        ));

    TemplateMessage templateMessage = new TemplateMessage(filterTitle, buttonsTemplate);
    PushMessage pushMessage = new PushMessage(aUserId, templateMessage);
    return LineMessagingServiceBuilder
        .create(aChannelAccessToken)
        .build()
        .pushMessage(pushMessage)
        .execute();
  }

  public static String createFromGenreId(List<Integer> aGenreIds) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < aGenreIds.size(); i++) {
      int id = aGenreIds.get(i);
      for (EGenre genre : EGenre.values()) {
        if (genre.getGenre() == id) {
          sb.append(",").append(genre.getDisplayname());
        }
      }
    }
    String genre = sb.toString().replaceFirst(",", "");
    if (genre.isEmpty() || genre.length() < 3) {
      genre = "N/A";
    }
    return genre;
  }

  public static String createFromGenre(List<Genre> aGenres) {
    StringBuilder sb = new StringBuilder();
    for (Genre genre : aGenres) {
      sb.append(",").append(genre.getName());
    }
    String genre = sb.toString().replaceFirst(",", "");
    if (genre.isEmpty() || genre.length() < 3) {
      genre = "N/A";
    }
    return genre;
  }

  public static TheMovieDbService createdService(String aBaseUrl) {
    Retrofit retrofit = new Retrofit.Builder().baseUrl(aBaseUrl)
        .addConverterFactory(GsonConverterFactory.create()).build();
    return retrofit.create(TheMovieDbService.class);
  }

  public static Response<DiscoverMovies> getTopRatedMovies(String aBaseUrl, String aApiKey, int aPage) throws IOException {
    return getTopRatedMovies(aBaseUrl, aApiKey, DFL_LANGUAGE, aPage, DFL_REGION);
  }

  public static Response<DiscoverMovies> getTopRatedMovies(String aBaseUrl, String aApiKey, int aPage, String aRegion) throws IOException {
    String region = aRegion != null ? aRegion : DFL_REGION;
    int page = aPage != 0 ? aPage : 0;
    return getTopRatedMovies(aBaseUrl, aApiKey, DFL_LANGUAGE, page, region);
  }

  public static Response<DiscoverMovies> getTopRatedMovies(String aBaseUrl, String aApiKey, String aLanguage, int aPage, String aRegion) throws IOException {
    TheMovieDbService service = createdService(aBaseUrl);

    String language = aLanguage != null ? aLanguage : DFL_LANGUAGE;
    int page = aPage != 0 ? aPage : 0;
    String region = aRegion != null ? aRegion : DFL_REGION;

    return service.topRatedMovies(aApiKey, language, page, region).execute();
  }

  public static Response<DiscoverMovies> getPopularMovies(String aBaseUrl, String aApiKey, int aPage) throws IOException {
    return getPopularMovies(aBaseUrl, aApiKey, DFL_LANGUAGE, aPage, DFL_REGION);
  }

  public static Response<DiscoverMovies> getPopularMovies(String aBaseUrl, String aApiKey, int aPage, String aRegion) throws IOException {
    String region = aRegion != null ? aRegion : DFL_REGION;
    int page = aPage != 0 ? aPage : 0;
    return getPopularMovies(aBaseUrl, aApiKey, DFL_LANGUAGE, page, region);
  }

  public static Response<DiscoverMovies> getPopularMovies(String aBaseUrl, String aApiKey, String aLanguage, int aPage, String aRegion) throws IOException {
    TheMovieDbService service = createdService(aBaseUrl);

    String language = aLanguage != null ? aLanguage : DFL_LANGUAGE;
    int page = aPage != 0 ? aPage : 0;
    String region = aRegion != null ? aRegion : DFL_REGION;

    return service.popularMovies(aApiKey, language, page, region).execute();
  }

  public static Response<DiscoverMovies> getNowPlayingMovies(String aBaseUrl, String aApiKey, int aPage) throws IOException {
    return getNowPlayingMovies(aBaseUrl, aApiKey, DFL_LANGUAGE, aPage, DFL_REGION);
  }

  public static Response<DiscoverMovies> getNowPlayingMovies(String aBaseUrl, String aApiKey, int aPage, String aRegion) throws IOException {
    String region = aRegion != null ? aRegion : DFL_REGION;
    int page = aPage != 0 ? aPage : 0;
    return getNowPlayingMovies(aBaseUrl, aApiKey, DFL_LANGUAGE, page, region);
  }

  public static Response<DiscoverMovies> getNowPlayingMovies(String aBaseUrl, String aApiKey, String aLanguage, int aPage, String aRegion) throws IOException {
    TheMovieDbService service = createdService(aBaseUrl);

    String language = aLanguage != null ? aLanguage : DFL_LANGUAGE;
    int page = aPage != 0 ? aPage : 0;
    String region = aRegion != null ? aRegion : DFL_REGION;

    return service.nowPlayingMovies(aApiKey, language, page, region).execute();
  }

  public static Response<DiscoverMovies> getSearchMovies(String aBaseUrl, String aApiKey, String aTitle, int aYear) throws IOException {
    TheMovieDbService service = createdService(aBaseUrl);
    if (aYear == 0) {
      return service.searchMovies(aApiKey, DFL_LANGUAGE, aTitle, 1, DFL_REGION).execute();
    }
    return service.searchMovies(aApiKey, DFL_LANGUAGE, aTitle, 1, DFL_REGION, aYear).execute();
  }

  // public static Response<DiscoverMovies> getDiscoverMovies(String aBaseUrl, String aApiKey) throws IOException {
  //   TheMovieDbService service = createdService(aBaseUrl);
  //
  //   LocalDate now = LocalDate.now();
  //   return service.discoverMovies(aApiKey, now.getYear()).execute();
  // }

  public static Response<ResultMovieDetail> getDetailMovie(String aBaseUrl, int aMovieId, String aApiKey) throws IOException {
    Retrofit retrofit = new Retrofit.Builder().baseUrl(aBaseUrl)
        .addConverterFactory(GsonConverterFactory.create()).build();
    TheMovieDbService service = retrofit.create(TheMovieDbService.class);

    return service.detailMovies(aMovieId, aApiKey).execute();
  }

  public static List<CarouselColumn> buildCarouselResultMovies(String aBaseImgUrl, List<ResultMovies> aResultMovies) {
    List<CarouselColumn> carouselColumn = new ArrayList<>();
    int min = ThreadLocalRandom.current().nextInt(1, 15 + 1);
    List<ResultMovies> resultMovies = aResultMovies.subList(min, min + 5);
    for (ResultMovies movies : resultMovies) {

      String filterTitle = filterTitle(movies.getTitle());
      String filterTagLine = filterTagLine(createFromGenreId(movies.getGenreIds()));

      String backdropImg = movies.getBackdropPath() == null ?
          (movies.getPosterPath() == null ? IMG_HOLDER : aBaseImgUrl + movies.getPosterPath()) :
          aBaseImgUrl + movies.getBackdropPath();

      String posterImg = movies.getPosterPath() == null ?
          (movies.getBackdropPath() == null ? IMG_HOLDER : aBaseImgUrl + movies.getBackdropPath()) :
          aBaseImgUrl + movies.getPosterPath();

      if (carouselColumn.size() < 5) {

        LOG.info("ResultMovies poster {}\n backdrop {}\n title {}\n genre {}\n id {}\n",
            posterImg,
            backdropImg,
            filterTitle + " (" + movies.getVoteAverage() + ")",
            filterTagLine,
            KW_DETAIL + " " + movies.getId());

        carouselColumn.add(
            new CarouselColumn(
                backdropImg,
                filterTitle + " (" + movies.getVoteAverage() + ")",
                filterTagLine,
                Arrays.asList(
                    new URIAction("Poster", posterImg),
                    new PostbackAction("Detail", KW_DETAIL + " " + movies.getId()))));
      }
    }

    return carouselColumn;
  }

  public static String createDetailOverview(ResultMovieDetail aMovieDetail, String aImdbUrl) {
    String overview = "Title: " + aMovieDetail.getOriginalTitle() + "\n";
    overview += "Genre: " + createFromGenre(aMovieDetail.getGenres()) + "\n";
    overview += "Rating: " + aMovieDetail.getVoteAverage() + " (" + aMovieDetail.getVoteCount() + ")\n";
    overview += "Release date: " + aMovieDetail.getReleaseDate() + "\n";
    overview += "IMDB: " + aImdbUrl + aMovieDetail.getImdbId() + "\n";
    overview += "Overview: \n" + aMovieDetail.getOverview() + "\n";
    return overview;
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

  public static String filterTagLine(String aTitle) {
    String filterTitle;
    if (aTitle.length() > 60) {
      filterTitle = aTitle.substring(0, 55) + "...";
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
