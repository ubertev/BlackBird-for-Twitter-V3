package com.example.edwardst4.blackbirdv3;


import android.app.ListActivity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twitter.sdk.android.tweetui.SearchTimeline;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;

public class SearchTimelineFragment extends ListFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final SearchTimeline searchTimeline = new SearchTimeline.Builder()
                .query("#android")
                .build();
        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(getActivity())
                .setTimeline(searchTimeline)
                .build();
        setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.search_activity, container, false);
    }
}
