package com.sriyanksiddhartha.speechtotext;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import static com.sriyanksiddhartha.speechtotext.PreferenceManager.saveTargetClass;

public class TestGetAcivity extends AppCompatActivity {
    TextToSpeech tts;
    Handler handler;
    Class<?> target_class;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_get_acivity);
        initView();
        initTTS();
    }

    private void initView() {
        handler = new Handler();
        String class_str = getSharedPreferences(PreferenceManager.PREFERNCE_NAME,MODE_PRIVATE).getString(PreferenceManager.KEY_PREFERNCE_NAME," ");
        String arr[] = class_str.split(" ");
        System.err.println("Saved Class is "+arr[1]);
        try {
            target_class = Class.forName(arr[1]);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

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
    private void speak() {
        float pitch = (float) 0.0f;
        if (pitch < 0.1) pitch = 1.0f;
        float speed = (float) 0.0f;
        if (speed < 0.1) speed = 1.0f;

        tts.setPitch(pitch);
        tts.setSpeechRate(speed);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.speak("Give Your Command by clicking the button", TextToSpeech.QUEUE_FLUSH, null,TextToSpeech.ACTION_TTS_QUEUE_PROCESSING_COMPLETED);
        }
        else
            {
            tts.speak("Give Your Command by clicking the button", TextToSpeech.QUEUE_FLUSH, null);
        }

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
    public void gotoscreen(View view) {
        switch (view.getId()){
            case R.id.btnmain:
                saveTargetClass(TestGetAcivity.this,MainActivity.class);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        speak();
                    }
                }, 1000);

                break;
            case R.id.btnnext:
                saveTargetClass(TestGetAcivity.this,NextActivity.class);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        speak();
                    }
                }, 1000);
                break;

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
        String class_str = getSharedPreferences(PreferenceManager.PREFERNCE_NAME,MODE_PRIVATE).getString(PreferenceManager.KEY_PREFERNCE_NAME," ");
        String arr[] = class_str.split(" ");
        System.err.println("Saved Class is "+arr[1]);
        try {
            target_class = Class.forName(arr[1]); System.err.println("Get Class is "+target_class);
            if (s.contains("Main") || s.contains("Jango") || s.contains("open")){
                IntentHandlwer(target_class);
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
    void IntentHandlwer(Class target){
        Intent tent = new Intent(TestGetAcivity.this,target);
        startActivity(tent);
        finish();
    }
}
