package com.wishakhn.jangoassistant;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView txvResult;
    TextToSpeech tts;
    String tts_str = "";
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handler = new Handler();
        tts_str = getResources().getString(R.string.welcome_note);
        initTTS();
        txvResult = (TextView) findViewById(R.id.txvResult);

    }


    private void showWelcomedialog() {
        AlertDialog.Builder welcomenote = new AlertDialog.Builder(MainActivity.this);
        View v = getLayoutInflater().inflate(R.layout.welcomedialog, null);
        welcomenote.setView(v);
        final AlertDialog dialog = welcomenote.create();
        dialog.show();
        dialog.setCancelable(false);
        TextView notetext = v.findViewById(R.id.notetext);
        notetext.setText(tts_str);
        ttsListner(dialog);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                speak(tts_str);
            }
        }, 1000);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


    }

    void initTTS() {
        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int ttsLang = tts.setLanguage(Locale.UK);

                    if (ttsLang == TextToSpeech.LANG_MISSING_DATA
                            || ttsLang == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "The Language is not supported!");
                    } else {
                        Log.i("TTS", "Language Supported.");
                        showWelcomedialog();

                    }
                    Log.i("TTS", "Initialization success.");
                } else {
                    Toast.makeText(getApplicationContext(), "TTS Initialization failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void speak(String text) {
        float pitch = (float) 0.0f;
        if (pitch < 0.1) pitch = 1.0f;
        float speed = (float) 0.0f;
        if (speed < 0.1) speed = 1.0f;

        tts.setPitch(pitch);
        tts.setSpeechRate(speed);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null,TextToSpeech.ACTION_TTS_QUEUE_PROCESSING_COMPLETED);
        }
        else {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }

    }
    void ttsListner(final AlertDialog dialog){
        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String s) {

            }

            @Override
            public void onDone(String s) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                    }
                });
            }

            @Override
            public void onError(String s) {

            }
        });
    }

    public void getSpeechInput(View view) {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 10);
        } else {
            Toast.makeText(this, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    actOnCommand(result.get(0));
                }
                break;
        }
    }

    private void actOnCommand(String s) {
        if (s.equalsIgnoreCase("next") || s.contains("next")) {
            Intent startnext = new Intent(MainActivity.this, NextActivity.class);
            startActivity(startnext);
        } else {
            txvResult.setText("you have spoken: " + s);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        tts.stop();
        tts.shutdown();
    }
}
