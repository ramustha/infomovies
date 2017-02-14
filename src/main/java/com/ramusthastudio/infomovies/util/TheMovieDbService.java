package com.ramusthastudio.infomovies.util;

import com.ramusthastudio.infomovies.model.DiscoverMovies;
import com.ramusthastudio.infomovies.model.DiscoverTvs;
import retrofit2.Call;
import retrofit2.http.GET;
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
}
