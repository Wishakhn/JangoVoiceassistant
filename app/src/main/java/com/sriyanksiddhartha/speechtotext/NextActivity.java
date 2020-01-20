package com.sriyanksiddhartha.speechtotext;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class NextActivity extends AppCompatActivity {
Handler handler;
TextToSpeech tts;
TextToSpeech tts_alter;
String tts_str;
ImageView startJango;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);
        handler = new Handler();
        startJango = findViewById(R.id.startJango);
        tts_str = getResources().getString(R.string.welcome_i_am_jango);
        initTTS();
        setListner();
    }

    private void setListner() {
        startJango.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speak("Is there anything I can help you with");
            }
        });
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
                        ttsListner();

                    }

                    Log.i("TTS", "Initialization success.");
                } else {
                    Toast.makeText(getApplicationContext(), "TTS Initialization failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                speak(tts_str);
            }
        }, 1000);
    }
    void initTTS_alter(final String msg) {
        tts_alter = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int ttsLang = tts.setLanguage(Locale.UK);

                    if (ttsLang == TextToSpeech.LANG_MISSING_DATA
                            || ttsLang == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "The Language is not supported!");
                    } else {
                        Log.i("TTS", "Language Supported.");
                        showImageDialog();

                    }

                    Log.i("TTS", "Initialization success.");
                } else {
                    Toast.makeText(getApplicationContext(), "TTS Initialization failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                speak_alter(msg);
            }
        }, 1000);
    }

    private void speak_alter(String text) {
        float pitch = (float) 0.0f;
        if (pitch < 0.1) pitch = 0.6f;
        float speed = (float) 0.0f;
        if (speed < 0.1) speed = 1.0f;

        tts_alter.setPitch(pitch);
        tts_alter.setSpeechRate(speed);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts_alter.speak(text, TextToSpeech.QUEUE_FLUSH, null,TextToSpeech.ACTION_TTS_QUEUE_PROCESSING_COMPLETED);
        }
        else {
            tts_alter.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }

    }
    private void speak(String text) {
        float pitch = (float) 0.0f;
        if (pitch < 0.1) pitch = 0.6f;
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
    void ttsListner(){
        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String s) {

            }

            @Override
            public void onDone(String s) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getVoiceInput();
                    }
                });
            }

            @Override
            public void onError(String s) {

            }
        });
    }
    void ttsListner_alter(final AlertDialog dialog){
        tts_alter.setOnUtteranceProgressListener(new UtteranceProgressListener() {
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

    private void showImageDialog() {
        AlertDialog.Builder welcomenote = new AlertDialog.Builder(NextActivity.this);
        View v = getLayoutInflater().inflate(R.layout.imagedialog, null);
        welcomenote.setView(v);
        final AlertDialog dialog = welcomenote.create();
        dialog.show();
        dialog.setCancelable(false);
        ImageView imageis= v.findViewById(R.id.imageis);
        imageis.setImageResource(R.drawable.coffee);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ttsListner_alter(dialog);
            }
        },2000);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    void  getVoiceInput(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 11);
        } else {
            Toast.makeText(this, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==11){
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                actOnCommand(result.get(0));
            }
        }
    }

    private void actOnCommand(String s) {
        if (s.contains("finish")|| s.contains("back")){
            finish();
        }
        else if (s.contains("next")){
            Intent nwxt = new Intent(NextActivity.this,TestGetAcivity.class);
            startActivity(nwxt);
            finish();
        }
        else if (s.equalsIgnoreCase("no") || s.equalsIgnoreCase("nothing")||s.contains("thank you") || s.contains("thanks")){
            if (tts.isSpeaking()){
                tts.stop();
            }
        }
        else {
            if (s.contains("coffee")){
                initTTS_alter("Oh! yeah sure you can have one, please enjoy your coffee.");
            }
            else if (s.contains("food")|| s.contains("pizza") || s.contains("burger") || s.contains("hungry")){
                speak("You are working at your office right now, so please behave while commanding your voice assistant, Do you want me to do anything else for you");
            }
            else {
                speak("Sorry but i cannot perform this action right now. please command again");

            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
      if (tts_alter !=null){
          tts_alter.stop();
          tts_alter.shutdown();
      }
        tts.stop();
        tts.shutdown();
    }
}
