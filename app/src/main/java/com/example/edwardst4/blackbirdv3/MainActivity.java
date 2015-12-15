package com.example.edwardst4.blackbirdv3;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.tweetcomposer.ComposerActivity;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import org.w3c.dom.Text;

import java.io.File;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "wNB64xwSHMHGUkv7vHmdlqJFG";
    private static final String TWITTER_SECRET = "UO9VeDfPrROwXd32p6ym2LThjOW0rbCZzAHqm48VnLezDF4v8Q";

    private TwitterLoginButton loginButton;
    private Button searchButton;
    private Button viewUser;
    private TextView textView;
    private Button composeTweet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig), new TweetComposer());
        setContentView(R.layout.activity_main);

        //link views
        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        searchButton = (Button) findViewById(R.id.search_button);
        viewUser = (Button) findViewById(R.id.view_tweets_button);
        textView = (TextView) findViewById(R.id.message);
        composeTweet = (Button) findViewById(R.id.compose);

        //hide search and view user buttons until user is authenticated
        searchButton.setVisibility(View.GONE);
        viewUser.setVisibility(View.GONE);
        composeTweet.setVisibility(View.GONE);

        //generate callback to bring user back to App after authenticating
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // The TwitterSession is also available through:
                // Twitter.getInstance().core.getSessionManager().getActiveSession()
                TwitterSession session = result.data;
                // TODO: Remove toast and use the TwitterSession's userID
                // with your app's user model
                String msg = "@" + session.getUserName() + " logged in! (#" + session.getUserId() + ")";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

                //bring out new buttons and hide login button after logging in usr
                viewUser.setVisibility(View.VISIBLE);
                searchButton.setVisibility(View.VISIBLE);
                loginButton.setVisibility(View.GONE);
                textView.setText(R.string.user_prompt);
                composeTweet.setVisibility(View.VISIBLE);
            }

            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });

        //intent to launch timeline view on button press
        viewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TimelineActivity.class);
                startActivity(intent);
            }
        });

        /*
        * intent to launch the search activity on button press.
        * :(( doesnt work.
        * TODO read up on Twitter Search API
        * */
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SearchTimelineFragment.class);
                startActivity(intent);
            }
        });

        /*
        * on button press, launches an sub-activity that brings up a tweet composition screen
        * for the user to send a tweet
        * */
        composeTweet.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final TwitterSession session = TwitterCore.getInstance().getSessionManager()
                        .getActiveSession();
                final Intent intent = new ComposerActivity.Builder(MainActivity.this)
                        .session(session)
                        .createIntent();
                startActivity(intent);
            }
        });

    }

    /*
    * sends a notification when the user tweets letting them know that their tweet has been sent.
    * */
    public void Notify(View view) {
        //get notification service
        Intent intent = new Intent(this, NotificationView.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

        Notification notification = new Notification.Builder(this)
                .setContentTitle("BlackBird for Twitter")
                .setContentText("Tweet Sent!").setSmallIcon(R.drawable.twitterbox)
                .setContentIntent(pendingIntent).build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(0, notification);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Make sure that the loginButton hears the result from any
        // Activity that it triggered.
        loginButton.onActivityResult(requestCode, resultCode, data);
    }

    //not used for now
    //TODO develop later on
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //TODO complete later
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
