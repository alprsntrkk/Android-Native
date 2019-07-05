package com.androidnative;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.telecom.Call;
import android.util.Log;
import android.view.View;

import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.uimanager.IllegalViewOperationException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.UUID;

import AlizeSpkRec.SimpleSpkDetSystem;

import static android.support.v4.app.ActivityCompat.startActivityForResult;

public class SpeakerRecognitionModule extends ReactContextBaseJavaModule {
    WavRecorder wavRecorderModel;
    WavRecorder wavRecorderSpeaker;
    String pathSpeaker;
    String pathReadableSpeaker;
    String pathModel;
    String pathReadableModel;
    String result;
    Context context;
    View view;

    public SpeakerRecognitionModule(ReactApplicationContext reactContext) {
        super(reactContext); //required by React Native
    }

    @Override
    //getName is required to define the name of the module represented in JavaScript
    public String getName() {
        return "SpeakerRecognition";
    }

    @ReactMethod

    public void sayHi(Callback errorCallback, Callback successCallback) {
        try {
            System.out.println("Greetings from Java");
            successCallback.invoke("Callback : Greetings from Java");
        } catch (IllegalViewOperationException e) {
            errorCallback.invoke(e.getMessage());
        }

    }

    @ReactMethod
    public void speakerRecorderStart(Callback errorCallback, Callback successCallback){
        try {
            wavRecorderSpeaker=new WavRecorder(createPathSpeaker());
            wavRecorderSpeaker.startRecording();
            System.out.println("AndroidNative");
            successCallback.invoke(pathSpeaker);
        } catch (IllegalViewOperationException e) {
            errorCallback.invoke(e.getMessage());
        }
    }

    @ReactMethod
    public void speakerRecorderStop(Callback errorCallback, Callback successCallback){

        try {
            wavRecorderSpeaker.stopRecording();
            System.out.println("AndroidNative");
            successCallback.invoke("Speaker recorder stopped!");
        } catch (IllegalViewOperationException e) {
            errorCallback.invoke(e.getMessage());
        }

    }

    @ReactMethod
    public void modelRecorderStart(Callback errorCallback, Callback successCallback){
        try {
            wavRecorderModel=new WavRecorder(createPathModel());
            wavRecorderModel.startRecording();
            System.out.println("AndroidNative");
            successCallback.invoke(pathModel);
        } catch (IllegalViewOperationException e) {
            errorCallback.invoke(e.getMessage());
        }

    }

    @ReactMethod
    public void modelRecorderStop(Callback errorCallback, Callback successCallback){
        try {
            wavRecorderModel.stopRecording();
            System.out.println("AndroidNative");
            successCallback.invoke("Model recorder stopped!");
        } catch (IllegalViewOperationException e) {
            errorCallback.invoke(e.getMessage());
        }

    }

    private String createPathSpeaker(){
        pathReadableSpeaker="/alperVava"+ UUID.randomUUID().toString()+"_audio_record.wav";
        pathSpeaker = Environment.getExternalStorageDirectory().getAbsolutePath()+pathReadableSpeaker;
        return pathSpeaker;
    }

    private String createPathModel(){
        pathReadableModel="/alperVava"+ UUID.randomUUID().toString()+"_audio_record.wav";
        pathModel = Environment.getExternalStorageDirectory().getAbsolutePath()+pathReadableModel;
        return pathModel;
    }

    @ReactMethod
    public SimpleSpkDetSystem alizeAll(Callback errorCallback, Callback successCallback){
        try {
            InputStream configAsset = getReactApplicationContext().getAssets().open("AlizeDefault.cfg");
            final SimpleSpkDetSystem alizeSystem = new SimpleSpkDetSystem(configAsset, getReactApplicationContext().getFilesDir().getPath());
            configAsset.close();

            InputStream backgroundModelAsset = getReactApplicationContext().getAssets().open("gmm/world.gmm");
            alizeSystem.loadBackgroundModel(backgroundModelAsset);
            backgroundModelAsset.close();


            alizeSystem.resetAudio();
            alizeSystem.resetFeatures();




            //<---------------------------------------------------------------------------------------------------------------------->
            //Environment.getExternalStorageDirectory().getAbsoluteFile()
            //Environment.getExternalStorageDirectory().getAbsolutePath()           +         "/"+"/alperVava"+ UUID.randomUUID().toString()+"_audio_record.wav";

            File file = new File("sdcard"+pathReadableModel);
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStream audioIS = fileInputStream;
            ByteArrayOutputStream audioBytes = new ByteArrayOutputStream();
            while (audioIS.available() > 0) {
                audioBytes.write(audioIS.read());
            }
            byte[] tmpBytes = audioBytes.toByteArray();
            short[] audioL16Samples = new short[tmpBytes.length/2];
            ByteBuffer.wrap(tmpBytes).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(audioL16Samples);

            // Then, pass it to ALIZÉ the same way we would for data just recorded with the microphone.
            alizeSystem.addAudio(audioL16Samples);

            // Phase 2: Train a model with the just-added audio
            // Speaker IDs are just free-form strings. It is usually a good idea to keep them simple and store
            // speaker-related metadata, such as names, in a separate, application-managed database.
            alizeSystem.createSpeakerModel("spk01");



            alizeSystem.resetAudio();
            alizeSystem.resetFeatures();



            audioIS = getReactApplicationContext().getAssets().open("data/B1ALP.wav");
            audioBytes = new ByteArrayOutputStream();
            while (audioIS.available() > 0) {
                audioBytes.write(audioIS.read());
            }
            tmpBytes = audioBytes.toByteArray();
            audioL16Samples = new short[tmpBytes.length/2];
            ByteBuffer.wrap(tmpBytes).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(audioL16Samples);

            // Then, pass it to ALIZÉ the same way we would for data just recorded with the microphone.
            alizeSystem.addAudio(audioL16Samples);
            alizeSystem.createSpeakerModel("spk02");



            alizeSystem.resetAudio();
            alizeSystem.resetFeatures();


            file = new File("sdcard"+pathReadableSpeaker);
            fileInputStream = new FileInputStream(file);
            audioIS = fileInputStream;
            audioBytes = new ByteArrayOutputStream();
            while (audioIS.available() > 0) {
                audioBytes.write(audioIS.read());
            }
            tmpBytes = audioBytes.toByteArray();
            audioL16Samples = new short[tmpBytes.length/2];
            ByteBuffer.wrap(tmpBytes).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(audioL16Samples);
            alizeSystem.addAudio(audioL16Samples);


                SimpleSpkDetSystem.SpkRecResult verificationResult = alizeSystem.verifySpeaker("spk02");

                // The returned value tells us if there was a match, and what the score was.
                Log.i("ALIZE","***********************************************");
                Log.i("ALIZE","Speaker verification against speaker spk02:");
                Log.i("ALIZE","  match: " + verificationResult.match);
                Log.i("ALIZE","  score: " + verificationResult.score);
                Log.i("ALIZE","***********************************************");
                alizeSystem.resetAudio();
                alizeSystem.resetFeatures();


                file = new File("sdcard"+pathReadableSpeaker);
                fileInputStream = new FileInputStream(file);


            audioIS=fileInputStream;
            audioBytes = new ByteArrayOutputStream();
            while (audioIS.available() > 0) {
                audioBytes.write(audioIS.read());
            }
            tmpBytes = audioBytes.toByteArray();
            audioL16Samples = new short[tmpBytes.length/2];
            ByteBuffer.wrap(tmpBytes).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(audioL16Samples);
            alizeSystem.addAudio(audioL16Samples);


            SimpleSpkDetSystem.SpkRecResult identificationResult = alizeSystem.identifySpeaker();

            // Here, the result tells us whether any registered speaker matched the signal,
            // which one obtained the highest score, and what this score was (even if there was no match).
            Log.i("ALIZE","***********************************************");
            Log.i("ALIZE","Speaker identification:");
            Log.i("ALIZE","  match: " + identificationResult.match);
            Log.i("ALIZE","  closest speaker: " + identificationResult.speakerId);
            Log.i("ALIZE","  score: " + identificationResult.score);
            Log.i("ALIZE","***********************************************");

            alizeSystem.resetAudio();
            alizeSystem.resetFeatures();

            /*================*/
            /*   That's it!   */
            /*================*/
            if(identificationResult.match==true&&identificationResult.score>10){
                successCallback.invoke();
            }

            return alizeSystem;

        } catch (Throwable e) {
            errorCallback.invoke(e+" hata!");
            return null;
        }
    }

    @ReactMethod
    public void result(Callback errorCallback, Callback successCallback){
        try{
           successCallback.invoke(this.result);
        }
        catch(Exception e){
            errorCallback.invoke(e);
        }
    }
}
