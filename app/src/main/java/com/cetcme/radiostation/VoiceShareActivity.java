package com.cetcme.radiostation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class VoiceShareActivity extends AppCompatActivity {

    private Button StartRecordButton = null;
    //it needs to be a single-channel (monaural),
    //little-endian, unheadered 16-bit signed PCM audio file sampled at 16000 Hz.
    //44100Hz是当前唯一能保证在所有设备上工作的采样率，在一些设备上还有22050, 16000或11025。
    static final int frequency = 8000;   //8KHz
    static final int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_MONO;
    static final int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;

    int recBufSize = 0;
    private AudioRecord audioRecord = null;
    int playBufSize = 0;
    private AudioTrack audioTrack = null;
    public MediaPlayer AnswerPlayer = null;

    public Handler AudioRecordHandler = null;
    public Runnable AudioRecordTask = null;
    public boolean AudioRecordTimerRun = false;

    public String RawFilePath = "/sdcard/VoiceShareRecord.pcm";
    public File RawFile = null;
    public FileOutputStream RawFOStream = null;
    public BufferedOutputStream RawBOStream = null;
    public InputStream AnswerIStream = null;
    public FileInputStream AnswerFIStream = null;

    //-------------------------------------------------
    static final int VOICE_FRAME_SIZE = 480;
    public String EncodeDataFilePath = "/sdcard/VoiceShareEncodeResult.g726";
    public String DecodeDataFilePath = "/sdcard/VoiceShareDecodeResult.pcm";

    //Multicast Encoded Voice Data!!!
    static String MultiSenderAddress = "192.168.0.196";
    static int MultiSenderPort = 9999;
    static InetAddress IA = null;
    static MulticastSocket MultiReceiver = null;

    private ProgressDialog AudioRecordPD = null;
    private ProgressDialog AudioRecogPD = null;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        //默认竖屏!!!
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_share);

        //16bits->recBufSize:1600 16bits->playBufSize:2972 8bits->playBufSize:1486
        recBufSize = AudioRecord.getMinBufferSize(frequency, channelConfiguration, audioEncoding);
        Log.d("main", "recBufSize:" + recBufSize);

        //recBufSize:1600
        if (recBufSize < 0)
        {
            recBufSize = 1600 * 3;
            //mBufferFrames = mBufferBytes / (numChannels * sampleBytes);
        }

        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, frequency, channelConfiguration, audioEncoding, recBufSize);
        playBufSize = AudioTrack.getMinBufferSize(frequency, channelConfiguration, audioEncoding);
        Log.d("main", "playBufSize:" + playBufSize);

        //playBufSize:1486
        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, frequency, channelConfiguration, audioEncoding, playBufSize, AudioTrack.MODE_STREAM);

        StartRecordButton= (Button) findViewById(R.id.StartRecordButton);
        StartRecordButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                AudioRecordPD = new ProgressDialog(VoiceShareActivity.this);
                AudioRecordPD.setMessage("Please Speak...");
                AudioRecordPD.show();
                new AudioRecordTask().execute("Start");
            }
        });

        AudioRecordHandler = new Handler();
        AudioRecordTask = new Runnable() {
            public void run() {
                //Stop!!!
                if (AudioRecordTimerRun) {
                    //HideList();
                    AudioRecordTimerRun = false;
                }
            }
        };

        try {
            IA = InetAddress.getByName(MultiSenderAddress);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        try {
            MultiReceiver = new MulticastSocket(MultiSenderPort);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            //MultiReceiver.setTimeToLive(255);
            MultiReceiver.setTimeToLive(2);
            //MultiReceiver.setReceiveBufferSize(10240);
            //MultiReceiver.setSoTimeout(10);    //Wait 10ms!!!
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            MultiReceiver.joinGroup(IA);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 设置三种类型参数分别为String,Integer,String
    class AudioRecordTask extends AsyncTask<String, Integer, String>
    {
        // 可变长的输入参数，与AsyncTask.exucute()对应
        @Override
        protected String doInBackground(String... params) {
            //Start!!!
            //Create New Raw File
            RawFile = new File(RawFilePath);
            try {
                RawFOStream = new FileOutputStream(RawFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            RawBOStream = new BufferedOutputStream(RawFOStream);

            byte[] RecordBuffer = new byte[recBufSize];
            audioRecord.startRecording();

            AudioRecordHandler.removeCallbacks(AudioRecordTask);
            AudioRecordTimerRun = true;
            //AudioRecordHandler.postDelayed(AudioRecordTask, 5000);


            String FILE_NAME = System.currentTimeMillis() + ".txt";
            FileUtil.saveFile(FILE_NAME, "");

            while(AudioRecordTimerRun) {
                //1600->OK 960->OK!!!Has Lost???
                int bufferReadResult = audioRecord.read(RecordBuffer, 0, VOICE_FRAME_SIZE * 2);  //480*2=960
                Log.d("bufferReadResult", String.valueOf(bufferReadResult));
                byte[] TmpBuf = new byte[bufferReadResult];

                Log.i("main", "doInBackground: "+ RecordBuffer);
                FileUtil.appendData(FILE_NAME, String.valueOf(RecordBuffer));

                System.arraycopy(RecordBuffer, 0, TmpBuf, 0, bufferReadResult);

                byte[] TmpEnArr = new byte[VOICE_FRAME_SIZE / 8 * 2];

//                JniUtils jniUtils = new JniUtils();
//                jniUtils.VoiceEncode(TmpBuf, TmpEnArr);

                DatagramPacket packet = new DatagramPacket(TmpEnArr, VOICE_FRAME_SIZE / 8 * 2, IA, MultiSenderPort);
                packet.setLength(VOICE_FRAME_SIZE / 8 * 2);  //480*2/8=120

//                Log.i("main", "doInBackground: "+ packet);

                try {
                    MultiReceiver.send(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //RAEncodeFile.write(TmpEnArr, 0, VOICE_FRAME_SIZE/8*2);  //480/8*2=120
				/*
				try {
					RawBOStream.write(TmpBuf, 0, bufferReadResult);
				} catch (IOException e) {
					e.printStackTrace();
				}
				*/
            }

            try {
                RawBOStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                RawFOStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            audioRecord.stop();
            AudioRecordPD.dismiss();
            //VoiceRecogByRawFile(RawFilePath);
            return "OK!";
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(VoiceShareActivity.this, "Record OK!", Toast.LENGTH_SHORT).show();
            //AudioRecogPD=new ProgressDialog(voiceshare.this);
            //AudioRecogPD.setMessage("Recoging...");
            //AudioRecogPD.show();
            //new AudioRecogTask().execute("Start");
            ////new VoiceEncodeDecodeTask().execute("Start");

            try {
                MultiReceiver.leaveGroup(IA);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        @Override
        protected void onPreExecute() {
            //任务启动
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            //更新进度
        }
    }

    // 设置三种类型参数分别为String,Integer,String
    class AudioRecogTask extends AsyncTask<String, Integer, String>
    {
        // 可变长的输入参数，与AsyncTask.exucute()对应
        @Override
        protected String doInBackground(String... params)
        {
            audioTrack.play();
            byte[] PlayBuf=new byte[playBufSize];
            FileInputStream FIS=null;
            try
            {
                FIS = new FileInputStream(RawFile);
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
            int RawFileBytesRead=0;
            while(true)
            {
                try
                {
                    RawFileBytesRead=FIS.read(PlayBuf, 0, playBufSize);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                if(RawFileBytesRead>0)
                {
                    audioTrack.write(PlayBuf, 0, RawFileBytesRead);
                }
                else
                {
                    return "OK!";
                }
            }
        }

        @Override
        protected void onCancelled()
        {
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(String result)
        {
            AudioRecogPD.dismiss();
            audioTrack.stop();
        }

        @Override
        protected void onPreExecute()
        {
            //任务启动
        }

        @Override
        protected void onProgressUpdate(Integer... values)
        {
            //更新进度
        }
    }

    public static final int EXIT_ID = Menu.FIRST;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, EXIT_ID, 0, "Exit");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case EXIT_ID: {
                VoiceShareActivity.this.finish();
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    static {
        System.loadLibrary("JniG726");
    }

//    //jbyteArray SpeechData, jbyteArray BitstreamData
//    public native int VoiceEncode(byte SpeechData[],byte BitstreamData[]);
//    //jbyteArray BitstreamData, jbyteArray SpeechData
//    public native int VoiceDecode(byte BitstreamData[],byte SpeechData[]);

    // 设置三种类型参数分别为String,Integer,String
    class VoiceEncodeDecodeTask extends AsyncTask<String, Integer, String>
    {
        // 可变长的输入参数，与AsyncTask.exucute()对应
        @Override
        protected String doInBackground(String... params)
        {
            audioTrack.play();

            short[] ou_dec_pcmbuf = new short[VOICE_FRAME_SIZE];     //decoder output
            short[] in_enc_pcmbuf = new short[VOICE_FRAME_SIZE];     //encoder input
            short[] ou_enc_unpacked = new short[VOICE_FRAME_SIZE];	  //encoder output
            byte[] TmpArr=new byte[VOICE_FRAME_SIZE*2];
            byte[] TmpEnArr=new byte[VOICE_FRAME_SIZE/8*2];
            int frame=0;

            File BeforeEncodeFile = new File(RawFilePath);
            File EncodeFile = new File(EncodeDataFilePath);
            if (EncodeFile.exists())
                EncodeFile.delete();
            File DecodeFile = new File(DecodeDataFilePath);
            if (DecodeFile.exists())
                DecodeFile.delete();
            try
            {
                //Need to Change!!!
                RandomAccessFile RABeforeEncodeFile = new RandomAccessFile(BeforeEncodeFile, "rw");
                RandomAccessFile RAEncodeFile = new RandomAccessFile(EncodeFile, "rw");
                //Try to read to short array!!!
                //readshort!!!
                //960!!!

                while( RABeforeEncodeFile.read(TmpArr, 0, VOICE_FRAME_SIZE*2)==VOICE_FRAME_SIZE*2)
                {
                    for(int i=0;i<VOICE_FRAME_SIZE;i++)
                    {
                        //高字节位->最重要的字节 低字节位->最不重要的字节 big-endian->最重要的字节在前 最不重要的字节在后 SPARC PowerPC->big-endian 而Intel x86->little-endian
                        in_enc_pcmbuf[i]=(short) (TmpArr[i+1]<<8+TmpArr[i]);
                    }

                    JniUtils jniUtils = new JniUtils();
                    jniUtils.VoiceEncode(TmpArr, TmpEnArr);
                    RAEncodeFile.write(TmpEnArr, 0, VOICE_FRAME_SIZE/8*2);  //480/8*2=120
                    frame++;
                }

                RABeforeEncodeFile.close();
                RAEncodeFile.close();

                frame=0;
                RandomAccessFile RABeforeDecodeFile = new RandomAccessFile(EncodeFile, "rw");
                RandomAccessFile RADecodeFile = new RandomAccessFile(DecodeFile, "rw");

                while( RABeforeDecodeFile.read(TmpEnArr, 0, VOICE_FRAME_SIZE/8*2)==VOICE_FRAME_SIZE/8*2)
                {
                    JniUtils jniUtils = new JniUtils();
                    jniUtils.VoiceDecode(TmpEnArr, TmpArr);
                    RADecodeFile.write(TmpArr, 0, VOICE_FRAME_SIZE*2);  //480/8*2=120
                    frame++;
                }

                RABeforeDecodeFile.close();
                RADecodeFile.close();
            }
            catch (Exception e1)
            {
                e1.printStackTrace();
            }

            //PlayRecordPCMFile------------------------------------->
            byte[] PlayBuf=new byte[playBufSize];
            FileInputStream FIS=null;
            try
            {
                FIS = new FileInputStream(RawFile);
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
            int RawFileBytesRead=0;
            while(true)
            {
                try
                {
                    RawFileBytesRead=FIS.read(PlayBuf, 0, playBufSize);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                if(RawFileBytesRead>0)
                {
                    audioTrack.write(PlayBuf, 0, RawFileBytesRead);
                }
                else
                {
                    //return "OK!";
                    break;
                }
            }
            audioTrack.stop();

            audioTrack.play();
            FIS=null;
            try
            {
                FIS = new FileInputStream(DecodeFile);
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
            RawFileBytesRead=0;
            while(true)
            {
                try
                {
                    RawFileBytesRead=FIS.read(PlayBuf, 0, playBufSize);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                if(RawFileBytesRead>0)
                {
                    audioTrack.write(PlayBuf, 0, RawFileBytesRead);
                }
                else
                {
                    //return "OK!";
                    break;
                }
            }

            return "OK!";
        }

        @Override
        protected void onCancelled()
        {
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(String result)
        {
            AudioRecogPD.dismiss();
            audioTrack.stop();
        }

        @Override
        protected void onPreExecute()
        {
            //任务启动
        }

        @Override
        protected void onProgressUpdate(Integer... values)
        {
            //更新进度
        }
    }
}
