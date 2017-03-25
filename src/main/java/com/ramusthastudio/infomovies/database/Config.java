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

  @Bean(name = "com.themoviedb.api_key")
  public String getApiKey() {
    return System.getenv("com.themoviedb.api_key");
  }

  @Bean(name = "com.themoviedb.base_url")
  public String getBaseUrl() {
    return System.getenv("com.themoviedb.base_url");
  }

  @Bean(name = "com.themoviedb.base_imdb_url")
  public String getBaseImdbUrl() { return System.getenv("com.themoviedb.base_imdb_url"); }

  @Bean(name = "com.themoviedb.base_video_url")
  public String getBaseVideoUrl() {
    return System.getenv("com.themoviedb.base_video_url");
  }

  @Bean(name = "com.themoviedb.base_img_url")
  public String getBaseImgUrl() {
    return System.getenv("com.themoviedb.base_img_url");
  }
}
