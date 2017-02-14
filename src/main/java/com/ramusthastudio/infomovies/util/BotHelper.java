package com.ramusthastudio.infomovies.util;

import com.linecorp.bot.client.LineMessagingServiceBuilder;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.template.CarouselColumn;
import com.linecorp.bot.model.message.template.CarouselTemplate;
import com.linecorp.bot.model.profile.UserProfileResponse;
import com.linecorp.bot.model.response.BotApiResponse;
import java.io.IOException;
import java.util.List;
import retrofit2.Response;

public final class BotHelper {
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

  public static final String KW_MOVIE_BULAN_INI = "movie bulan ini";
  public static final String KW_SERIES_BULAN_INI = "series bulan ini";

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
    greeting += "Jika kamu ingin tahu daftar movie yang realese bulan ini, tulis 'movie bulan ini'! \n";
    greeting += "Jika kamu ingin tahu daftar series yang realese bulan ini, tulis 'series bulan ini! \n";
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

    sb.toString().replaceFirst(",", "");
    return sb.toString();
  }
}
