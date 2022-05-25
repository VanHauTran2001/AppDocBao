package com.example.appdocbao.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.appdocbao.Model.Item;
import com.example.appdocbao.R;

public class RssAdapter extends RecyclerView.Adapter<RssAdapter.ViewHolder>{
    private final IRSS irss;

    public RssAdapter(IRSS irss) {
        this.irss = irss;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rss,parent,false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Item item = irss.getData(position);
       holder.txtTitle.setText(item.title);
       holder.txtContent.setText(item.description);
       holder.itemView.setOnClickListener(view -> irss.setOnClickData(position));
    }

    @Override
    public int getItemCount() {
       return irss.getCount();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView txtTitle , txtContent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtContent = itemView.findViewById(R.id.txtContent);
        }
    }
    public interface IRSS{
        int getCount();
        Item getData(int position);
        void setOnClickData(int position);
    }
}
