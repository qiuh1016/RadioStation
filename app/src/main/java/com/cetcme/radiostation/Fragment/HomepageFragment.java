package com.cetcme.radiostation.Fragment;

import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cetcme.radiostation.Main2Activity;
import com.cetcme.radiostation.R;
import com.cetcme.radiostation.voiceSocket.VoiceSocketActivity;
import com.qiuhong.qhlibrary.Dialog.QHDialog;
import com.qiuhong.qhlibrary.QHTitleView.QHTitleView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class HomepageFragment extends Fragment implements View.OnClickListener{

    private String TAG = "HomepageFragment";

    private Spinner agc_spinner;
    private Spinner att_spinner;
    private Spinner sql_spinner;
    private Spinner pow_spinner;

    private TextView mmsi_tv;
    private TextView ch_tv;
    private TextView tx_tv;
    private TextView rx_tv;
    private TextView n_tv;
    private TextView w_tv;
    private TextView utc_tv;

    private TextView circle_tv;
    private TextView progress_tv;

    private TextView conversation_number_tv;

    public static final int MSG_UPDATA_TIME = 1;

    public static HomepageFragment newInstance(String param1) {
        HomepageFragment fragment = new HomepageFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        fragment.setArguments(args);
        return fragment;
    }

    public HomepageFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homepage, container, false);
//        Bundle bundle = getArguments();
//        String agrs1 = bundle.getString("agrs1");

        initTitleView(view);
        initSpinner(view);
        initView(view);

        mmsi_tv.setText("MMSI    " + "123456789");
        ch_tv.setText("0200");
        tx_tv.setText("2182.00");
        rx_tv.setText("2182.00");

        n_tv.setText("34°42.2800N");
        w_tv.setText("135°19.5900W");

        //更新系统时间
        new TimeThread().start();


        return view;
    }

    private void initTitleView(View view) {
        QHTitleView qhTitleView = (QHTitleView) view.findViewById(R.id.qhTitleView);
        qhTitleView.setTitle(getString(R.string.main_tab_name_1));
        qhTitleView.setBackView(0);
        qhTitleView.setRightView(R.drawable.connect_icon);
        qhTitleView.setClickCallback(new QHTitleView.ClickCallback() {
            @Override
            public void onBackClick() {
                //
            }

            @Override
            public void onRightClick() {
                ((Main2Activity) getActivity()).connectServer();
                //
            }
        });
    }

    private void initSpinner(View view) {
        agc_spinner = (Spinner) view.findViewById(R.id.agc_spinner);
        att_spinner = (Spinner) view.findViewById(R.id.att_spinner);
        sql_spinner = (Spinner) view.findViewById(R.id.sql_spinner);
        pow_spinner = (Spinner) view.findViewById(R.id.pow_spinner);



        agc_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, " onItemSelected: " + "agc_spinner - " + position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        att_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, " onItemSelected: " + "att_spinner - " + position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sql_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, " onItemSelected: " + "sql_spinner - " + position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        pow_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, " onItemSelected: " + "pow_spinner - " + position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initView(View view) {
        view.findViewById(R.id.voiceTextView).setOnClickListener(this);
        view.findViewById(R.id.voice_layout).setOnClickListener(this);
        view.findViewById(R.id.conversation_icon_tv).setOnClickListener(this);

        mmsi_tv = (TextView) view.findViewById(R.id.mmsi_textview);
        conversation_number_tv = (TextView) view.findViewById(R.id.conversation_number_tv);
        conversation_number_tv.setOnClickListener(this);

        ch_tv = (TextView) view.findViewById(R.id.ch_tv);
        tx_tv = (TextView) view.findViewById(R.id.tx_tv);
        rx_tv = (TextView) view.findViewById(R.id.rx_tv);

        n_tv = (TextView) view.findViewById(R.id.n_tv);
        w_tv = (TextView) view.findViewById(R.id.w_tv);
        utc_tv = (TextView) view.findViewById(R.id.utc_tv);

        SimpleDateFormat df = new SimpleDateFormat("HH:mm");//设置日期格式
        utc_tv.setText("UTC " + df.format(new Date()));// new Date()为获取当前系统时间

        circle_tv = (TextView) view.findViewById(R.id.circle_tv);
        progress_tv = (TextView) view.findViewById(R.id.progress_tv);

        ch_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final QHDialog chQHDialog = new QHDialog(getActivity(), "频道选择", "");
                chQHDialog.setPositiveButton("确认", 0, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        dialog.dismiss();
                        String ch = chQHDialog.getEditTextText();

                        QHDialog errQHDialog = new QHDialog(getActivity(), "提示", "输入有误");
                        errQHDialog.setOnlyOneButtonText("好的");

                        if (!isNumeric(ch) || ch.equals("")) {
                            errQHDialog.show();
                            return;
                        }

                        switch (ch.length()) {
                            case 1:
                                ch = "000" + ch;
                                ch_tv.setText(ch);
                                break;
                            case 2:
                                ch = "00" + ch;
                                ch_tv.setText(ch);
                                break;
                            case 3:
                                ch = "0" + ch;
                                ch_tv.setText(ch);
                                break;
                            case 4:
                                ch_tv.setText(ch);
                                break;
                            default:
                                errQHDialog.show();
                                break;
                        }

                    }
                });
                chQHDialog.setNegativeButton("取消", 0, null);
                chQHDialog.setEditText("请输入0～9999的频道编号");
                chQHDialog.setCancelable(false);
                chQHDialog.show();
            }
        });

        tx_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final QHDialog txQHDialog = new QHDialog(getActivity(), "发送频率选择", "");
                txQHDialog.setPositiveButton("确认", 0, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        dialog.dismiss();
                        String tx = txQHDialog.getEditTextText();

                        QHDialog errQHDialog = new QHDialog(getActivity(), "提示", "输入有误");
                        errQHDialog.setOnlyOneButtonText("好的");

                        if (!isNum(tx) || tx.equals("")) {
                            errQHDialog.show();
                            return;
                        }

                        DecimalFormat decimalFormat = new DecimalFormat(".00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
                        String a = decimalFormat.format(Double.valueOf(tx));//format 返回的是字符串

                        switch (a.length()) {
                            case 4:
                                tx = "000" + a;
                                tx_tv.setText(tx);
                                break;
                            case 5:
                                tx = "00" + a;
                                tx_tv.setText(tx);
                                break;
                            case 6:
                                tx = "0" + a;
                                tx_tv.setText(tx);
                                break;
                            case 7:
                                tx_tv.setText(a);
                                break;
                            default:
                                errQHDialog.show();
                                break;
                        }

                    }
                });
                txQHDialog.setNegativeButton("取消", 0, null);
                txQHDialog.setEditText("请输入0～9999的2位小数");
                txQHDialog.setCancelable(false);
                txQHDialog.show();
            }
        });

        rx_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final QHDialog rxQHDialog = new QHDialog(getActivity(), "接收频率选择", "");
                rxQHDialog.setPositiveButton("确认", 0, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        dialog.dismiss();
                        String rx = rxQHDialog.getEditTextText();

                        QHDialog errQHDialog = new QHDialog(getActivity(), "提示", "输入有误");
                        errQHDialog.setOnlyOneButtonText("好的");

                        if (!isNum(rx) || rx.equals("")) {
                            errQHDialog.show();
                            return;
                        }

                        DecimalFormat decimalFormat = new DecimalFormat(".00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
                        String a = decimalFormat.format(Double.valueOf(rx));//format 返回的是字符串

                        switch (a.length()) {
                            case 4:
                                rx = "000" + a;
                                rx_tv.setText(rx);
                                break;
                            case 5:
                                rx = "00" + a;
                                rx_tv.setText(rx);
                                break;
                            case 6:
                                rx = "0" + a;
                                rx_tv.setText(rx);
                                break;
                            case 7:
                                rx_tv.setText(a);
                                break;
                            default:
                                errQHDialog.show();
                                break;
                        }

                    }
                });
                rxQHDialog.setNegativeButton("取消", 0, null);
                rxQHDialog.setEditText("请输入0～9999的2位小数");
                rxQHDialog.setCancelable(false);
                rxQHDialog.show();
            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.voiceTextView:
//            case R.id.voice_layout:
                Intent intent = new Intent(getActivity(), VoiceSocketActivity.class);
                startActivity(intent);
                break;
            case R.id.conversation_number_tv:
            case R.id.conversation_icon_tv:
                Toast.makeText(getActivity(), "conversation", Toast.LENGTH_SHORT).show();
            default:
                break;
        }
    }

    public static boolean isNumeric(String str){
        for (int i = 0; i < str.length(); i++){
//            System.out.println(str.charAt(i));
            if (!Character.isDigit(str.charAt(i))){
                return false;
            }
        }
        return true;
    }

    public static boolean isNum(String str){
        return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //通过消息的内容msg.what  分别更新ui
            switch (msg.what) {
                case MSG_UPDATA_TIME:
                    //获取到系统当前时间 long类型
                    long time = System.currentTimeMillis();
                    //将long类型的时间转换成日历格式
                    Date data = new Date(time);
                    // 转换格式，年月日时分秒 星期  的格式
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                    //显示在textview上，通过转换格式
                    utc_tv.setText("UTC " + simpleDateFormat.format(data));
                    break;
                default:
                    break;
            }
        }
    };

    //开一个线程继承Thread
    public class TimeThread extends Thread {
        //重写run方法
        @Override
        public void run() {
            super.run();
            // do-while  一 什么什么 就
            do {
                try {
                    //每隔一秒 发送一次消息
                    Thread.sleep(1000);
                    Message msg = new Message();
                    //消息内容 为MSG_ONE
                    msg.what = MSG_UPDATA_TIME;
                    //发送
                    handler.sendMessage(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
        }
    }
}
