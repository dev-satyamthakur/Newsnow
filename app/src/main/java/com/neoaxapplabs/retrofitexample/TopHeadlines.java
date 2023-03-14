package com.neoaxapplabs.retrofitexample;

import java.util.List;

public class TopHeadlines {

    private String status;

    private Integer totalResults;

    private List<Articles> articles = null;

    public String getStatus() {
        return status;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public List<Articles> getArticles() {
        return articles;
    }
}
