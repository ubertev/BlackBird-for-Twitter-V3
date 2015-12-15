package com.example.edwardst4.blackbirdv3;

import android.app.ListActivity;
import android.os.Bundle;

import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

public class TimelineActivity extends ListActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timeline);

        //build the timeline by supplying specific user @ name
        //TODO allow for full timeline to be viewed as opposed to single user
        final UserTimeline userTimeline = new UserTimeline.Builder()
                .screenName("ubertev")
                .build();
        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(this)
                .setTimeline(userTimeline)
                .build();
        setListAdapter(adapter);
    }
}
