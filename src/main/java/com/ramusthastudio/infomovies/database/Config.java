package com.ramusthastudio.infomovies.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("classpath:application.properties")
public class Config {
  @Autowired
  Environment mEnv;

  @Bean(name = "com.linecorp.channel_secret")
  public String getChannelSecret() {
    return mEnv.getProperty("com.linecorp.channel_secret");
  }

  @Bean(name = "com.linecorp.channel_access_token")
  public String getChannelAccessToken() {
    return mEnv.getProperty("com.linecorp.channel_access_token");
  }
}
