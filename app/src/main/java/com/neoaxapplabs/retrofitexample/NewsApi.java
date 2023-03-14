package com.neoaxapplabs.retrofitexample;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class NewsApi {

    public static final String API_KEY = "446ee53c08bf4f919ab281ae4bac28f7";
    public static final String BASE_URL = "https://newsapi.org/v2/";

    public static TopHeadlineServices topHeadlineServices;

    public static TopHeadlineServices getTopHeadlineServices(){

        if (topHeadlineServices == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            topHeadlineServices = retrofit.create(TopHeadlineServices.class);
        }
        return topHeadlineServices;

    }

    interface TopHeadlineServices {
        @GET("top-headlines/?apiKey=" + API_KEY)
       Call<TopHeadlines> getTopHeadlines(@Query("country") String country, @Query("page") int page);

        @GET("top-headlines?country=in&category=science&apiKey=" + API_KEY)
        Call<TopHeadlines> getScienceHeadlines(@Query("page") int page);
    }

}
