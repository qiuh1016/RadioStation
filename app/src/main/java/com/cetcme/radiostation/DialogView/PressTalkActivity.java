package com.cetcme.radiostation.DialogView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cetcme.radiostation.MainActivity;
import com.cetcme.radiostation.R;
import com.qiuhong.qhlibrary.QHTitleView.QHTitleView;

import java.io.File;

public class PressTalkActivity extends Activity {

    private Drawable[] micImages;
    private TextView mic_textView;

    private Button talk_button;
    private TextView hint_textView;

    private MediaRecorder recorder=null;

    private int BASE = 600;
    private int SPACE = 200;// 间隔取样时间

    //录音存储在SD卡的路径
    private String output_Path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator+"luyin.3gp";
    //录音文件
    private File soundFile;

    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            int what = msg.what;
            //根据mHandler发送what的大小决定话筒的图片是哪一张
            //说话声音越大,发送过来what值越大
            if( what > micImages.length - 1){
                what = micImages.length - 1;
            }
            mic_textView.setBackground(micImages[what]);
        };
    };
    private Runnable mUpdateMicStatusTimer = new Runnable() {
        public void run() {
            updateMicStatus();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_press_talk);
        initView();
    }

    private void initView() {
        initTitleView();

        mic_textView = (TextView) findViewById(R.id.mic_textView);
        hint_textView = (TextView) findViewById(R.id.hint_textView);
        talk_button = (Button) findViewById(R.id.talk_button);
        talk_button.setOnTouchListener(new PressToSpeakListen());
        talk_button.setOnClickListener(null);

        micImages = new Drawable[] {
//            getResources().getDrawable(R.drawable.mic_pic),
            getResources().getDrawable(R.drawable.mic_sound_1),
            getResources().getDrawable(R.drawable.mic_sound_2),
            getResources().getDrawable(R.drawable.mic_sound_3),
            getResources().getDrawable(R.drawable.mic_sound_4),
            getResources().getDrawable(R.drawable.mic_sound_5),
            getResources().getDrawable(R.drawable.mic_sound_6),
            getResources().getDrawable(R.drawable.mic_sound_7),
            getResources().getDrawable(R.drawable.mic_sound_8),
            getResources().getDrawable(R.drawable.mic_sound_9),
            getResources().getDrawable(R.drawable.mic_sound_10),
            getResources().getDrawable(R.drawable.mic_sound_11),
            getResources().getDrawable(R.drawable.mic_sound_12)
        };
    }

    private void initTitleView() {
        QHTitleView qhTitleView = (QHTitleView) findViewById(R.id.titleView);
        qhTitleView.setBackgroundResource(R.drawable.top_select);
        qhTitleView.setTitle("语音发送");
        qhTitleView.setRightView(R.drawable.exit_icon);
        qhTitleView.setClickCallback(new QHTitleView.ClickCallback() {
            @Override
            public void onBackClick() {
                //
            }

            @Override
            public void onRightClick() {
                onBackPressed();
            }
        });
    }

    /**
     * 按住说话listener
     *
     */
    class PressToSpeakListen implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (!isExitsSdcard()) {
                        Toast.makeText(PressTalkActivity.this, "未检测到SD卡", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    if(recorder!=null){
                        recorder.stop();
                        recorder.release();
                        recorder=null;
                    }
                    soundFile = new File(output_Path);
                    recorder = new MediaRecorder();
                    recorder.setAudioSource(MediaRecorder.AudioSource.MIC);//声音来源是话筒
                    recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);//设置格式
                    recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);//设置解码方式
                    recorder.setOutputFile(soundFile.getAbsolutePath());
                    try {
                        recorder.prepare();
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    recorder.start();
                    updateMicStatus();
                    hint_textView.setText("语音实时发送中");
                    return true;
                case MotionEvent.ACTION_MOVE: {
                    //在这里只是做了监听，并没有做与发送相关的处理
//                    if (event.getY() < 0) {
//                        hint_textView.setText("松开手指，取消发送");
//                    } else {
//                        hint_textView.setText("手指上滑，取消发送");
//                    }
                    return true;
                }
                case MotionEvent.ACTION_UP:
                    //抬起手指，停止录音
                    hint_textView.setText("");
                    mic_textView.setBackgroundResource(R.drawable.mic_pic);
                    stopRecord();
                    return true;
                default:

                    return false;
            }
        }
    }

    private void stopRecord(){
        if (soundFile != null && soundFile.exists()){

            try {
                //下面三个参数必须加，不加的话会奔溃，在mediarecorder.stop();
                //报错为：RuntimeException:stop failed
                recorder.setOnErrorListener(null);
                recorder.setOnInfoListener(null);
                recorder.setPreviewDisplay(null);
                // 停止录音
                recorder.stop();
            } catch (IllegalStateException e) {
                // TODO: handle exception
                Log.i("Exception", Log.getStackTraceString(e));
            }catch (RuntimeException e) {
                // TODO: handle exception
                Log.i("Exception", Log.getStackTraceString(e));
            }catch (Exception e) {
                // TODO: handle exception
                Log.i("Exception", Log.getStackTraceString(e));
            }

            // 释放资源
            recorder.release();
            recorder = null;
        }
    }
    private void updateMicStatus() {
        if(recorder != null){
            int ratio = recorder.getMaxAmplitude() / BASE;
            int db = 0;// 分贝   
            if (ratio > 1)
                db = (int) (20 * Math.log10(ratio));
            System.out.println("分贝值：" + db + "     " + Math.log10(ratio));
            //我对着手机说话声音最大的时候，db达到了35左右，
            mHandler.postDelayed(mUpdateMicStatusTimer, SPACE);
            //所以除了2，为的就是对应14张图片
            mHandler.sendEmptyMessage(db/2);
        }
    }
    /**
     * 检测Sdcard是否存在
     *
     * @return
     */
    public  boolean isExitsSdcard() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            return true;
        else
            return false;
    }

}
