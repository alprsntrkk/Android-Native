/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow
 */

import React, {Component} from 'react';
import {Platform, StyleSheet, Text, View,Button} from 'react-native';
import {NativeModules} from 'react-native';
import {PermissionsAndroid} from 'react-native';
import VoiceRec from './androidvoice';

var SpeakerRecognition = NativeModules.SpeakerRecognition;

const instructions = Platform.select({
  ios: 'Press Cmd+R to reload,\n' + 'Cmd+D or shake for dev menu',
  android:
    'Double tap R on your keyboard to reload,\n' +
    'Shake or press menu button for dev menu',
});

type Props = {};
export default class App extends Component<Props> {

  componentDidMount(){
    this.requestStoragePermission();
    this.requestAudioPermission();
  }
  async sayHiFromJava() {
    //HelloWorld.sayHi( (err) => {console.log(err)}, (msg) => {console.log(msg)} );
    SpeakerRecognition.sayHi( (err) => {console.log(err)}, (msg) => {console.log(msg) } );
  }

  async modelRecorderStart(){
    SpeakerRecognition.modelRecorderStart( (err) => {console.log(err)}, (msg) => {console.log(msg) });
  }

  async modelRecorderStop(){
    SpeakerRecognition.modelRecorderStop( (err) => {console.log(err)}, (msg) => {console.log(msg) });
  }

  async speakerRecorderStart(){
    SpeakerRecognition.speakerRecorderStart( (err) => {console.log(err)}, (msg) => {console.log(msg) });
  }

  async speakerRecorderStop(){
    SpeakerRecognition.speakerRecorderStop( (err) => {console.log(err)}, (msg) => {console.log(msg) });
  }

  async alizeAll(){
    SpeakerRecognition.alizeAll((err) => {console.log(err+"  hata")},(msg) => {console.log(msg) });
  }
  async result(){
    SpeakerRecognition.result( (err) => {console.log(err)}, (msg) => {console.log(msg) });
  }

  async requestAudioPermission() {
    try {
      const granted = await PermissionsAndroid.request(
        PermissionsAndroid.PERMISSIONS.RECORD_AUDIO,
        {
          title: 'Cool Audio App Audio Permission',
          message:
            'Cool Audio App needs access to your mic ',
          buttonNeutral: 'Ask Me Later',
          buttonNegative: 'Cancel',
          buttonPositive: 'OK',
        },
      );
      if (granted === PermissionsAndroid.RESULTS.GRANTED) {
        console.log('You can use the mic');
      } else {
        console.log('mic permission denied');
      }
    } catch (err) {
      console.warn(err);
    }
  }
  async requestStoragePermission() {
    try {
      const granted = await PermissionsAndroid.request(
        PermissionsAndroid.PERMISSIONS.READ_EXTERNAL_STORAGE,
        {
          title: 'Cool Photo App Camera Permission',
          message:
            'Cool Storage App needs access to your sdcard ',
          buttonNeutral: 'Ask Me Later',
          buttonNegative: 'Cancel',
          buttonPositive: 'OK',
        },
      );
      if (granted === PermissionsAndroid.RESULTS.GRANTED) {
        console.log('You can use the sdcard');
      } else {
        console.log('sdcard permission denied');
      }
    } catch (err) {
      console.warn(err);
    }
  }


  render() {
    return (
      <View style={styles.modelRecorderStart}>
        <VoiceRec/>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
  instructions: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 5,
  },
});
