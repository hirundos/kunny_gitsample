package com.example.kunny_gitsample.ui.search;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.kunny_gitsample.R;
import com.example.kunny_gitsample.api.GithubApi;
import com.example.kunny_gitsample.api.GithubApiProvider;
import com.example.kunny_gitsample.api.model.GithubRepo;
import com.example.kunny_gitsample.api.model.RepoSearchResponse;
import com.example.kunny_gitsample.ui.repo.RepositoryActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity implements SearchAdapter.ItemClickListener {

    RecyclerView rvList;
    ProgressBar progress;
    TextView tvMessage;
    MenuItem menuSearch;
    SearchView searchView;

    SearchAdapter adapter;

    GithubApi api;

    Call<RepoSearchResponse> searchCall;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        rvList = findViewById(R.id.rvActivitySearchList);
        progress = findViewById(R.id.pbActivitySearch);
        tvMessage = findViewById(R.id.tvActivitySearchMessage);

        adapter = new SearchAdapter();
        adapter.setItemClickListener(this);
        rvList.setLayoutManager(new LinearLayoutManager(this));
        rvList.setAdapter(adapter);

        api = GithubApiProvider.provideGithubApi(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_search, menu);
        menuSearch = menu.findItem(R.id.menu_activity_search_query);

        searchView = (SearchView) menuSearch.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                updateTitle(query);
                hideSoftKeyboard();
                collapseSearchView();
                searchRepository(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        menuSearch.expandActionView();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (R.id.menu_activity_search_query == item.getItemId()) {
            item.expandActionView();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(GithubRepo repository) {
        Intent intent = new Intent(this, RepositoryActivity.class);
        intent.putExtra(RepositoryActivity.KEY_USER_LOGIN, repository.owner.login);
        intent.putExtra(RepositoryActivity.KEY_REPO_NAME, repository.name);
        startActivity(intent);
    }

    private void searchRepository(String query) {
        clearResults();
        hideError();
        showProgress();

        searchCall = api.searchRepository(query);
        searchCall.enqueue(new Callback<RepoSearchResponse>() {
            @Override
            public void onResponse(Call<RepoSearchResponse> call,
                                   Response<RepoSearchResponse> response) {
                hideProgress();

                RepoSearchResponse searchResult = response.body();
                if (response.isSuccessful() && null != searchResult) {
                    adapter.setItems(searchResult.items);
                    adapter.notifyDataSetChanged();

                    if (0 == searchResult.totalCount) {
                        showError(getString(R.string.no_search_result));
                    }
                } else {
                    showError("Not successful: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<RepoSearchResponse> call, Throwable t) {
                hideProgress();
                showError(t.getMessage());
            }
        });
    }

    private void updateTitle(String query) {
        ActionBar ab = getSupportActionBar();
        if (null != ab) {
            ab.setSubtitle(query);
        }
    }

    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
    }

    private void collapseSearchView() {
        menuSearch.collapseActionView();
    }

    private void clearResults() {
        adapter.clearItems();
        adapter.notifyDataSetChanged();
    }

    private void showProgress() {
        progress.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        progress.setVisibility(View.GONE);
    }

    private void showError(String message) {
        tvMessage.setText(message);
        tvMessage.setVisibility(View.VISIBLE);
    }

    private void hideError() {
        tvMessage.setText("");
        tvMessage.setVisibility(View.GONE);
    }
}