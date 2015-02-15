package francoj11.youtubenatgeo;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class MainActivity extends YouTubeBaseActivity {

    //String used to log:
    private static final String LOGTAG = "NATGEO";

    //The playlist
	private ArrayList<VideoInfo> playlist = null;

    //The refreshItem button in the ActionBar
	private MenuItem refreshItem = null;

    //RecyclerView
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    //Youtube
    private final String APP_YOUTUBE = "YOUR_YOUTUBE_KEY_HERE";
    private YouTubePlayerView youtubePlayerView;
    private YouTubePlayer player = null;
    private String urlXML = "http://gdata.youtube.com/feeds/base/users/NationalGeographic/uploads?alt=rss&v=2&orderby=published&client=ytapi-youtube-profile";
    private boolean fullScreen = false;
    private boolean isPlaying;
    private int lastVideo;
    private int currentVideoMilisec;

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        Log.d(LOGTAG,"SAVING");
        bundle.putParcelableArrayList("playlist", playlist);
        bundle.putBoolean("fullScreen", fullScreen);
        bundle.putBoolean("isPlaying",isPlaying);
        bundle.putInt("lastVideo",lastVideo);
        bundle.putInt("currentVideoMilisec",currentVideoMilisec);
        super.onSaveInstanceState(bundle);
    }


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

        if (savedInstanceState == null || !savedInstanceState.containsKey("playlist")) {
            playlist = new ArrayList<VideoInfo>();
        } else {
            playlist = savedInstanceState.getParcelableArrayList("playlist");
            fullScreen = savedInstanceState.getBoolean("fullScreen");
            isPlaying = savedInstanceState.getBoolean("isPlaying");
            lastVideo = savedInstanceState.getInt("lastVideo");
            currentVideoMilisec = savedInstanceState.getInt("currentVideoMilisec");
        }

        //Initialize the RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Initialize the YoutubePlayerView
        youtubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_player);
        youtubePlayerView.initialize(APP_YOUTUBE,new YouTubePlayer.OnInitializedListener() {

            /*On succes, save the instance of the YoutubePlayer to play the videos, and cue the
             * first video of the playlist if there is any.*/
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer
                                                youTubePlayer, boolean b) {
                player = youTubePlayer;
                Log.d(LOGTAG,"PLAYERSUCCESS, RESTORING: " + b);

                player.setOnFullscreenListener(new YouTubePlayer.OnFullscreenListener() {
                    @Override
                    public void onFullscreen(boolean b) {
                        fullScreen = b;
                        isPlaying = player.isPlaying();
                        currentVideoMilisec = player.getCurrentTimeMillis();
                    }
                });

                if (playlist.size() > 0 && !b){
                    player.cueVideo(playlist.get(0).getYoutubeLink());
                }

                if (b && isPlaying){
                    player.loadVideo(playlist.get(lastVideo).getYoutubeLink(),currentVideoMilisec);
                }
            }

            /*On failure inform the user about the error*/
            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                YouTubeInitializationResult youTubeInitializationResult) {
                youTubeInitializationResult.getErrorDialog(MainActivity.this,0);
            }
        });

        //On item touch, reproduce the video asociated to the item.
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this,
                                             new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d(LOGTAG,"PRESSED: " + position );
                if (player != null){
                    player.loadVideo(playlist.get(position).getYoutubeLink());
                }
                lastVideo = position;
            }
        }));

        //Download the Youtube feed if the playlist is empty:
        if (playlist.isEmpty()) {
            new DownloadWebpageTask().execute(urlXML);
        } else {
            mAdapter = new MyAdapter(playlist, MainActivity.this);
            mRecyclerView.setAdapter(mAdapter);
        }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		refreshItem = menu.findItem(R.id.action_refresh);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
        if (id == R.id.action_refresh) {
			Log.d(LOGTAG,"REFRESHING");
			new DownloadWebpageTask().execute(urlXML);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

    @Override
    public void onBackPressed() {
        Log.d(LOGTAG,"BACK PRESSSED");
        if (player != null && fullScreen) {
            player.setFullscreen(false);
        } else {
            super.onBackPressed();
        }
    }

    //This task takes a URL string and download its content in the background as an InputStream
    //which is passed to the Parser class to parse and extract the youtube's videos information
    //which are stored in VideoInfo objects and appended to the playlist.
	private class DownloadWebpageTask extends AsyncTask<String, Void, Void> {

        //On PreExecute the refreshItem button starts an indeterminate progressBar
		protected void onPreExecute() {
			Log.d(LOGTAG,"PREEXECUTE");
            if (refreshItem != null) {
                refreshItem.setActionView(R.layout.actionbar_indeterminate_progress);
            }
            playlist.clear();
		}

		@Override
		protected Void doInBackground(String... urls) {
			try {
				Log.d(LOGTAG,"DOWNLOADING FEED");
                downloadUrl(urls[0]);
			} catch (IOException e){
				Log.d(LOGTAG,"ERROR DOWNLOADING FEED");
			}
            return null;
        }

        //On PostExecute the refreshItem button stops the progressBar animation, and the Adapter
        //is created using the now populated playlist.
		protected void onPostExecute(Void v){
			Log.d(LOGTAG,"FINISHED DOWNLOAD");
            mAdapter = new MyAdapter(playlist,MainActivity.this);
            mRecyclerView.setAdapter(mAdapter);

            if (refreshItem != null) {
                refreshItem.setActionView(null);
            }

			System.out.println("----------------------------");
			System.out.println("----------------------------");
			for (VideoInfo videoInfo : playlist) {
				System.out.println(videoInfo);
			}
            System.out.println("----------------------------");
            System.out.println("----------------------------");

            if (player != null){
                player.cueVideo(playlist.get(0).getYoutubeLink());
            }
		}
		
		// Given a URL, establishes an HttpUrlConnection and retrieves
		// the web page content as a InputStream, which it returns as
		// a string.
		private void downloadUrl(String myurl) throws IOException {
		    InputStream is = null;

		    try {
		        URL url = new URL(myurl);
		        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		        conn.setReadTimeout(10000 /* milliseconds */);
		        conn.setConnectTimeout(15000 /* milliseconds */);
		        conn.setRequestMethod("GET");
		        conn.setDoInput(true);
		        // Starts the query
		        conn.connect();
		        int response = conn.getResponseCode();
		        Log.d(LOGTAG, "The response is: " + response);
		        is = conn.getInputStream();

	        	Parser p = new Parser(playlist);
	        	p.parse(is);
	        	
		        return;
		        
		    // Makes sure that the InputStream is closed after the app is
		    // finished using it.
		    } catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			} finally {
		        if (is != null) {
		            is.close();
		        	Log.d(LOGTAG,"INPUTSTREAM CLOSED");
		        } 
		    }
			return;
		}
	}

}
