package com.example.kunny_gitsample.ui.search;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.kunny_gitsample.GlideApp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kunny_gitsample.R;
import com.example.kunny_gitsample.api.model.GithubRepo;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.RepositoryHolder> {

    private List<GithubRepo> items = new ArrayList<>();

    private ColorDrawable placeholder = new ColorDrawable(Color.GRAY);

    @Nullable
    private ItemClickListener listener;

    @NonNull
    @NotNull
    @Override
    public SearchAdapter.RepositoryHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new RepositoryHolder(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SearchAdapter.RepositoryHolder holder, int position) {
        final GithubRepo repo = items.get(position);

        GlideApp.with(holder.itemView.getContext())
                .load(repo.owner.avatarUrl)
                .placeholder(placeholder)
                .into(holder.ivProfile);

        holder.tvName.setText(repo.fullName);
        holder.tvLanguage.setText(TextUtils.isEmpty(repo.language)
                ? holder.itemView.getContext().getText(R.string.no_language_specified)
                : repo.language);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != listener) {
                    listener.onItemClick(repo);
                }
            }
        });
    }

    @Override
    public int getItemCount()  {
        return items.size();
    }

    public void setItems(@NonNull List<GithubRepo> items) {
        this.items = items;
    }

    public void setItemClickListener(@Nullable ItemClickListener listener) {
        this.listener = listener;
    }

    public void clearItems() {
        this.items.clear();
    }

    static class RepositoryHolder extends RecyclerView.ViewHolder {

        ImageView ivProfile;
        TextView tvName;
        TextView tvLanguage;

        RepositoryHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_repository, parent, false));

            ivProfile = itemView.findViewById(R.id.ivItemRepositoryProfile);
            tvName = itemView.findViewById(R.id.tvItemRepositoryName);
            tvLanguage = itemView.findViewById(R.id.tvItemRepositoryLanguage);
        }
    }

    public interface ItemClickListener {

        void onItemClick(GithubRepo repository);
    }
}
