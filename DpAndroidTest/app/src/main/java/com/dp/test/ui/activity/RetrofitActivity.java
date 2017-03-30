package com.dp.test.ui.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.dp.test.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Url;

public class RetrofitActivity extends AbstractActivity {

    private TextView mTvResult;

    private final Set<HttpUrl> fetchedUrls = Collections.synchronizedSet(
            new LinkedHashSet<HttpUrl>());
    private final ConcurrentHashMap<String, AtomicInteger> hostnames = new ConcurrentHashMap<>();
    private PageService pageService;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);

        mTvResult = (TextView) findViewById(R.id.tv_result);
        runRetrofit("https://www.baidu.com/");
    }

    private void runRetrofit(String url) {
        Dispatcher dispatcher = new Dispatcher(Executors.newFixedThreadPool(20));
        dispatcher.setMaxRequests(20);
        dispatcher.setMaxRequestsPerHost(1);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .dispatcher(dispatcher)
                .connectionPool(new ConnectionPool(100, 30, TimeUnit.SECONDS))
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HttpUrl.parse("https://example.com/"))
                .addConverterFactory(PageAdapter.FACTORY)
                .client(okHttpClient)
                .build();

        pageService = retrofit.create(PageService.class);


        crawlPage(HttpUrl.parse(url));
    }

    public void crawlPage(HttpUrl url) {
        // Skip hosts that we've visited many times.
        AtomicInteger hostnameCount = new AtomicInteger();
        AtomicInteger previous = hostnames.putIfAbsent(url.host(), hostnameCount);
        if (previous != null) hostnameCount = previous;
        if (hostnameCount.incrementAndGet() > 100) return;

        // Asynchronously visit URL.
        pageService.get(url).enqueue(new Callback<Page>() {
            @Override public void onResponse(Call<Page> call, Response<Page> response) {
                if (!response.isSuccessful()) {
                    System.out.println(call.request().url() + ": failed: " + response.code());
                    return;
                }

                // Print this page's URL and title.
                Page page = response.body();
                HttpUrl base = response.raw().request().url();
                System.out.println(base + ": " + page.title);
                mTvResult.setText(page.title);

                // Enqueue its links for visiting.
                for (String link : page.links) {
                    HttpUrl linkUrl = base.resolve(link);
                    if (linkUrl != null && !fetchedUrls.add(linkUrl)) {
                        crawlPage(linkUrl);
                    }
                }
            }

            @Override public void onFailure(Call<Page> call, Throwable t) {
                System.out.println(call.request().url() + ": failed: " + t);
            }
        });
    }

    static class Page {
        final String title;
        final List<String> links;

        Page(String title, List<String> links) {
            this.title = title;
            this.links = links;
        }
    }

    interface PageService {
        @GET
        Call<Page> get(@Url HttpUrl url);
    }

    static final class PageAdapter implements Converter<ResponseBody, Page> {
        static final Converter.Factory FACTORY = new Converter.Factory() {
            @Override public Converter<ResponseBody, ?> responseBodyConverter(
                    Type type, Annotation[] annotations, Retrofit retrofit) {
                if (type == Page.class) return new PageAdapter();
                return null;
            }
        };

        @Override public Page convert(ResponseBody responseBody) throws IOException {
            Document document = Jsoup.parse(responseBody.string());
            List<String> links = new ArrayList<>();
            for (Element element : document.select("a[href]")) {
                links.add(element.attr("href"));
            }
            return new Page(document.title(), Collections.unmodifiableList(links));
        }
    }
}
