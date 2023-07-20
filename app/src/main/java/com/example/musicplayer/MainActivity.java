package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Context;
    import android.content.DialogInterface;
    import android.content.Intent;
    import android.media.AudioManager;
    import android.media.MediaPlayer;
    import android.os.Handler;
    import android.os.Message;
    import android.os.Bundle;
    import android.util.Log;
    import android.view.View;
    import android.widget.ImageView;
    import android.widget.MediaController;
    import android.widget.SeekBar;
    import android.widget.TextView;
    import android.widget.Toast;

import java.util.ArrayList;

    public class MainActivity extends AppCompatActivity {

        ImageView play, prev, next, imageView;
        TextView songTitle;
        SeekBar mSeekBarTime, mSeekBarVol;
        static MediaPlayer mMediaPlayer;
        private Runnable runnable;
        private AudioManager mAudioManager;
        int currentIndex = 0;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

            // initializing views

            play = findViewById(R.id.play);
            prev = findViewById(R.id.prev);
            next = findViewById(R.id.next);
            songTitle = findViewById(R.id.songTitle);
            imageView = findViewById(R.id.imageView);
            mSeekBarTime =findViewById(R.id.seekBarTime);
            mSeekBarVol = findViewById(R.id.seekBarVol);

            // creating an ArrayList to store our songs

            final ArrayList<Integer> songs = new ArrayList<>();

            songs.add(0, R.raw.song1);
            songs.add(1, R.raw.song2);
            songs.add(2, R.raw.song3);
            songs.add(3, R.raw.song4);
            songs.add(4, R.raw.song5);
            songs.add(5, R.raw.song6);



            // intializing mediaplayer

            mMediaPlayer = MediaPlayer.create(getApplicationContext(), songs.get(currentIndex));



            // seekbar volume

            int maxV = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            int curV = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            mSeekBarVol.setMax(maxV);
            mSeekBarVol.setProgress(curV);

            mSeekBarVol.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });


            //above seekbar volume
            //

            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSeekBarTime.setMax(mMediaPlayer.getDuration());
                    if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                        mMediaPlayer.pause();
                        play.setImageResource(R.drawable.ic_play_arrow);
                    } else {
                        mMediaPlayer.start();
                        play.setImageResource(R.drawable.ic_pause);
                    }

                    songNames();

                }
            });


            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mMediaPlayer != null) {
                        play.setImageResource(R.drawable.ic_pause);
                    }

                    if (currentIndex < songs.size() - 1) {
                        currentIndex++;
                    } else {
                        currentIndex = 0;
                    }

                    if (mMediaPlayer.isPlaying()) {
                        mMediaPlayer.stop();
                    }

                    mMediaPlayer = MediaPlayer.create(getApplicationContext(), songs.get(currentIndex));

                    mMediaPlayer.start();
                    songNames();

                }
            });


            prev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mMediaPlayer != null) {
                        play.setImageResource(R.drawable.ic_pause);
                    }

                    if (currentIndex > 0) {
                        currentIndex--;
                    } else {
                        currentIndex = songs.size() - 1;
                    }
                    if (mMediaPlayer.isPlaying()) {
                        mMediaPlayer.stop();
                    }

                    mMediaPlayer = MediaPlayer.create(getApplicationContext(), songs.get(currentIndex));
                    mMediaPlayer.start();
                    songNames();

                }
            });

        }

        private void songNames() {
            if (currentIndex == 0) {
                songTitle.setText("Alan Walker-Forever");
                imageView.setImageResource(R.drawable.img1);
            }
            if (currentIndex == 1) {
                songTitle.setText("Billie Eilish-Your Power");
                imageView.setImageResource(R.drawable.img2);
            }
            if (currentIndex == 2) {
                songTitle.setText("BTS-Butter");
                imageView.setImageResource(R.drawable.img3);
            }
            if (currentIndex == 3) {
                songTitle.setText("Usher-Yeah");
                imageView.setImageResource(R.drawable.img4);
            }
            if (currentIndex == 4) {
                songTitle.setText("Call me by your name");
                imageView.setImageResource(R.drawable.img5);
            }
            if (currentIndex == 5) {
                songTitle.setText("Starboy");
                imageView.setImageResource(R.drawable.img6);
            }


            // seekbar duration
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mSeekBarTime.setMax(mMediaPlayer.getDuration());
                    mMediaPlayer.start();
                }
            });

            mSeekBarTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser) {
                        mMediaPlayer.seekTo(progress);
                        mSeekBarTime.setProgress(progress);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (mMediaPlayer != null) {
                        try {
                            if (mMediaPlayer.isPlaying()) {
                                Message message = new Message();
                                message.what = mMediaPlayer.getCurrentPosition();
                                handler.sendMessage(message);
                                Thread.sleep(1000);
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }

        @SuppressLint("Handler Leak") Handler handler = new Handler () {
            @Override
            public void handleMessage  (Message msg) {
                mSeekBarTime.setProgress(msg.what);
            }
        };

    }