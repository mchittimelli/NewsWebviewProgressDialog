package com.example.newswebviewresponsive;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
EditText search;
Button buttonSearch;
ConnectivityManager connectivityManager;
boolean isConnected;
ArrayList<Article> articleArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        search=findViewById(R.id.topic);
        buttonSearch=findViewById(R.id.button);
        final GetDataService service=RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        buttonSearch.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        connectivityManager=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNet=connectivityManager.getActiveNetworkInfo();
        isConnected = activeNet!=null && activeNet.isConnectedOrConnecting();

        String query=search.getText().toString();
        Log.d("My Query",query);

        if (query.equals("")){
            Toast.makeText(getApplicationContext(),"Please Enter a Keyword",Toast.LENGTH_LONG).show();
        }
        else{
            if (!isConnected){
                Toast.makeText(getApplicationContext(),"Please check Connection",Toast.LENGTH_LONG).show();
            }
            else {
                Call<News> newsCall=service.getAllArticles(query,getResources().getString(R.string.api_key));
                newsCall.enqueue(new Callback<News>() {
                    @Override
                    public void onResponse(Call<News> call, Response<News> response) {
                        Log.d("Response fetched ",response.body().toString());
                        News news=response.body();
                        articleArrayList=new ArrayList<>(news.getArticles());
                        Log.d("Array size ", String.valueOf(articleArrayList.size()));

                        int size=articleArrayList.size();
                        if(size!=0){
                            for (int i=0;i<articleArrayList.size();i++){
                                if(!articleArrayList.get(i).getSource().getName().contains("Engadget")&& !articleArrayList.get(i).getSource().getName().contains("Lifehacker.com")){
                                    Intent intent=new Intent(MainActivity.this,NewsActivity.class);
                                    intent.putExtra("url",articleArrayList.get(i).getUrl());
                                    startActivity(intent);
                                    break;
                                }
                            }
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"failure near intent",Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<News> call, Throwable t) {
                    Log.d("in failure ",t.getMessage());
                    }
                });
            }
        }

    }
});



    }
}
