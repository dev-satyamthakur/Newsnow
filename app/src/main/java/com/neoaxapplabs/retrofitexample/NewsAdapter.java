package com.neoaxapplabs.retrofitexample;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private Context context;
    private List<Articles> articles;

    public NewsAdapter(Context context, List<Articles> articles) {
        this.context = context;
        this.articles = articles;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_layout, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        Articles article = articles.get(position);
        holder.head.setText(article.getTitle());
        holder.desc.setText(article.getDescription());
        holder.sourceName.setText(article.getSource().getName());
        Glide.with(context)
                .load(article.getUrlToImage())
                .placeholder(R.drawable.img)
                .transition(DrawableTransitionOptions.withCrossFade(1200))
                .into(holder.image_thumb);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(article.getUrl()));
                context.startActivity(browserIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder{

      ImageView image_thumb;
      TextView head, desc, author, sourceName;

      public NewsViewHolder(View itemView){
          super(itemView);
          image_thumb = itemView.findViewById(R.id.image_thumb);
          head = itemView.findViewById(R.id.tv_head);
          desc = itemView.findViewById(R.id.tv_desc);
          sourceName = itemView.findViewById(R.id.tv_sourceName);

      }

    }

}
