package com.dp.test.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.dp.test.R;
import java.util.List;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class RetrofitActivity extends AbstractActivity {

    private TextView mTvResult;

    String API_BASE_URL = "https://api.github.com/";

    OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    Retrofit.Builder builder = new Retrofit.Builder().baseUrl(API_BASE_URL).addConverterFactory(GsonConverterFactory.create());

    Retrofit retrofit = builder.client(httpClient.build()).build();

    GitHubClient client = retrofit.create(GitHubClient.class);

    Call<List<GitHubRepo>> call = client.reposForUser("fs-opensource");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);
        mTvResult = (TextView) findViewById(R.id.tv_result);
        run();
    }

    private void run() {
        call.enqueue(new Callback<List<GitHubRepo>>() {
            @Override
            public void onResponse(Call<List<GitHubRepo>> call, Response<List<GitHubRepo>> response) {
                List<GitHubRepo> list = response.body();
                for (GitHubRepo repo : list) {
                    Log.i("ljq", "MainActivity ---- run ---- repo.getId : " + repo.getId() + ", repo.getName : " + repo.getName());
                }
                if (list.size() > 0) {
                    mTvResult.setText(list.get(0).getName());
                }
            }

            @Override
            public void onFailure(Call<List<GitHubRepo>> call, Throwable t) {

            }
        });
    }

    public class GitHubRepo {
        private int id;
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public interface GitHubClient {
        @GET("/users/{user}/repos")
        Call<List<GitHubRepo>> reposForUser(
                @Path("user") String user
        );
    }
}
