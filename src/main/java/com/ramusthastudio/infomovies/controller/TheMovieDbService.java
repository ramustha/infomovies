package com.ramusthastudio.infomovies.controller;

import com.ramusthastudio.infomovies.model.DiscoverMovies;
import com.ramusthastudio.infomovies.model.DiscoverTvs;
import com.ramusthastudio.infomovies.model.ResultMovieDetail;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TheMovieDbService {
  @GET("discover/movie")
  Call<DiscoverMovies> discoverMovies(@Query("api_key") String aApi, @Query("primary_release_year") int aYear);

  @GET("discover/movie")
  Call<DiscoverMovies> discoverMovies(@Query("api_key") String aApi, @Query("primary_release_date.gte") int aGte, @Query("primary_release_date.lte") int alte);

  @GET("discover/tv")
  Call<DiscoverTvs> discoverTvs(@Query("api_key") String aApi, @Query("primary_release_year") int aYear);

  @GET("discover/tv")
  Call<DiscoverTvs> discoverTvs(@Query("api_key") String aApi, @Query("primary_release_date.gte") int aGte, @Query("primary_release_date.lte") int alte);

  @GET("movie/{id}")
  Call<ResultMovieDetail> detailMovies(@Path("id") int aMovieId, @Query("api_key") String aApi);

  // @GET("tv/{id}")
  // Call<DiscoverTvs> detailTv(@Path("id") int groupId, @Query("api_key") String aApi);

  @GET("movie/now_playing")
  Call<DiscoverMovies> nowPlayingMovies(@Query("api_key") String aApi);

  @GET("tv/on_the_air")
  Call<DiscoverTvs> onTheAirTv(@Query("api_key") String aApi);
}
