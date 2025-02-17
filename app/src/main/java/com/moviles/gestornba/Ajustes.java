package com.moviles.gestornba;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;
import android.Manifest;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Ajustes extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    Switch mute;
    Button tomarFoto;
    SeekBar barraAudio;
    String usuario;
    AudioManager audio;
    ActivityResultLauncher<Intent> launcherVideo;
    ImageView foton;
    Button btnReco,btnStop,btnPlay;
    Intent intent;



    private MediaRecorder recorder = null;
    private MediaPlayer player = null;
    String fileName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.vista_ajustes);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        intent = new Intent(this, Audio.class);
        toolbar = findViewById(R.id.barra);
        mute = findViewById(R.id.btnSilenciar);

        fileName=getExternalCacheDir().getAbsolutePath() + "/grabacion_audio.3gp";

        btnReco=findViewById(R.id.btnReco);
        btnReco.setOnClickListener(this);
        btnStop=findViewById(R.id.btnPara);
        btnStop.setOnClickListener(this);
        btnPlay=findViewById(R.id.btnRepro);
        btnPlay.setOnClickListener(this);
        mute.setOnClickListener(this);
        setSupportActionBar(toolbar);
        audio=(AudioManager) getSystemService(Context.AUDIO_SERVICE);
        usuario=getIntent().getStringExtra("usuario");
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        int maxVolume = audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
        tomarFoto=findViewById(R.id.btnTomarFoto);
        tomarFoto.setOnClickListener(this);
        foton=findViewById(R.id.imagenUsuario);
        barraAudio=findViewById(R.id.barraSonido);
        barraAudio.setMax(maxVolume);
        barraAudio.setProgress(currentVolume);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE},100);

        }
        launcherVideo = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            Intent data = result.getData();
                            Bundle extras = data.getExtras();
                            Bitmap imgBitmap = (Bitmap) extras.get("data");
                            foton.setImageBitmap(imgBitmap);
                        }
                    }
                }
        );

        barraAudio.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                audio.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mute.setChecked(false);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_equipos, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(R.id.pap==item.getItemId()){
            irSimular();
        }else if(R.id.pep==item.getItemId()){
            irEquipos();
        }else if(R.id.pip ==item.getItemId()){
            irPlantilla();
        }else if(R.id.pop ==item.getItemId()){
            irClasificacion();
        }else if(R.id.pup==item.getItemId()){
            Toast.makeText(this, "Ya estas en ajustes", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void irPlantilla() {
        Intent i=new Intent(this, ControladorEquipos.class);
        i.putExtra("usuario", usuario);
        startActivity(i);
    }

    public void irEquipos(){
        Intent i=new Intent(this, ControladorLiga.class);
        i.putExtra("usuario", usuario);
        startActivity(i);
    }

    public void irSimular(){
        Intent i=new Intent(this, MainActivity.class);
        i.putExtra("usuario", usuario);
        startActivity(i);
    }
    public void irClasificacion() {
        Intent i=new Intent(this, Clasificacion.class);
        i.putExtra("usuario", usuario);
        startActivity(i);
    }


    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.btnSilenciar){
            boolean s=mute.isChecked();
            if(s){
                audio.setStreamMute(AudioManager.STREAM_MUSIC, true);
            }else{
                audio.setStreamMute(AudioManager.STREAM_MUSIC, false);
            }

        } else if (v.getId()==R.id.btnTomarFoto) {
            tomarPicture();
        }else if (v.getId()==R.id.btnReco) {
            grabar();

        }else if (v.getId()==R.id.btnRepro) {
            reproducir();
        }else if (v.getId()==R.id.btnPara) {
            pararGrabar();
        }

    }


    public void tomarPicture(){

        Intent fotito = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(getIntent().resolveActivity(getPackageManager())!=null) {
            launcherVideo.launch(fotito);
        }
    }



    private void grabar() {
        Intent intent = new Intent(this, Audio.class);
        intent.setAction(Audio.ACTION_PAUSE); // o ACTION_PAUSE, ACTION_STOP
        startService(intent);
        recorder = new MediaRecorder();

        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);

        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);

        recorder.setOutputFile(fileName);

        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
            recorder.start();
            Toast.makeText(this, "Grabando", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
           e.printStackTrace();
        }
    }


    private void pararGrabar() {
        if (recorder != null) {
            try {
                recorder.stop();
                Intent intent = new Intent(this, Audio.class);
                intent.setAction(Audio.ACTION_PLAY); // o ACTION_PAUSE, ACTION_STOP
                startService(intent);
            } catch (RuntimeException stopException) {

            }
            recorder.release();
            recorder = null;

        }
    }


    private void reproducir() {
        player = new MediaPlayer();
        try {
            Intent intent = new Intent(this, Audio.class);
            intent.setAction(Audio.ACTION_PAUSE); // o ACTION_PAUSE, ACTION_STOP
            startService(intent);
            player.setDataSource(fileName);
            player.prepare();
            player.start();
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    intent.setAction(Audio.ACTION_PLAY);
                    startService(intent);
                }
            });
            Toast.makeText(this, "Play ", Toast.LENGTH_SHORT).show();



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Liberar recursos al detener la actividad
    @Override
    protected void onStop() {
        super.onStop();
        if (recorder != null) {
            recorder.release();
            recorder = null;
            Toast.makeText(this, "Grabación detenida", Toast.LENGTH_SHORT).show();
        }
        if (player != null) {
            player.release();
            player = null;
        }
    }


}