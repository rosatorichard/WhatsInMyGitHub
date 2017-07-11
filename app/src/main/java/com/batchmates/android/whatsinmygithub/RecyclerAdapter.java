package com.batchmates.android.whatsinmygithub;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android on 7/10/2017.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{

List<GithubHolderPOJO> list= new ArrayList<>();
    private URL url;
    private Bitmap bm;
    private String s;
    private ViewHolder holdest;

    public RecyclerAdapter(List<GithubHolderPOJO> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.repo_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        GithubHolderPOJO githubRepo=list.get(position);

        s=githubRepo.getAvatar().toString();
        //holder.avatar.setText(githubRepo.getAvatar().toString());

        holdest=holder;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    url = new URL(s);
                    bm = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
        holder.avatar.setImageBitmap(bm);
        holder.name.setText(githubRepo.getName().toString());
        holder.url.setText(githubRepo.getUrl().toString());
        holder.followers.setText(githubRepo.getFollowers().toString());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView avatar;
        TextView name;
        TextView url;
        TextView followers;
        public ViewHolder(View itemView) {
            super(itemView);
            avatar=(ImageView) itemView.findViewById(R.id.ivAvatar);
            name=(TextView)itemView.findViewById(R.id.tvName);
            url=(TextView)itemView.findViewById(R.id.tvURL);
            followers=(TextView)itemView.findViewById(R.id.tvFollowers);
        }
    }


}
