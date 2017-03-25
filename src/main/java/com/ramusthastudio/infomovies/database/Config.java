package com.ramusthastudio.infomovies.database;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class Config {
  @Bean(name = "com.linecorp.channel_secret")
  public String getChannelSecret() { return System.getenv("com.linecorp.channel_secret"); }

  @Bean(name = "com.linecorp.channel_access_token")
  public String getChannelAccessToken() { return System.getenv("com.linecorp.channel_access_token"); }

  @Bean public String getApiKey() {
    return System.getenv("com.themoviedb.api_key");
  }
  @Bean public String getBaseUrl() {
    return System.getenv("com.themoviedb.base_url");
  }
  @Bean public String getBaseImdbUrl() { return System.getenv("com.themoviedb.base_imdb_url"); }
  @Bean public String getBaseVideoUrl() {
    return System.getenv("com.themoviedb.base_video_url");
  }
  @Bean public String getBaseImgUrl() {
    return System.getenv("com.themoviedb.base_img_url");
  }
}
