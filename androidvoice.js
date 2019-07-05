
import React from 'react';
import Voice from 'react-native-voice';
import {View,Button,Text} from 'react-native';
import {PermissionsAndroid} from 'react-native';
import {NativeModules} from 'react-native';
var SpeakerRecognition = NativeModules.SpeakerRecognition;

export default class VoiceRec extends React.Component{
    constructor(props){
        super();
        this.state = {
        results: [],
        voiceRecognition:[],
        definitions:["T1","T2","T3","T4","T5","T6","T7",
                     "B1","B2","B3","B4","B5","B6","B7",
                     "C1","C2","C3","C4","C5","C6","C7",
                     "X1","X2","X3","X4","X5","X6","X7"],
        isStarted:false,
        isSamePerson:false
        }
        Voice.onSpeechPartialResults = this.onSpeechPartialResults.bind(this);
        Voice.onSpeechEnd=this.onSpeechEnd.bind(this);
        SpeakerRecognition.speakerRecorderStop=this.speakerRecorderStop.bind(this);
        SpeakerRecognition.speakerRecorderStart=this.speakerRecorderStart.bind(this);
      }

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
        SpeakerRecognition.alizeAll((err) => {console.log(err+"  hata")},() => {
            this.setState({isSamePerson:true});
        });
      }

      async result(){
        SpeakerRecognition.result( (err) => {console.log(err)}, (msg) => {console.log(msg) });
      }

    onSpeechPartialResults(e){        
        this.setState({
          results:e.value
        })
      }
    
      async onSpeechStart(){

        Voice.start('en_US');
      }
    
      async onSpeechEnd(){
        Voice.stop();       
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



      render(){ 
        for(i=0;i<this.state.results.length;i++){
          if(this.state.definitions.includes(this.state.results[i])){            
            this.state.voiceRecognition.push(this.state.results[i]);
            break;
          }
        }        
          return(
              <View>
                <Button
                    onPress={this.modelRecorderStart}
                    title="Model Kaydet"
                    color="#841584"
                    accessibilityLabel="Learn more about this purple button"
                    />
                    <Button
                    onPress={this.modelRecorderStop}
                    title="Model Durdur"
                    color="#841584"
                    accessibilityLabel="Learn more about this purple button"
                    />
                  <Button
                  
                    onPress={this.onSpeechStart}
                    title="Konus"
                    color="#841584"
                    accessibilityLabel="Learn more about this purple button"
                    />
                    <Button
                    onPress={this.onSpeechEnd}
                    title="Durdur"
                    color="#841584"
                    accessibilityLabel="Learn more about this purple button"
                    />
                    <Text>
                    {this.state.voiceRecognition.map( (text,index) => {
                        return(
                          <Text key={index}>{text} Sonuç {"\n"}</Text>
                        ) 
                    })}
                    </Text>
                    <Text>
                    {this.state.results.map( (text,index) => {
                        return(
                          <Text key={index}>{text} lib result {"\n"}</Text>
                        ) 
                    })}
                    </Text>
                    
              </View>
          )
      }
}