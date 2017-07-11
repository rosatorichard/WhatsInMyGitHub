package com.batchmates.android.whatsinmygithub;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListOfRepositories extends AppCompatActivity {

    private static final String BASE_URL = "https://api.github.com/";
    private String s;
    private List<GithubHolderPOJO> contactList=new ArrayList<>();
    private RecyclerView recycler;
    private RecyclerAdapter recycleAdapter;
    private DefaultItemAnimator itemAnimator=new DefaultItemAnimator();
    private RecyclerView.LayoutManager layoutManager;
    private BottomNavigationView bottom;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_repositories);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setShowHideAnimationEnabled(true);
        recycler=(RecyclerView)findViewById(R.id.myRecyclerView);
        bottom=(BottomNavigationView)findViewById(R.id.bottom_navigationSecond);

        bottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Toast.makeText(getApplicationContext(),"You Clicked "+item.getTitle(),Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        layoutManager= new GridLayoutManager(this,1, LinearLayoutManager.VERTICAL,false);


        s=getIntent().getStringExtra("EXTRA");

        Retrofit retro= new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        MyGitHubInterface gitInterface= retro.create(MyGitHubInterface.class);

        retrofit2.Call<List<GithubRepo>> callToGetRepo=gitInterface.callProfile(s);

        callToGetRepo.enqueue(new Callback<List<GithubRepo>>() {
            @Override
            public void onResponse(Call<List<GithubRepo>> call, Response<List<GithubRepo>> response) {
                for (int i = 0; i < response.body().size(); i++) {

                contactList.add(new GithubHolderPOJO(response.body().get(i).getOwner().getAvatarUrl(),"Name: "+
                        response.body().get(i).getName(),"URL: "+response.body().get(i).getUrl(),"Followers URL: "+response.body().get(i).getOwner().getFollowersUrl()));

                }
                recycleAdapter=new RecyclerAdapter(contactList);
                recycler.setLayoutManager(layoutManager);
                recycler.setItemAnimator(itemAnimator);
                recycler.setAdapter(recycleAdapter);
            }

            @Override
            public void onFailure(Call<List<GithubRepo>> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}
