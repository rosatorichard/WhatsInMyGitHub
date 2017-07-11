package com.batchmates.android.whatsinmygithub;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Main Activity";
    private static final String BASE_URL = "https://api.github.com/";
    private EditText editText;
    private ImageView avatar;
    private TextView profileName,repoURL, fourthElement, fifthElement;
    private URL url;
    private Bitmap bm;
    private Response<List<GithubRepo>> responseHere;
    private MyReciever myReciever;
    private String s="rosatorichard";
    private BottomNavigationView bottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText=(EditText)findViewById(R.id.etUsername);
        avatar=(ImageView)findViewById(R.id.imAvatar);
        profileName=(TextView)findViewById(R.id.profileName);
        repoURL=(TextView)findViewById(R.id.repoURL);
        fourthElement=(TextView)findViewById(R.id.fourthElement);
        fifthElement=(TextView)findViewById(R.id.fifthElement);

        bottom=(BottomNavigationView)findViewById(R.id.bottom_navigation);

        bottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Toast.makeText(getApplicationContext(),"You Clicked "+item.getTitle(),Toast.LENGTH_SHORT).show();
                return true;
            }
        });


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setShowHideAnimationEnabled(true);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);

    }

    public void showProfile(View view) {

        s=String.valueOf(editText.getText());
        Log.d(TAG, "showProfile: "+ s);
        if(s.length()<=1)
        {
            s="rosatorichard";
        }

        Retrofit retro= new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        MyGitHubInterface gitHubInterface= retro.create(MyGitHubInterface.class);



        retrofit2.Call<List<GithubRepo>> callToGetRepos=gitHubInterface.callProfile(s);

        Log.d(TAG, "valid "+callToGetRepos);
        //need to implement for non valid repository
        if(callToGetRepos!=null) {

            callToGetRepos.enqueue(new Callback<List<GithubRepo>>() {
                @Override
                public void onResponse(Call<List<GithubRepo>> call, Response<List<GithubRepo>> response) {

                    for (int i = 0; i < 1; i++) {

                        responseHere = response;
//                        String s = response.body().get(i).getOwner().getAvatarUrl();

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String s = responseHere.body().get(0).getOwner().getAvatarUrl();
                                try {
                                    url = new URL(s);
                                    bm = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                                    Log.d(TAG, "run: Inside the Thread" + bm);
                                    Intent intent = new Intent("Picture");
                                    sendBroadcast(intent);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
//                            avatar.setImageBitmap(bm);
                            }
                        }).start();
//                    try {
//                        url= new URL(s);
//                        bm= BitmapFactory.decodeStream(url.openConnection().getInputStream());
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    avatar.setImageBitmap(bm);
                        profileName.setText("Profile Name: "+response.body().get(i).getOwner().getLogin());
                        repoURL.setText("Repository link: "+response.body().get(i).getOwner().getHtmlUrl());
                        fourthElement.setText("Gists URL: "+response.body().get(i).getOwner().getGistsUrl());
                        fifthElement.setText("Type: "+response.body().get(i).getOwner().getType());

                        Log.d(TAG, "onResponse: " + response.body().get(i).getOwner().getAvatarUrl());
                    }
                }

                @Override
                public void onFailure(Call<List<GithubRepo>> call, Throwable t) {

                }
            });

        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        myReciever=new MyReciever();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("Picture");
        registerReceiver(myReciever,intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(myReciever);
    }

    public void activityTwo(View view) {

        Intent intent=new Intent(this,ListOfRepositories.class);
        intent.putExtra("EXTRA",s);
        startActivity(intent);
    }

    public class MyReciever extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context context, Intent intent) {
            avatar.setImageBitmap(bm);
            Log.d(TAG, "onReceive: MadeIT");
        }
    }

}
