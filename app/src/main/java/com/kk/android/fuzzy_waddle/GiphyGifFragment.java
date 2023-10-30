package com.kk.android.fuzzy_waddle;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.kk.android.fuzzy_waddle.model.GiphyImage;
import com.kk.android.fuzzy_waddle.net.GiphyGifLoader;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.loader.content.Loader;

public class GiphyGifFragment extends GiphyFragment implements View.OnClickListener {

    @Override
    public @NonNull
    Loader<List<GiphyImage>> onCreateLoader(int id, Bundle args) {
        return new GiphyGifLoader(getActivity(), searchString);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
        View view = super.onCreateView(inflater, viewGroup, bundle);
        ((SearchView) view.findViewById(R.id.gifSearchBar))
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
        view.findViewById(R.id.privacy_policy).setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View v) {
        startActivity(new Intent(getContext(),PrivacyPolicyActivity.class));
    }
}
