package com.dp.test.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.dp.test.R;
import com.dp.test.debug.DpDebug;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Url;

public class RetrofitActivity extends AbstractActivity {

    private TextView mTvResult;

    String API_BASE_URL = "https://api.github.com/";

    private static final String BTKITTY_URL = "http://btkitty.bid/";

    OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    Retrofit.Builder builder = new Retrofit.Builder().baseUrl(API_BASE_URL).addConverterFactory(GsonConverterFactory.create());

    Retrofit retrofit = builder.client(httpClient.build()).build();

    GitHubClient client = retrofit.create(GitHubClient.class);

    Call<List<GitHubRepo>> call = client.reposForUser("fs-opensource");

    private BTKittySearchApi mBTKittySearchApi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);
        mTvResult = (TextView) findViewById(R.id.tv_result);
        btKittyInit();
//        run();
//        runBTKittyAsync("girl");
//        runBTKittySync("beauty girl");
//        runBTKittyGetAsync(BTKITTY_URL);
        runBTKittyGetSync(BTKITTY_URL);
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

    public interface BTKittySearchApi {
        @FormUrlEncoded
        @Headers({"User-Agent:Mozilla"})
        @POST("http://btkitty.bid/")
        Call<ResponseBody> mainSearch(@Field("keyword") String keyword);

        @GET
        Call<ResponseBody> subSearch(@Url String url);
    }

    public void btKittyInit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BTKITTY_URL)
                .build();
        mBTKittySearchApi = retrofit.create(BTKittySearchApi.class);
    }

    private void runBTKittyAsync(String keyword) {
//        String body = "";
        Call<ResponseBody> call = mBTKittySearchApi.mainSearch(keyword);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String body = "";
                try {
                    body = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                DpDebug.log("RetrofixActivity ---- runBTKitty ---- onResponse ---- body : " + body);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                DpDebug.log("RetrofixActivity ---- runBTKitty ---- onFailure");
            }
        });
    }

    private void runBTKittySync(final String keyword) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String body = "";
                Call<ResponseBody> call = mBTKittySearchApi.mainSearch(keyword);
                try {
                    Response<ResponseBody> bodyResponse = call.execute();
                    body = bodyResponse.body().string();//获取返回体的字符串
                } catch (IOException e) {
                    DpDebug.log("RetrofixActivity ---- runBTKittySync ---- IOException : " + e.toString());
                    e.printStackTrace();
                }
                DpDebug.log("RetrofixActivity ---- runBTKittySync ---- body : " + body);
            }
        }).start();
    }

    private void runBTKittyGetAsync(final String url) {
        Call<ResponseBody> call = mBTKittySearchApi.subSearch(url);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String body = "";
                try {
                    body = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                DpDebug.log("RetrofixActivity ---- runBTKittyGetAsync ---- onResponse ---- body : " + body);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                DpDebug.log("RetrofixActivity ---- runBTKittyGetAsync ---- onFailure");
            }
        });
    }

    private void runBTKittyGetSync(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String body = "";
                Call<ResponseBody> call = mBTKittySearchApi.subSearch(url);
                try {
                    Response<ResponseBody> bodyResponse = call.execute();
                    body = bodyResponse.body().string();//获取返回体的字符串
                } catch (IOException e) {
                    DpDebug.log("RetrofixActivity ---- runBTKittyGetSync ---- IOException : " + e.toString());
                    e.printStackTrace();
                }
                DpDebug.log("RetrofixActivity ---- runBTKittyGetSync ---- body : " + body);
            }
        }).start();
    }
}
