package com.ramusthastudio.infomovies.util;

import com.linecorp.bot.client.LineMessagingServiceBuilder;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.profile.UserProfileResponse;
import com.linecorp.bot.model.response.BotApiResponse;
import java.io.IOException;
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
    String greeting = "Hi " + userProfile.getDisplayName() + "Selamat Datang di Info Movies\n";
    greeting += "Terima kasih telah menambahkan saya sebagai teman! (happy)\n\n";
    greeting += "Jika kamu menerima terlalu banyak pemberitahuan, ";
    greeting += "Silahkan buka pengaturan dari ruang obrolan ini dan matikan Pemberitahuan. ";
    TextMessage textMessage = new TextMessage(greeting);
    PushMessage pushMessage = new PushMessage(aUserId, textMessage);
    LineMessagingServiceBuilder
        .create(aChannelAccessToken)
        .build()
        .pushMessage(pushMessage)
        .execute();
  }
}
