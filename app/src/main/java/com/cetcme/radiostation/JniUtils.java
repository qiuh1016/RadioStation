package com.cetcme.radiostation;

/**
 * Created by qiuhong on 10/03/2017.
 */

public class JniUtils {

    public native int VoiceEncode(byte SpeechData[],byte BitstreamData[]);

    public native int VoiceDecode(byte BitstreamData[],byte SpeechData[]);

}
