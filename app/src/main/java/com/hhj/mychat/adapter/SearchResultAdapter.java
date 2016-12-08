package com.hhj.mychat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.hhj.mychat.model.SearchResultItem;
import com.hhj.mychat.widget.SearchResultItemView;

import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.SearchResultItemViewHolder> {
    public static final String TAG = "SearchResultAdapter";

    private Context mContext;

    private List<SearchResultItem> mSearchResultItems;

    public SearchResultAdapter(Context context, List<SearchResultItem> items) {
        mContext = context;
        mSearchResultItems = items;
    }

    @Override
    public SearchResultItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SearchResultItemViewHolder(new SearchResultItemView(mContext));
}

    @Override
    public void onBindViewHolder(SearchResultItemViewHolder holder, int position) {
        holder.mSearchResultItemView.bindView(mSearchResultItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mSearchResultItems.size();
    }

    public class SearchResultItemViewHolder extends RecyclerView.ViewHolder {

        private SearchResultItemView mSearchResultItemView;

        public SearchResultItemViewHolder(SearchResultItemView itemView) {
            super(itemView);
            mSearchResultItemView = itemView;
        }
    }
}
