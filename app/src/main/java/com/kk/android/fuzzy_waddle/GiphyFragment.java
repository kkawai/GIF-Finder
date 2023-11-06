package com.kk.android.fuzzy_waddle;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kk.android.fuzzy_waddle.model.GiphyImage;
import com.kk.android.fuzzy_waddle.net.GiphyLoader;
import com.kk.android.fuzzy_waddle.util.AsyncTask;
import com.kk.android.fuzzy_waddle.util.InfiniteScrollListener;
import com.kk.android.fuzzy_waddle.util.ViewUtil;

import java.util.LinkedList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class GiphyFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<GiphyImage>>,
        GiphyAdapter.OnGiphyImageClickListener, View.OnClickListener {

    private static final String TAG = GiphyFragment.class.getSimpleName();

    private GiphyAdapter giphyAdapter;
    private RecyclerView recyclerView;
    private ProgressBar loadingProgress;
    private TextView noResultsView;
    private GiphyAdapter.OnGiphyImageClickListener listener;

    protected String searchString;

    @Override
    public @NonNull
    Loader<List<GiphyImage>> onCreateLoader(int id, Bundle args) {
        return new GiphyLoader(requireActivity(), searchString);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
        ViewGroup container = ViewUtil.inflate(inflater, viewGroup, R.layout.giphy_fragment);
        recyclerView = ViewUtil.findById(container, R.id.giphy_list);
        loadingProgress = ViewUtil.findById(container, R.id.loading_progress);
        noResultsView = ViewUtil.findById(container, R.id.no_results);
        ((SearchView) container.findViewById(R.id.gifSearchBar))
                .setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        if (query.length() < 2) {
                            return false;
                        }
                        setSearchString(query);
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        return false;
                    }
                });
        container.findViewById(R.id.privacy_policy).setOnClickListener(this);
        return container;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle bundle) {
        super.onViewCreated(view, bundle);

        giphyAdapter = new GiphyAdapter(view.getContext(), new LinkedList<>());
        giphyAdapter.setListener(this);

        setLayoutManager(true); //true -> use grid layout
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(giphyAdapter);
        recyclerView.addOnScrollListener(new GiphyScrollListener(this, giphyAdapter));

        LoaderManager.getInstance(this).initLoader(0, null, this);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<GiphyImage>> loader, @NonNull List<GiphyImage> data) {
        if (isDetached() || isRemoving()) return;
        loadingProgress.setVisibility(View.GONE);

        if (data.isEmpty()) noResultsView.setVisibility(View.VISIBLE);
        else noResultsView.setVisibility(View.GONE);

        giphyAdapter.setImages(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<GiphyImage>> loader) {
        noResultsView.setVisibility(View.GONE);
        giphyAdapter.setImages(new LinkedList<>());
    }

    public void setLayoutManager(boolean gridLayout) {
        recyclerView.setLayoutManager(getLayoutManager(gridLayout));
    }

    private RecyclerView.LayoutManager getLayoutManager(boolean gridLayout) {
        return gridLayout ? new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                : new LinearLayoutManager(getActivity());
    }

    public void setClickListener(GiphyAdapter.OnGiphyImageClickListener listener) {
        this.listener = listener;
    }

    public void setSearchString(@Nullable String searchString) {
        this.searchString = searchString;
        this.noResultsView.setVisibility(View.GONE);
        LoaderManager.getInstance(this).restartLoader(0, null, this);
    }

    @Override
    public void onGiphyImageClicked(GiphyImage giphyImage) {
        if (listener != null) {
            listener.onGiphyImageClicked(giphyImage);
        }
    }

    private static class GiphyScrollListener extends InfiniteScrollListener {

        private Fragment fragment;
        private GiphyAdapter giphyAdapter;

        GiphyScrollListener(Fragment fragment, GiphyAdapter giphyAdapter) {
            this.fragment = fragment;
            this.giphyAdapter = giphyAdapter;
        }

        @Override
        public void onLoadMore(final int currentPage) {
            final Loader<List<GiphyImage>> loader = LoaderManager.getInstance(fragment).getLoader(0);
            if (loader == null) return;

            new AsyncTask<Void, List<GiphyImage>>() {
                @Override
                protected List<GiphyImage> doInBackground(Void... params) {
                    return ((GiphyLoader) loader).loadPage(currentPage * Constants.PAGE_SIZE);
                }

                protected void onPostExecute(List<GiphyImage> images) {
                    if (fragment.isDetached() || fragment.isRemoving()) return;
                    giphyAdapter.addImages(images);
                }
            }.execute();
        }
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(getContext(), PrivacyPolicyActivity.class));
    }
}
