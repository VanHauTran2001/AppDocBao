package com.example.appdocbao.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;


import com.example.appdocbao.Activity.NewActivity;
import com.example.appdocbao.Model.Item;
import com.example.appdocbao.R;

import java.util.ArrayList;
import java.util.List;

public class RssAdapter extends RecyclerView.Adapter<RssAdapter.ViewHolder>{
    private List<Item> itemList = new ArrayList<>();
    private Context context;

    public RssAdapter( Context context,List<Item> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_rss,parent,false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Item item = itemList.get(position);
       holder.txtTitle.setText(item.getTitle());
       holder.txtContent.setText(item.getDescription());
       holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, NewActivity.class);
                intent.putExtra("title",item.getTitle());
                intent.putExtra("description",item.getDescription());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView txtTitle , txtContent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtContent = itemView.findViewById(R.id.txtContent);
        }
    }
}
