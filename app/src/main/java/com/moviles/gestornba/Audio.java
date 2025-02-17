package com.moviles.gestornba;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.media.MediaPlayer;
import android.util.Log;

public class Audio extends Service {
    public static final String ACTION_PLAY = "PLAY";
    public static final String ACTION_PAUSE = "PAUSE";
    public static final String ACTION_STOP = "STOP";

    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();

        mediaPlayer = MediaPlayer.create(this, R.raw.audio1);
        mediaPlayer.setLooping(true);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null && intent.getAction() != null) {
            String action = intent.getAction();
            switch (action) {
                case ACTION_PLAY:
                    playAudio();
                    break;
                case ACTION_PAUSE:
                    pauseAudio();
                    break;
                case ACTION_STOP:
                    stopAudio();
                    break;
            }
        }
        // Si el servicio es finalizado por el sistema, no lo recrea.
        return START_NOT_STICKY;
    }

    private void playAudio() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();

        }
    }

    private void pauseAudio() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();

        }
    }

    private void stopAudio() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            // Después de detener, se debe preparar nuevamente para poder reproducir
            mediaPlayer.reset();
            // Vuelve a configurar el MediaPlayer
            mediaPlayer = MediaPlayer.create(this, R.raw.audio1);

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()){
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        // Si no necesitas binding, simplemente retorna null.
        return null;
    }
    public Audio() {
    }


}