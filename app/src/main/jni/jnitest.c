#include "com_cetcme_radiostation_JniUtils.h"
#include "g726.h"
#include <string.h>
#include <jni.h>
#include <stdio.h>
#include <stdlib.h>

JNIEXPORT jstring JNICALL
Java_com_cetcme_jnitest_JniUtils_getString (JNIEnv *env, jobject obj) {
    return (*env) -> NewStringUTF(env, "Hello World!!!");
}

JNIEXPORT jint JNICALL
Java_com_cetcme_jnitest_JniUtils_VoiceEncode(JNIEnv* env, jobject thiz, jbyteArray SpeechData, jbyteArray BitstreamData)
{
    jbyte * speech = (jbyte*)(*env)->GetByteArrayElements(env, SpeechData, 0);
    jbyte * bitstream= (jbyte*)(*env)->GetByteArrayElements(env, BitstreamData, 0);

    g726_Encode(speech,bitstream);

    (*env)->ReleaseByteArrayElements(env, SpeechData, speech, 0);
    (*env)->ReleaseByteArrayElements(env, BitstreamData, bitstream, 0);
    return 0;
}

JNIEXPORT jint JNICALL
Java_com_cetcme_jnitest_JniUtils_VoiceDecode(JNIEnv* env, jobject thiz, jbyteArray BitstreamData, jbyteArray SpeechData)
{
    jbyte * bitstream= (jbyte*)(*env)->GetByteArrayElements(env, BitstreamData, 0);
    jbyte * speech = (jbyte*)(*env)->GetByteArrayElements(env, SpeechData, 0);

    g726_Decode(bitstream,speech);

    (*env)->ReleaseByteArrayElements(env, BitstreamData, bitstream, 0);
    (*env)->ReleaseByteArrayElements(env, SpeechData, speech, 0);
    return 0;
}
