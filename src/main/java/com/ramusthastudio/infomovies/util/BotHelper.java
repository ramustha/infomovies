package com.ramusthastudio.infomovies.util;

import com.linecorp.bot.client.LineMessagingServiceBuilder;
import com.linecorp.bot.model.Multicast;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.action.URIAction;
import com.linecorp.bot.model.message.StickerMessage;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.VideoMessage;
import com.linecorp.bot.model.message.template.ButtonsTemplate;
import com.linecorp.bot.model.message.template.CarouselColumn;
import com.linecorp.bot.model.message.template.CarouselTemplate;
import com.linecorp.bot.model.message.template.ConfirmTemplate;
import com.linecorp.bot.model.message.template.Template;
import com.linecorp.bot.model.profile.UserProfileResponse;
import com.linecorp.bot.model.response.BotApiResponse;
import com.ramusthastudio.infomovies.controller.TheMovieDbService;
import com.ramusthastudio.infomovies.model.DiscoverMovies;
import com.ramusthastudio.infomovies.model.DiscoverTvs;
import com.ramusthastudio.infomovies.model.FindMovies;
import com.ramusthastudio.infomovies.model.Genre;
import com.ramusthastudio.infomovies.model.ResultMovieDetail;
import com.ramusthastudio.infomovies.model.ResultMovies;
import com.ramusthastudio.infomovies.model.ResultTvs;
import com.ramusthastudio.infomovies.model.ResultTvsDetail;
import com.ramusthastudio.infomovies.model.ResultTvsVideo;
import com.ramusthastudio.infomovies.model.Seasons;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class BotHelper {
  private static final Logger LOG = LoggerFactory.getLogger(BotHelper.class);
  public static final String DFL_REGION = "ID";

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

  public static final String KW_FIND = "Find";
  public static final String KW_TV_FIND = "Tv Find";
  public static final String KW_DETAIL = "Detail";
  public static final String KW_TV_DETAIL = "Tv Detail";
  public static final String KW_STAR = "Star";
  public static final String KW_DETAIL_OVERVIEW = "Overview";
  public static final String KW_TV_DETAIL_OVERVIEW = "Tv Overview";
  public static final String KW_TV_DETAIL_TRAILER_OVERVIEW = "Tv Trailer Overview";
  public static final String KW_NOW_PLAYING = "Now Playing";
  public static final String KW_LATEST = "Latest";
  public static final String KW_TV_LATEST = "Tv Latest";
  public static final String KW_POPULAR = "Popular";
  public static final String KW_TV_POPULAR = "Tv Popular";
  public static final String KW_TV_AIRING_TODAY = "Tv Airing Today";
  public static final String KW_TV_ON_AIR = "Tv On Air";
  public static final String KW_TV_TOP_RATED = "Tv Top Rated";
  public static final String KW_TOP_RATED = "Top Rated";
  public static final String KW_UPCOMING = "Coming Soon";
  public static final String KW_RECOMMEND = "Recommend";
  public static final String KW_SIMILAR = "Similar";
  public static final String KW_VIDEOS = "Video";
  public static final String KW_PANDUAN = "Panduan";

  public static final String IMG_HOLDER = "https://docs.google.com/uc?id=0B-F-b_ahxeRqcXBiMFVvQ1dwYlE";

  public static Response<UserProfileResponse> getUserProfile(String aChannelAccessToken,
      String aUserId) throws IOException {
    return LineMessagingServiceBuilder.create(aChannelAccessToken).build().getProfile(aUserId).execute();
  }

  public static Response<BotApiResponse> replayMessage(String aChannelAccessToken, String aReplayToken,
      String aMsg) throws IOException {
    TextMessage message = new TextMessage(aMsg);
    ReplyMessage pushMessage = new ReplyMessage(aReplayToken, message);
    return LineMessagingServiceBuilder.create(aChannelAccessToken).build().replyMessage(pushMessage).execute();
  }

  public static Response<BotApiResponse> pushMessage(String aChannelAccessToken, String aUserId,
      String aMsg) throws IOException {
    TextMessage message = new TextMessage(aMsg);
    PushMessage pushMessage = new PushMessage(aUserId, message);
    return LineMessagingServiceBuilder.create(aChannelAccessToken).build().pushMessage(pushMessage).execute();
  }

  public static Response<BotApiResponse> multicastMessage(String aChannelAccessToken, Set<String> aUserIds,
      String aMsg) throws IOException {
    TextMessage message = new TextMessage(aMsg);
    Multicast pushMessage = new Multicast(aUserIds, message);
    return LineMessagingServiceBuilder.create(aChannelAccessToken).build().multicast(pushMessage).execute();
  }

  public static Response<BotApiResponse> templateMessage(String aChannelAccessToken, String aUserId,
      Template aTemplate) throws IOException {
    TemplateMessage message = new TemplateMessage("Result", aTemplate);
    PushMessage pushMessage = new PushMessage(aUserId, message);
    return LineMessagingServiceBuilder.create(aChannelAccessToken).build().pushMessage(pushMessage).execute();
  }

  public static Response<BotApiResponse> stickerMessage(String aChannelAccessToken, String aUserId,
      String aPackageId, String stickerId) throws IOException {
    StickerMessage message = new StickerMessage(aPackageId, stickerId);
    PushMessage pushMessage = new PushMessage(aUserId, message);
    return LineMessagingServiceBuilder.create(aChannelAccessToken).build().pushMessage(pushMessage).execute();
  }

  public static Response<BotApiResponse> videoMessage(String aChannelAccessToken, String aUserId,
      String aImageHolderUrl, String aVideoUrl) throws IOException {
    VideoMessage message = new VideoMessage(aVideoUrl, aImageHolderUrl);
    PushMessage pushMessage = new PushMessage(aUserId, message);
    return LineMessagingServiceBuilder.create(aChannelAccessToken).build().pushMessage(pushMessage).execute();
  }

  public static Response<BotApiResponse> carouselMessage(String aChannelAccessToken, String aUserId,
      String aBaseImgUrl, List<ResultMovies> aResultMovies, int aMax) throws IOException {
    List<CarouselColumn> carouselColumn = buildCarouselColumn(aBaseImgUrl, aResultMovies, aMax);
    CarouselTemplate template = new CarouselTemplate(carouselColumn);
    return templateMessage(aChannelAccessToken, aUserId, template);
  }

  public static Response<BotApiResponse> carouselMessageTv(String aChannelAccessToken, String aUserId,
      String aBaseImgUrl, List<ResultTvs> aResultTvs, int aMax) throws IOException {
    List<CarouselColumn> carouselColumn = buildCarouselColumnTv(aBaseImgUrl, aResultTvs, aMax);
    CarouselTemplate template = new CarouselTemplate(carouselColumn);
    return templateMessage(aChannelAccessToken, aUserId, template);
  }

  public static Response<BotApiResponse> confirmMessage(String aChannelAccessToken, String aUserId,
      FindMovies aFindMovies) throws IOException {
    int page = (aFindMovies.getMax() == 15 ? aFindMovies.getPage() + 1 : aFindMovies.getPage());
    int max = (aFindMovies.getMax() == 15 ? 0 : aFindMovies.getMax() + 5);
    int year = aFindMovies.getYear();
    String region = aFindMovies.getRegion() == null ? "" : aFindMovies.getRegion();
    String data = aFindMovies.getFlag() + " " + page + "," + max + "," + year + "," + region;

    ConfirmTemplate template = new ConfirmTemplate("Lihat yang lain ?", Arrays.asList(
        new PostbackAction("Ya", data),
        new PostbackAction("Panduan", KW_PANDUAN)));

    return templateMessage(aChannelAccessToken, aUserId, template);
  }

  public static Response<BotApiResponse> buttonMessage(String aChannelAccessToken, String aBaseImdbUrl,
      String aBaseImgUrl, String aUserId, ResultMovieDetail aMovieDetail) throws IOException {
    String title = createTitle(aMovieDetail.getTitle());
    String tagline = createTagline(createFromGenre(aMovieDetail.getGenres()));
    String homepage = createHomepage(aBaseImdbUrl, aMovieDetail.getHomepage(), aMovieDetail.getImdbId());
    String backDropPath = createBackDropPath(aBaseImgUrl, aMovieDetail.getBackdropPath(), aMovieDetail.getPosterPath());
    String posterPath = createPosterPath(aBaseImgUrl, aMovieDetail.getBackdropPath(), aMovieDetail.getPosterPath());

    LOG.info("ResultMovies poster {}\n backdrop {}\n title {}\n genre {}\n homepage {}\n",
        posterPath, backDropPath, title + " (" + aMovieDetail.getVoteAverage() + ")", tagline, homepage);

    ButtonsTemplate template = new ButtonsTemplate(
        backDropPath, title + " (" + aMovieDetail.getVoteAverage() + ")", tagline,
        Arrays.asList(
            new PostbackAction("Overview", KW_DETAIL_OVERVIEW + " " + aMovieDetail.getId()),
            new URIAction("IMDB", aBaseImdbUrl + aMovieDetail.getImdbId()),
            new URIAction("Homepage", homepage)
        ));

    return templateMessage(aChannelAccessToken, aUserId, template);
  }

  public static Response<BotApiResponse> buttonMessageTv(String aChannelAccessToken, String aBaseImdbUrl,
      String aBaseImgUrl, String aUserId, ResultTvsDetail aTvDetail) throws IOException {
    String title = createTitle(aTvDetail.getName());
    List<Seasons> seasons = aTvDetail.getSeasons();
    String tagline = createTagline(
        "Current Season: " + aTvDetail.getNumberOfSeasons() + "\n" +
            "Current episode: " + seasons.get(seasons.size() - 1).getEpisodeCount() + "\n" +
            "Duration: " + aTvDetail.getEpisodeRunTimes().get(0) + " M\n"

    );
    String homepage = createHomepage(aBaseImdbUrl, aTvDetail.getHomepage(), "");
    String backDropPath = createBackDropPath(aBaseImgUrl, aTvDetail.getBackdropPath(), aTvDetail.getPosterPath());
    String posterPath = createPosterPath(aBaseImgUrl, aTvDetail.getBackdropPath(), aTvDetail.getPosterPath());

    LOG.info("ResultTvs poster {}\n backdrop {}\n title {}\n genre {}\n homepage {}\n",
        posterPath, backDropPath, title + " (" + aTvDetail.getVoteAverage() + ")", tagline, homepage);

    ButtonsTemplate template = new ButtonsTemplate(
        backDropPath, title + " (" + aTvDetail.getVoteAverage() + ")", tagline,
        Arrays.asList(
            new PostbackAction("Trailer", KW_TV_DETAIL_TRAILER_OVERVIEW + " " + aTvDetail.getId()),
            new PostbackAction("Overview", KW_TV_DETAIL_OVERVIEW + " " + aTvDetail.getId()),
            new URIAction("Homepage", homepage)
        ));

    return templateMessage(aChannelAccessToken, aUserId, template);
  }

  public static List<CarouselColumn> buildCarouselColumn(String aBaseImgUrl, List<ResultMovies> aResultMovies,
      int aMin) {
    List<CarouselColumn> carouselColumn = new ArrayList<>();
    List<ResultMovies> resultMovies;
    if (aResultMovies.size() > 5) {
      int max = aMin + 5;
      max = max > aResultMovies.size() ? aResultMovies.size() : max;
      resultMovies = aResultMovies.subList(aMin, max);
    } else {
      resultMovies = aResultMovies;
    }

    for (ResultMovies movies : resultMovies) {
      String filterTitle = createTitle(movies.getTitle());
      String filterTagLine = createTagline(createFromGenreId(movies.getGenreIds()));
      String backDropPath = createBackDropPath(aBaseImgUrl, movies.getBackdropPath(), movies.getPosterPath());
      String posterPath = createPosterPath(aBaseImgUrl, movies.getBackdropPath(), movies.getPosterPath());

      LOG.info("ResultMovies poster {}\n backdrop {}\n title {}\n genre {}\n id {}\n",
          posterPath, backDropPath, filterTitle + " (" + movies.getVoteAverage() + ")", filterTagLine,
          KW_DETAIL + " " + movies.getId());

      carouselColumn.add(
          new CarouselColumn(
              backDropPath, filterTitle + " (" + movies.getVoteAverage() + ")", filterTagLine,
              Arrays.asList(
                  new URIAction("Poster", posterPath),
                  new PostbackAction("Detail", KW_DETAIL + " " + movies.getId()))));
    }

    return carouselColumn;
  }

  public static List<CarouselColumn> buildCarouselColumnTv(String aBaseImgUrl, List<ResultTvs> aResultTvs,
      int aMin) {
    List<CarouselColumn> carouselColumn = new ArrayList<>();
    List<ResultTvs> resultMovies;
    if (aResultTvs.size() > 5) {
      int max = aMin + 5;
      max = max > aResultTvs.size() ? aResultTvs.size() : max;
      resultMovies = aResultTvs.subList(aMin, max);
    } else {
      resultMovies = aResultTvs;
    }

    for (ResultTvs tv : resultMovies) {
      String filterTitle = createTitle(tv.getName());
      String filterTagLine = createTagline(createFromGenreId(tv.getGenreIds()));
      String backDropPath = createBackDropPath(aBaseImgUrl, tv.getBackdropPath(), tv.getPosterPath());
      String posterPath = createPosterPath(aBaseImgUrl, tv.getBackdropPath(), tv.getPosterPath());

      LOG.info("ResultTvs poster {}\n backdrop {}\n title {}\n genre {}\n id {}\n",
          posterPath, backDropPath, filterTitle + " (" + tv.getVoteAverage() + ")", filterTagLine,
          KW_DETAIL + " " + tv.getId());

      carouselColumn.add(
          new CarouselColumn(
              backDropPath, filterTitle + " (" + tv.getVoteAverage() + ")", filterTagLine,
              Arrays.asList(
                  new URIAction("Poster", posterPath),
                  new PostbackAction("Detail", KW_TV_DETAIL + " " + tv.getId()))));
    }

    return carouselColumn;
  }

  public static void greetingMessage(String aChannelAccessToken, String aUserId) throws IOException {
    stickerMessage(aChannelAccessToken, aUserId, "1", "125");
    UserProfileResponse userProfile = getUserProfile(aChannelAccessToken, aUserId).body();
    String greeting = "Hi " + userProfile.getDisplayName() + ", selamat datang di Info Movies\n";
    greeting += "Terima kasih telah menambahkan aku sebagai teman! \n\n";
    greeting += "Disini kamu bisa meminta aku untuk memberikan informasi seputar movie maupun series...\n\n";
    greeting += "Kalau kamu suka dengan aku tolong invite teman kamu yah supaya add line aku di @wix3579a";
    pushMessage(aChannelAccessToken, aUserId, greeting);
    unrecognizedMessage(aChannelAccessToken, aUserId);
  }

  public static void greetingMessageGroup(String aChannelAccessToken, String aUserId) throws IOException {
    String greeting = "Hi Manteman, makasih yah udah invite aku disini\n";
    greeting += "kalian bisa meminta aku untuk memberikan informasi seputar movie maupun series...\n\n";
    greeting += "Kalau kamu suka dengan aku tolong invite teman kamu yah supaya add line aku di @wix3579a";
    pushMessage(aChannelAccessToken, aUserId, greeting);
    unrecognizedMessage(aChannelAccessToken, aUserId);
  }

  public static void errorMessage(String aChannelAccessToken, String aUserId) throws IOException {
    stickerMessage(aChannelAccessToken, aUserId, "1", "123");
    UserProfileResponse userProfile = getUserProfile(aChannelAccessToken, aUserId).body();
    String msg = "Hi " + userProfile.getDisplayName() + "\n";
    msg += "Mohon maaf, pesan yang kamu minta gagal di tampilkan, coba ulangi lagi! \n\n";
    pushMessage(aChannelAccessToken, aUserId, msg);
  }

  public static void unrecognizedMessage(String aChannelAccessToken, String aUserId) throws IOException {
    String greeting = "Panduan Info Movies:\n\n";
    greeting += "Movies...\n\n";
    greeting += "1. " + KW_NOW_PLAYING + " *(ID)\n";
    greeting += "2. " + KW_POPULAR + " *(ID)\n";
    greeting += "3. " + KW_TOP_RATED + " *(ID)\n";
    greeting += "4. " + KW_UPCOMING + " *(ID)\n";
    greeting += "5. " + KW_POPULAR + " *(ID)\n\n";

    greeting += "6. " + KW_FIND + " Judul, *(2014)\n";
    greeting += "7. " + KW_FIND + " Judul, *(ID)\n\n";

    greeting += "Tv Series...\n\n";
    greeting += "1. " + KW_TV_POPULAR + "\n";
    greeting += "2. " + KW_TV_TOP_RATED + "\n";
    greeting += "3. " + KW_TV_AIRING_TODAY + "\n";
    greeting += "4. " + KW_TV_ON_AIR + "\n\n";

    greeting += "5. " + KW_TV_FIND + " Judul, *(2014)\n";
    // greeting += "Daftar Movie bulan ini : '" + KW_MOVIE_BULAN_INI + "' \n";
    // greeting += "On Air Series : '" + KW_ON_THE_AIR + "'! \n";
    greeting += "\n\n*Opsional";
    pushMessage(aChannelAccessToken, aUserId, greeting);
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

  public static String createDetailOverviewTv(ResultTvsDetail aTvDetail) {
    String overview = "Title: " + aTvDetail.getName() + "\n";
    overview += "Genre: " + createFromGenre(aTvDetail.getGenres()) + "\n";
    overview += "Rating: " + aTvDetail.getVoteAverage() + " (" + aTvDetail.getVoteCount() + ")\n";
    overview += "First air date: " + aTvDetail.getFirstAirDate() + "\n";
    overview += "Last air date: " + aTvDetail.getLastAirDate() + "\n";
    overview += "Seasons: " + aTvDetail.getNumberOfSeasons() + "\n";
    overview += "Episodes: " + aTvDetail.getNumberOfEpisodes() + "\n";
    overview += "Duration: " + aTvDetail.getEpisodeRunTimes() + " Minutes\n";
    overview += "Status: " + aTvDetail.getStatus() + "\n";
    overview += "Homepage: " + aTvDetail.getHomepage() + "\n";
    overview += "Overview: \n" + aTvDetail.getOverview();
    return overview;
  }

  public static String createTitle(String aTitle) {
    String filterTitle;
    if (aTitle.length() > 30) {
      filterTitle = aTitle.substring(0, 30) + "...";
    } else {
      filterTitle = aTitle;
    }
    return filterTitle;
  }

  public static String createTagline(String aTagline) {
    String filterTitle;
    if (aTagline.length() > 55) {
      filterTitle = aTagline.substring(0, 55) + "...";
    } else {
      filterTitle = aTagline;
    }
    return filterTitle;
  }

  public static String createHomepage(String aBaseImdbUrl, String aHomepage, String aImdbId) {

    return aHomepage == null || aHomepage.isEmpty() ? aBaseImdbUrl + aImdbId : aHomepage;
  }

  public static String createBackDropPath(String aBaseImgUrl, String aBackdropPath, String aPosterPath) {
    return aBackdropPath == null ? (aPosterPath == null ? IMG_HOLDER : aBaseImgUrl + aPosterPath) :
        aBaseImgUrl + aBackdropPath;
  }

  public static String createPosterPath(String aBaseImgUrl, String aBackdropPath, String aPosterPath) {
    return aPosterPath == null ? (aBackdropPath == null ? IMG_HOLDER : aBaseImgUrl + aBackdropPath) :
        aBaseImgUrl + aPosterPath;
  }

  public static String createFromGenreId(List<Integer> aGenreIds) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < aGenreIds.size(); i++) {
      int id = aGenreIds.get(i);
      for (EGenre genre : EGenre.values()) {
        if (genre.getGenre() == id) { sb.append(",").append(genre.getDisplayname()); }
      }
    }
    String genre = sb.toString().replaceFirst(",", "");
    if (genre.isEmpty() || genre.length() < 3) { genre = "N/A"; }
    return genre;
  }

  public static String createFromGenre(List<Genre> aGenres) {
    StringBuilder sb = new StringBuilder();
    for (Genre genre : aGenres) { sb.append(",").append(genre.getName()); }
    String genre = sb.toString().replaceFirst(",", "");
    if (genre.isEmpty() || genre.length() < 3) { genre = "N/A"; }
    return genre;
  }

  public static TheMovieDbService createdService(String aBaseUrl) {
    Retrofit retrofit = new Retrofit.Builder().baseUrl(aBaseUrl)
        .addConverterFactory(GsonConverterFactory.create()).build();
    return retrofit.create(TheMovieDbService.class);
  }

  public static Response<ResultMovieDetail> getDetailMovie(String aBaseUrl, int aMovieId, String aApiKey) throws IOException {
    Retrofit retrofit = new Retrofit.Builder().baseUrl(aBaseUrl)
        .addConverterFactory(GsonConverterFactory.create()).build();
    TheMovieDbService service = retrofit.create(TheMovieDbService.class);

    return service.detailMovies(aMovieId, aApiKey).execute();
  }

  public static Response<ResultTvsDetail> getDetailTvs(String aBaseUrl, int aTvId, String aApiKey) throws IOException {
    Retrofit retrofit = new Retrofit.Builder().baseUrl(aBaseUrl)
        .addConverterFactory(GsonConverterFactory.create()).build();
    TheMovieDbService service = retrofit.create(TheMovieDbService.class);

    return service.detailTvs(aTvId, aApiKey).execute();
  }

  public static Response<ResultTvsVideo> getDetailTvsVideo(String aBaseUrl, int aTvId, String aApiKey) throws IOException {
    Retrofit retrofit = new Retrofit.Builder().baseUrl(aBaseUrl)
        .addConverterFactory(GsonConverterFactory.create()).build();
    TheMovieDbService service = retrofit.create(TheMovieDbService.class);

    return service.detailTvsVideo(aTvId, aApiKey).execute();
  }

  // public static Response<DiscoverMovies> getTopRatedMovies(String aBaseUrl, String aApiKey, int aPage) throws IOException {
  //   return getTopRatedMovies(aBaseUrl, aApiKey, aPage, DFL_REGION);
  // }

  // public static Response<DiscoverMovies> getTopRatedMovies(String aBaseUrl, String aApiKey, int aPage, String aRegion) throws IOException {
  //   String region = aRegion != null ? aRegion : "";
  //   int page = aPage != 0 ? aPage : 0;
  //   return getTopRatedMovies(aBaseUrl, aApiKey, page, region);
  // }
  //
  // public static Response<DiscoverMovies> getTopRatedMovies(String aBaseUrl, String aApiKey, String aLanguage, int aPage, String aRegion) throws IOException {
  //   TheMovieDbService service = createdService(aBaseUrl);
  //
  //   int page = aPage != 0 ? aPage : 0;
  //   String region = aRegion != null ? aRegion : "";
  //
  //   return service.topRatedMovies(aApiKey, page, region).execute();
  // }

  public static Response<DiscoverMovies> getTopRatedMovies(String aBaseUrl, String aApiKey, FindMovies aFindMovies) throws IOException {
    TheMovieDbService service = createdService(aBaseUrl);
    return service.topRatedMovies(aApiKey, aFindMovies.getPage(), aFindMovies.getRegion()).execute();
  }

  public static Response<DiscoverTvs> getTopRatedTvs(String aBaseUrl, String aApiKey, FindMovies aFindMovies) throws IOException {
    TheMovieDbService service = createdService(aBaseUrl);
    return service.topRatedTvs(aApiKey, aFindMovies.getPage(), aFindMovies.getRegion()).execute();
  }

  public static Response<DiscoverTvs> getAiringTodayTvs(String aBaseUrl, String aApiKey, FindMovies aFindMovies) throws IOException {
    TheMovieDbService service = createdService(aBaseUrl);
    return service.airingTodayTvs(aApiKey, aFindMovies.getPage(), aFindMovies.getRegion()).execute();
  }

  public static Response<DiscoverTvs> getOnAirTvs(String aBaseUrl, String aApiKey, FindMovies aFindMovies) throws IOException {
    TheMovieDbService service = createdService(aBaseUrl);
    return service.onAirTvs(aApiKey, aFindMovies.getPage(), aFindMovies.getRegion()).execute();
  }

  public static Response<DiscoverMovies> getUpcomingMoviesMovies(String aBaseUrl, String aApiKey, FindMovies aFindMovies) throws IOException {
    TheMovieDbService service = createdService(aBaseUrl);
    return service.upcomingMovies(aApiKey, aFindMovies.getPage(), aFindMovies.getRegion()).execute();
  }

  public static Response<DiscoverMovies> getPopularMovies(String aBaseUrl, String aApiKey, FindMovies aFindMovies) throws IOException {
    TheMovieDbService service = createdService(aBaseUrl);
    return service.popularMovies(aApiKey, aFindMovies.getPage(), aFindMovies.getRegion()).execute();
  }

  public static Response<DiscoverTvs> getPopularTvs(String aBaseUrl, String aApiKey, FindMovies aFindMovies) throws IOException {
    TheMovieDbService service = createdService(aBaseUrl);
    return service.popularTvs(aApiKey, aFindMovies.getPage(), aFindMovies.getRegion()).execute();
  }

  public static Response<DiscoverMovies> getNowPlayingMovies(String aBaseUrl, String aApiKey, FindMovies aFindMovies) throws IOException {
    TheMovieDbService service = createdService(aBaseUrl);
    return service.nowPlayingMovies(aApiKey, aFindMovies.getPage(), aFindMovies.getRegion()).execute();
  }

  public static Response<DiscoverMovies> getSearchMovies(String aBaseUrl, String aApiKey, FindMovies aFindMovies) throws IOException {
    TheMovieDbService service = createdService(aBaseUrl);
    if (aFindMovies.getYear() == 0) {
      return service.searchMovies(aApiKey, aFindMovies.getTitle(), aFindMovies.getPage(), aFindMovies.getRegion()).execute();
    }
    return service.searchMovies(aApiKey, aFindMovies.getTitle(), aFindMovies.getPage(), aFindMovies.getRegion(), aFindMovies.getYear()).execute();
  }

  public static Response<DiscoverTvs> getSearchTvs(String aBaseUrl, String aApiKey, FindMovies aFindMovies) throws IOException {
    TheMovieDbService service = createdService(aBaseUrl);
    if (aFindMovies.getYear() == 0) {
      return service.searchTvs(aApiKey, aFindMovies.getTitle(), aFindMovies.getPage()).execute();
    }
    return service.searchTvs(aApiKey, aFindMovies.getTitle(), aFindMovies.getPage(), aFindMovies.getYear()).execute();
  }
}
