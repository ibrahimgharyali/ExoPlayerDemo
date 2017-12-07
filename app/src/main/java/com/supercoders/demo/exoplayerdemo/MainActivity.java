package com.supercoders.demo.exoplayerdemo;

import android.content.pm.PackageManager;
import android.media.MediaCodec;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.extractor.mp4.Mp4Extractor;
import com.google.android.exoplayer2.mediacodec.MediaCodecSelector;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.MediaCodecVideoRenderer;
import com.google.android.exoplayer2.video.VideoRendererEventListener;

import java.security.Permission;

public class MainActivity extends AppCompatActivity implements VideoRendererEventListener {

    private static final String TAG = "MainActivity";
    private static final int REQUEST_WRITE_PERMISSION = 345;
    private SimpleExoPlayerView simpleExoPlayerView;
    private SimpleExoPlayer player;
    private TextView resolutionTextView;
    boolean durationSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermission();
        resolutionTextView = new TextView(this);
        resolutionTextView = (TextView) findViewById(R.id.resolution_textView);

// 1. Create a default TrackSelector
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

// 2. Create a default LoadControl
        LoadControl loadControl = new DefaultLoadControl();

// 3. Create the player
        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);
        simpleExoPlayerView = new SimpleExoPlayerView(this);
        simpleExoPlayerView = (SimpleExoPlayerView) findViewById(R.id.player_view);

//Set media controller
        simpleExoPlayerView.setUseController(true);
        simpleExoPlayerView.setShowMultiWindowTimeBar(true);
        simpleExoPlayerView.requestFocus();

// Bind the player to the view.
        simpleExoPlayerView.setPlayer(player);


//
    }//End of onCreate

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
        } else {
            fetchResourceAndStart();
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            fetchResourceAndStart();
        }
    }
    private void fetchResourceAndStart() {
//        I. ADJUST HERE:
//CHOOSE CONTENT: LiveStream / SdCard

//LIVE STREAM SOURCE: * Livestream links may be out of date so find any m3u8 files online and replace:

//        Uri mp4VideoUri2 =Uri.parse("http://81.7.13.162/hls/ss1/index.m3u8"); //random 720p source
//        Uri mp4VideoUri =Uri.parse("http://54.255.155.24:1935//Live/_definst_/amlst:sweetbcha1novD235L240P/playlist.m3u8"); //Radnom 540p indian channel
//
// Uri mp4VideoUri =Uri.parse("FIND A WORKING LINK ABD PLUg INTO HERE"); //PLUG INTO HERE<------------------------------------------


//VIDEO FROM SD CARD: (2 steps. set up file and path, then change videoSource to get the file)
        String urimp1 = "/Download/video/s_1.mp4"; //upload file to device and add path/name.mp4
        String urimp2 = "/Download/video/s_2.3gp"; //upload file to device and add path/name.mp4
        String urimp3 = "/Download/video/s_3.mp4"; //upload file to device and add path/name.mp4
        String urimp4 = "/Download/video/s_4.mp4"; //upload file to device and add path/name.mp4
//        Uri mp4VideoUri = Uri.parse(Environment.getExternalStorageDirectory().getAbsolutePath()+urimp4);

        /*Uri videoUri1 = Uri.parse(Environment.getExternalStorageDirectory().getAbsolutePath()+urimp1);
        Uri videoUri2 = Uri.parse(Environment.getExternalStorageDirectory().getAbsolutePath()+urimp2);
        Uri videoUri3 = Uri.parse(Environment.getExternalStorageDirectory().getAbsolutePath()+urimp3);
        Uri videoUri4 = Uri.parse(Environment.getExternalStorageDirectory().getAbsolutePath()+urimp4);*/



//Measures bandwidth during playback. Can be null if not required.
        DefaultBandwidthMeter bandwidthMeterA = new DefaultBandwidthMeter();
//Produces DataSource instances through which media data is loaded.
        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "exoplayer2example"), bandwidthMeterA);
//Produces Extractor instances for parsing the media data.
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();


// II. ADJUST HERE:

        String[] uris = new String[]{
                "/Download/video/s_1.mp4",
                "/Download/video/s_2.3gp",
                "/Download/video/s_3.mp4",
                "/Download/video/s_4.mp4"
        };
        Uri[] videoUris = new Uri[uris.length];
        MediaSource[] videoSources = new MediaSource[uris.length];
        for (int i = 0; i < uris.length; i ++ ) {
            String vurl =uris[i];
            videoUris[i] = Uri.parse(Environment.getExternalStorageDirectory().getAbsolutePath() + vurl);
            videoSources[i] = new ExtractorMediaSource(videoUris[i], dataSourceFactory, extractorsFactory, null, null);
        }
//This is the MediaSource representing the media to be played:
//FOR SD CARD SOURCE:
        /*MediaSource videoSource1 = new ExtractorMediaSource(videoUri1, dataSourceFactory, extractorsFactory, null, null);
        MediaSource videoSource2 = new ExtractorMediaSource(videoUri2, dataSourceFactory, extractorsFactory, null, null);
        MediaSource videoSource3 = new ExtractorMediaSource(videoUri3, dataSourceFactory, extractorsFactory, null, null);
        MediaSource videoSource4 = new ExtractorMediaSource(videoUri4, dataSourceFactory, extractorsFactory, null, null);*/


//FOR LIVESTREAM LINK:
//        MediaSource videoSource = new HlsMediaSource(videoUri3, dataSourceFactory, 1, null, null);
//        MediaSource videoSource2 = new HlsMediaSource(videoUri4, dataSourceFactory, 1, null, null);
//        MergingMediaSource mergingMediaSource = new MergingMediaSource(videoSource);
        final ConcatenatingMediaSource mediaSource = new ConcatenatingMediaSource(true,videoSources);


//        final LoopingMediaSource loopingSource = new LoopingMediaSource(mediaSource);

// Prepare the player with the source.
        player.prepare(mediaSource);
//        player.prepare(videoSource1);

        player.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest) {
                Log.v(TAG, "Listener-onTimelineChanged...");
            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
                Log.v(TAG, "Listener-onTracksChanged...");
            }

            @Override
            public void onLoadingChanged(boolean isLoading) {
                Log.v(TAG, "Listener-onLoadingChanged...isLoading:"+isLoading);
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                Log.v(TAG, "Listener-onPlayerStateChanged..." + playbackState);
                if (playbackState == Player.STATE_READY && !durationSet) {
                    long realDurationMillis = player.getDuration();
                    durationSet = true;
                }
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {
                Log.v(TAG, "Listener-onRepeatModeChanged...");
            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                Log.v(TAG, "Listener-onPlayerError...");
                player.stop();
                player.prepare(mediaSource);
                player.setPlayWhenReady(true);
            }

            @Override
            public void onPositionDiscontinuity() {
                Log.v(TAG, "Listener-onPositionDiscontinuity...");
            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
                Log.v(TAG, "Listener-onPlaybackParametersChanged...");
            }
        });

        player.setPlayWhenReady(true); //run file/link when ready to play.
        player.setVideoDebugListener(this); //for listening to resolution change and  outputing the resolution
    }

    @Override
    public void onVideoEnabled(DecoderCounters counters) {

    }

    @Override
    public void onVideoDecoderInitialized(String decoderName, long initializedTimestampMs, long initializationDurationMs) {

    }

    @Override
    public void onVideoInputFormatChanged(Format format) {

    }

    @Override
    public void onDroppedFrames(int count, long elapsedMs) {

    }

    @Override
    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
        Log.v(TAG, "onVideoSizeChanged ["  + " width: " + width + " height: " + height + "]");
        resolutionTextView.setText("RES:(WxH):" + width + "X" + height + "   " + height + "p");
    }

    @Override
    public void onRenderedFirstFrame(Surface surface) {

    }

    @Override
    public void onVideoDisabled(DecoderCounters counters) {

    }






//-------------------------------------------------------ANDROID LIFECYCLE---------------------------------------------------------------------------------------------

    @Override
    protected void onStop() {
        super.onStop();
        Log.v(TAG, "onStop()...");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v(TAG, "onStart()...");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(TAG, "onResume()...");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(TAG, "onPause()...");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "onDestroy()...");
        player.release();
    }

}
