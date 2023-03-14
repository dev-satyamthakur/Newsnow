package com.neoaxapplabs.retrofitexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ShimmerFrameLayout shimmerFrameLayout;
    LinearLayoutManager manager;
    NewsAdapter adapter;
    List<Articles> articles = new ArrayList<>();
    int totalResults, totalPages;
    int currentItems, scrollOutItems,  totalItems;
    boolean isScrolling = false;
    int currentPageHeadlines = 1;
    int currentPageScience = 1;
    ProgressBar progressBar;
    boolean isScience = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        shimmerFrameLayout = findViewById(R.id.shimmerFrameLayout);
        recyclerView = findViewById(R.id.recylerView);
        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        adapter = new NewsAdapter(MainActivity.this,articles);
        recyclerView.setAdapter(adapter);
        recyclerView.setVisibility(View.GONE);
        shimmerFrameLayout.setVisibility(View.VISIBLE);


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems = manager.getChildCount();
                totalItems = manager.getItemCount();
                scrollOutItems = manager.findFirstVisibleItemPosition();


                    if (isScrolling && (currentItems + scrollOutItems == totalItems)) {
                        isScrolling = false;
                        if (!isScience && currentPageHeadlines <= totalPages) {
                            progressBar.setVisibility(View.VISIBLE);
                            getTopHeadlines(currentPageHeadlines);
                        }
                        if (isScience && currentPageScience <= totalPages) {
                            progressBar.setVisibility(View.VISIBLE);
                            fetchScience(currentPageScience);
                        }

                    }


            }
        });


        getTopHeadlines(currentPageHeadlines);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.mainmenu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.science) {
            articles.removeAll(articles);
            recyclerView.setVisibility(View.GONE);
            shimmerFrameLayout.startShimmer();
            shimmerFrameLayout.setVisibility(View.VISIBLE);
            fetchScience(currentPageScience);

        }

        return super.onOptionsItemSelected(item);
    }

    private void fetchScience(int pageNo) {
        Call<TopHeadlines> topHeadlinesCall = NewsApi.getTopHeadlineServices().getScienceHeadlines(pageNo);
        topHeadlinesCall.enqueue(new Callback<TopHeadlines>() {
            @Override
            public void onResponse(Call<TopHeadlines> call, Response<TopHeadlines> response) {
                articles.addAll(response.body().getArticles());
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                adapter.notifyDataSetChanged();
                currentPageScience += 1;
                isScience = true;
                currentPageHeadlines = 1;

                totalResults = response.body().getTotalResults();
                int pages = totalResults/20;
                if (totalResults % 20 != 0){
                    pages +=1;
                }
                totalPages = pages;
            }

            @Override
            public void onFailure(Call<TopHeadlines> call, Throwable t) {

            }
        });
    }

    private void getTopHeadlines(int pageNo) {
        Call<TopHeadlines> topHeadlinesCall = NewsApi.getTopHeadlineServices().getTopHeadlines("in", pageNo);
        topHeadlinesCall.enqueue(new Callback<TopHeadlines>() {
            @Override
            public void onResponse(Call<TopHeadlines> call, Response<TopHeadlines> response) {
                Log.d("data", "Success");
                TopHeadlines headlines = response.body();
                progressBar.setVisibility(View.GONE);
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                articles.addAll(headlines.getArticles());
                adapter.notifyDataSetChanged();
                currentPageHeadlines +=1;
                isScience = false;
                currentPageScience = 1;

                totalResults = response.body().getTotalResults();
                int pages = totalResults/20;
                if (totalResults % 20 != 0){
                    pages +=1;
                }
                totalPages = pages;

            }

            @Override
            public void onFailure(Call<TopHeadlines> call, Throwable t) {
                Log.d("data", "Error");
            }
        });
    }
}