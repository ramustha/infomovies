package com.ramusthastudio.infomovies.controller;

import com.ramusthastudio.infomovies.model.DiscoverMovies;
import com.ramusthastudio.infomovies.model.DiscoverTvs;
import com.ramusthastudio.infomovies.model.DiscoverVideosMovies;
import com.ramusthastudio.infomovies.model.ResultMovieDetail;
import com.ramusthastudio.infomovies.model.ResultTvsDetail;
import com.ramusthastudio.infomovies.model.ResultTvsVideo;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TheMovieDbService {

  @GET("/movie/latest")
  Call<ResultMovieDetail> latestMovies(@Query("api_key") String aApi);

  @GET("movie/now_playing")
  Call<DiscoverMovies> nowPlayingMovies(@Query("api_key") String aApi, @Query("page") int aPage, @Query("region") String aRegion);

  @GET("movie/popular")
  Call<DiscoverMovies> popularMovies(@Query("api_key") String aApi, @Query("page") int aPage, @Query("region") String aRegion);

  @GET("movie/top_rated")
  Call<DiscoverMovies> topRatedMovies(@Query("api_key") String aApi, @Query("page") int aPage, @Query("region") String aRegion);

  @GET("movie/upcoming")
  Call<DiscoverMovies> upcomingMovies(@Query("api_key") String aApi, @Query("page") int aPage, @Query("region") String aRegion);

  @GET("movie/{movie_id}/recommendations")
  Call<DiscoverMovies> recommendationsMovies(@Path("movie_id") int aMovieId, @Query("api_key") String aApi, @Query("page") int aPage);

  @GET("movie/{movie_id}/similar")
  Call<DiscoverMovies> similarMovies(@Path("movie_id") int aMovieId, @Query("api_key") String aApi, @Query("page") int aPage);

  @GET("movie/{movie_id}/videos")
  Call<DiscoverVideosMovies> videoMovies(@Path("movie_id") int aMovieId, @Query("api_key") String aApi);

  @GET("movie/{movie_id}")
  Call<ResultMovieDetail> detailMovies(@Path("movie_id") int aMovieId, @Query("api_key") String aApi);

  @GET("search/movie")
  Call<DiscoverMovies> searchMovies(@Query("api_key") String aApi, @Query("query") String aQuery, @Query("page") int aPage, @Query("region") String aRegion);

  @GET("search/movie")
  Call<DiscoverMovies> searchMovies(@Query("api_key") String aApi, @Query("query") String aQuery, @Query("page") int aPage, @Query("region") String aRegion, @Query("primary_release_year") int aReleaseYear);

  @GET("tv/popular")
  Call<DiscoverTvs> popularTvs(@Query("api_key") String aApi, @Query("page") int aPage, @Query("region") String aRegion);

  @GET("tv/airing_today")
  Call<DiscoverTvs> airingTodayTvs(@Query("api_key") String aApi, @Query("page") int aPage, @Query("region") String aRegion);

  @GET("tv/on_the_air")
  Call<DiscoverTvs> onAirTvs(@Query("api_key") String aApi, @Query("page") int aPage, @Query("region") String aRegion);

  @GET("tv/top_rated")
  Call<DiscoverTvs> topRatedTvs(@Query("api_key") String aApi, @Query("page") int aPage, @Query("region") String aRegion);

  @GET("tv/{tv_id}")
  Call<ResultTvsDetail> detailTvs(@Path("tv_id") int aMovieId, @Query("api_key") String aApi);

  @GET("tv/{tv_id}/videos")
  Call<ResultTvsVideo> detailTvsVideo(@Path("tv_id") int aMovieId, @Query("api_key") String aApi);

  @GET("search/tv")
  Call<DiscoverTvs> searchTvs(@Query("api_key") String aApi, @Query("query") String aQuery, @Query("page") int aPage);

  @GET("search/tv")
  Call<DiscoverTvs> searchTvs(@Query("api_key") String aApi, @Query("query") String aQuery, @Query("page") int aPage, @Query("first_air_date_year") int aFirstAirDateYear);


  // @GET("discover/movie")
  // Call<DiscoverMovies> discoverMovies(@Query("api_key") String aApi, @Query("primary_release_year") int aYear);
  //
  // @GET("discover/movie")
  // Call<DiscoverMovies> discoverMovies(@Query("api_key") String aApi, @Query("primary_release_date.gte") int aGte, @Query("primary_release_date.lte") int alte);
  //
  // @GET("discover/tv")
  // Call<DiscoverTvs> discoverTvs(@Query("api_key") String aApi, @Query("primary_release_year") int aYear);
  //
  // @GET("tv/on_the_air")
  // Call<DiscoverTvs> onTheAirTv(@Query("api_key") String aApi);
  //
  // // @GET("tv/{id}")
  // // Call<DiscoverTvs> detailTv(@Path("id") int groupId, @Query("api_key") String aApi);
  //
  // @GET("discover/tv")
  // Call<DiscoverTvs> discoverTvs(@Query("api_key") String aApi, @Query("primary_release_date.gte") int aGte, @Query("primary_release_date.lte") int alte);
}
