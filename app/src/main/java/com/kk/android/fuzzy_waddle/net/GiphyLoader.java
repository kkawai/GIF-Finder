package com.kk.android.fuzzy_waddle.net;


import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import com.annimon.stream.Stream;
import com.kk.android.fuzzy_waddle.Constants;
import com.kk.android.fuzzy_waddle.model.GiphyImage;
import com.kk.android.fuzzy_waddle.model.GiphyResponse;
import com.kk.android.fuzzy_waddle.util.AsyncLoader;
import com.kk.android.fuzzy_waddle.util.JsonUtils;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

public class GiphyLoader extends AsyncLoader<List<GiphyImage>> {
    private static final String TAG = GiphyLoader.class.getSimpleName();
    private static final String TRENDING_URL = "https://api.giphy.com/v1/gifs/trending?api_key=9DZwwQGAruSKAL9E01vVDyctF3E5OCfL&offset=%d&limit=" + Constants.PAGE_SIZE;

    private static final String SEARCH_URL = "https://api.giphy.com/v1/gifs/search?api_key=9DZwwQGAruSKAL9E01vVDyctF3E5OCfL&offset=%d&limit=" + Constants.PAGE_SIZE + "&q=%s";

    @Nullable
    private final String searchString;

    private final OkHttpClient client;

    public GiphyLoader(@NonNull Context context, @Nullable String searchString) {
        super(context);
        this.searchString = searchString;
        this.client = new OkHttpClient.Builder().build();
    }

    @Override
    public List<GiphyImage> loadInBackground() {
        return loadPage(0);
    }

    public @NonNull
    List<GiphyImage> loadPage(int offset) {
        try {
            String url;

            if (TextUtils.isEmpty(searchString)) url = String.format(TRENDING_URL, offset);
            else url = String.format(SEARCH_URL, offset, Uri.encode(searchString));

            Request request = new Request.Builder().url(url).build();
            Response response = client.newCall(request).execute();

            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            GiphyResponse giphyResponse = JsonUtils.fromJson(response.body().byteStream(), GiphyResponse.class);
            List<GiphyImage> results = Stream.of(giphyResponse.getData())
                    .filterNot(g -> TextUtils.isEmpty(g.getGifUrl()))
                    .filterNot(g -> TextUtils.isEmpty(g.getGifMmsUrl()))
                    .filterNot(g -> TextUtils.isEmpty(g.getStillUrl()))
                    .toList();

            if (results == null) return new LinkedList<>();
            else return results;

        } catch (IOException e) {
            Timber.tag(TAG).w(e);
            return new LinkedList<>();
        }
    }

}
