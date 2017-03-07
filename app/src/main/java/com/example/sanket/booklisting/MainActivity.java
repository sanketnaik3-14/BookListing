package com.example.sanket.booklisting;

import android.app.LoaderManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.handle;
import static android.provider.Contacts.SettingsColumns.KEY;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Books>> {

    public static final String LOG_TAG = MainActivity.class.getName();
    ListView listView;
    private String temp = "";
    private ProgressBar progressBar;
    private TextView mEmptyStateTextView;
    private BooksAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar) findViewById(R.id.loading_spinner);
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        mEmptyStateTextView.setText(getString(R.string.enter_your_search));
        progressBar.setVisibility(View.GONE);

        listView = (ListView) findViewById(R.id.list);

        adapter = new BooksAdapter(this, new ArrayList<Books>());
        listView.setAdapter(adapter);

        listView.setEmptyView(mEmptyStateTextView);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();

        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                temp = query.replaceAll("\\s+", "");
                progressBar.setVisibility(View.VISIBLE);
                mEmptyStateTextView.setVisibility(View.GONE);
                ConnectivityManager coMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo info = coMgr.getActiveNetworkInfo();

                if (info != null && info.isConnected()) {
                    LoaderManager loaderManager = getLoaderManager();
                    loaderManager.destroyLoader(1);
                    loaderManager.initLoader(1, null, MainActivity.this);
                } else {
                    progressBar.setVisibility(View.GONE);
                    mEmptyStateTextView.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                    mEmptyStateTextView.setText(R.string.no_internet_connection);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                mEmptyStateTextView.setVisibility(View.GONE);
                return false;
            }
        });

        return true;
    }

    @Override
    public Loader<List<Books>> onCreateLoader(int id, Bundle args) {
        String max = getString(R.string.max_result);
        String url = getString(R.string.book_url) + temp + max;
        Log.i(LOG_TAG, url);
        mEmptyStateTextView.setVisibility(View.GONE);
        return new BooksLoader(this, url);
    }

    @Override
    public void onLoadFinished(Loader<List<Books>> loader, List<Books> data) {

        progressBar.setVisibility(View.GONE);
        adapter.clear();

        if (data != null && !data.isEmpty()) {
            adapter.addAll(data);
        } else {
            mEmptyStateTextView.setText(getString(R.string.books_not_found));
            mEmptyStateTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Books>> loader) {

        adapter.clear();
    }
}
