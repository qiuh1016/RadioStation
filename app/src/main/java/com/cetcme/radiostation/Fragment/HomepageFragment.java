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

import com.cetcme.radiostation.DialogView.AgcSelectActivity;
import com.cetcme.radiostation.DialogView.AttSelectActivity;
import com.cetcme.radiostation.DialogView.PowSelectActivity;
import com.cetcme.radiostation.DialogView.SqlSelectActivity;
import com.cetcme.radiostation.DialogView.SsbSelectActivity;
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

    private TextView ssb_TextView;
    private TextView agc_TextView;
    private TextView sql_TextView;
    private TextView att_TextView;
    private TextView pow_TextView;

    private int lastSsbState;
    private int lastAgcState;
    private String lastAgcMState;
    private int lastSqlState;
    private String lastSqlMState;
    private int lastAttState;
    private int lastPowState;


    private TextView mmsi_tv;
    private TextView ch_tv;
    private TextView ch_name_tv;
    private TextView tx_tv;
    private TextView rx_tv;
    private TextView gpsY_tv;
    private TextView gpsX_tv;
    private TextView utc_tv;

    private String mmsi;
    private int ch;
    private String ch_name;
    private double tx;
    private double rx;
    private String gpsX;
    private String gpsY;

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




        //更新系统时间
        new TimeThread().start();

        //获取系统设置
        initSystemSetting();

        //更新ui
        updateView();

        return view;
    }

    private void initSystemSetting() {
        lastSsbState = 0;

        lastAgcState = 0;
        lastAgcMState = "";

        lastSqlState = 0;
        lastSqlMState = "";

        lastAttState = 0;
        lastPowState = 0;

        mmsi = "123456789";
        ch = 200;
        ch_name = "USER2";
        tx = 2182.00;
        rx = 2182.00;

        gpsY = "34°42.2800N";
        gpsX = "135°19.5900W";

    }

    public void updateView() {
        mmsi_tv.setText(mmsi);
        ch_name_tv.setText(ch_name);
        gpsY_tv.setText(gpsY);
        gpsX_tv.setText(gpsX);

        setCH(ch + "");
        setTX(tx + "");
        setRX(rx + "");

    }

    private void setCH(String ch) {
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
                break;
        }
    }


    private void setTX(String a) {
        DecimalFormat decimalFormat = new DecimalFormat(".00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        a = decimalFormat.format(Double.valueOf(a));//format 返回的是字符串
        switch (a.length()) {
            case 4:
                a = "0000" + a;
                break;
            case 5:
                a = "000" + a;
                break;
            case 6:
                a = "00" + a;
                break;
            case 7:
                a = "0" + a;
                break;
            default:
                break;
        }
        tx_tv.setText(a);
    }

    private void setRX(String a) {
        DecimalFormat decimalFormat = new DecimalFormat(".00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        a = decimalFormat.format(Double.valueOf(a));//format 返回的是字符串
        switch (a.length()) {
            case 4:
                a = "0000" + a;
                break;
            case 5:
                a = "000" + a;
                break;
            case 6:
                a = "00" + a;
                break;
            case 7:
                a = "0" + a;
                break;
            default:
                break;
        }
        rx_tv.setText(a);
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
                switch (position) {
                    case 0:
                        lastSqlState = 0;
                        break;
                    case 1:
                        lastSqlState = 1;
                        break;
                    case 2:
                        final QHDialog sqlQHDialog = new QHDialog(getActivity(), "SQL选择", "");
                        sqlQHDialog.setNegativeButton("确认", 0, new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which){
                                dialog.dismiss();
                                String sql = sqlQHDialog.getEditTextText();

                                QHDialog errQHDialog = new QHDialog(getActivity(), "提示", "输入有误");

                                errQHDialog.setPositiveButton("好的", 0, new DialogInterface.OnClickListener(){
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
//                                        resetSql();
                                        sqlQHDialog.show();
                                    }
                                });

                                if (!isNumeric(sql) || sql.equals("")) {
                                    errQHDialog.show();
                                    return;
                                }

                                Log.i(TAG, "onClick: " + sql);

                                switch (sql.length()) {
                                    case 1:
                                        sql = "0" + sql;
                                        sql_TextView.setText(sql);
                                        sql_spinner.setVisibility(View.GONE);
                                        sql_TextView.setVisibility(View.VISIBLE);
                                        lastSqlState = 2;
                                        lastSqlMState = sql;
                                        break;
                                    case 2:
                                        sql_TextView.setText(sql);
                                        sql_spinner.setVisibility(View.GONE);
                                        sql_TextView.setVisibility(View.VISIBLE);
                                        lastSqlState = 2;
                                        lastSqlMState = sql;
                                        break;
                                    default:
                                        errQHDialog.show();
                                        resetSql();
                                        break;
                                }

                            }
                        });
                        sqlQHDialog.setPositiveButton("取消", 0,  new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which){
                                dialog.dismiss();
                                //TODO: 取消输入的话 返回spinner原先的状态
                                resetSql();
                            }
                        });
                        sqlQHDialog.setEditText("请输入0～99的sql值");
                        sqlQHDialog.setCancelable(false);
                        sqlQHDialog.show();
                        break;
                    default:
                        resetSql();
                }
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

        ssb_TextView = (TextView) view.findViewById(R.id.ssb_textView);
        ssb_TextView.setOnClickListener(this);

        agc_TextView = (TextView) view.findViewById(R.id.agc_TextView);
        agc_TextView.setOnClickListener(this);
        view.findViewById(R.id.agc_layout).setOnClickListener(this);

        sql_TextView = (TextView) view.findViewById(R.id.sql_TextView);
        sql_TextView.setOnClickListener(this);
        view.findViewById(R.id.sql_layout).setOnClickListener(this);

        att_TextView = (TextView) view.findViewById(R.id.att_TextView);
        att_TextView.setOnClickListener(this);
        view.findViewById(R.id.att_layout).setOnClickListener(this);

        pow_TextView = (TextView) view.findViewById(R.id.pow_TextView);
        pow_TextView.setOnClickListener(this);
        view.findViewById(R.id.pow_layout).setOnClickListener(this);



        mmsi_tv = (TextView) view.findViewById(R.id.mmsi_tv);
        conversation_number_tv = (TextView) view.findViewById(R.id.conversation_number_tv);
        conversation_number_tv.setOnClickListener(this);

        ch_tv = (TextView) view.findViewById(R.id.ch_tv);
        ch_name_tv = (TextView) view.findViewById(R.id.ch_name_tv);
        tx_tv = (TextView) view.findViewById(R.id.tx_tv);
        rx_tv = (TextView) view.findViewById(R.id.rx_tv);

        gpsY_tv = (TextView) view.findViewById(R.id.gpsY_tv);
        gpsX_tv = (TextView) view.findViewById(R.id.gpsX_tv);
        utc_tv = (TextView) view.findViewById(R.id.utc_tv);

        SimpleDateFormat df = new SimpleDateFormat("HH:mm");//设置日期格式
        utc_tv.setText("UTC " + df.format(new Date()));// new Date()为获取当前系统时间

        circle_tv = (TextView) view.findViewById(R.id.circle_tv);
        progress_tv = (TextView) view.findViewById(R.id.progress_tv);

        ch_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final QHDialog chQHDialog = new QHDialog(getActivity(), "频道选择", "");
                chQHDialog.setNegativeButton("确认", 0, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        dialog.dismiss();
                        String ch = chQHDialog.getEditTextText();

                        QHDialog errQHDialog = new QHDialog(getActivity(), "提示", "输入有误");
                        errQHDialog.setOnlyOneButtonText("好的");

                        if (!isNumeric(ch) || ch.equals("") || ch.length() > 4) {
                            errQHDialog.show();
                        } else {
                            setCH(ch);
                        }

                    }
                });
                chQHDialog.setPositiveButton("取消", 0, null);
                chQHDialog.setEditText("请输入0～9999的频道编号");
                chQHDialog.setCancelable(false);
                chQHDialog.show();
            }
        });

        tx_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final QHDialog txQHDialog = new QHDialog(getActivity(), "发送频率选择", "");
                txQHDialog.setNegativeButton("确认", 0, new DialogInterface.OnClickListener(){
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

                        if (a.length() < 4 || a.length() > 8) {
                            errQHDialog.show();
                        } else {
                            setTX(a);
                        }

                    }
                });
                txQHDialog.setPositiveButton("取消", 0, null);
                txQHDialog.setEditText("请输入0～99999的2位小数");
                txQHDialog.setCancelable(false);
                txQHDialog.show();
            }
        });

        rx_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final QHDialog rxQHDialog = new QHDialog(getActivity(), "接收频率选择", "");
                rxQHDialog.setNegativeButton("确认", 0, new DialogInterface.OnClickListener(){
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

                        if (a.length() < 4 || a.length() > 8) {
                            errQHDialog.show();
                        } else {
                            setRX(a);
                        }

                    }
                });
                rxQHDialog.setPositiveButton("取消", 0, null);
                rxQHDialog.setEditText("请输入0～99999的2位小数");
                rxQHDialog.setCancelable(false);
                rxQHDialog.show();
            }
        });

    }

    private void resetSql() {
        if (lastSqlState != 2) {
            sql_spinner.setSelection(lastSqlState);
            sql_spinner.setVisibility(View.VISIBLE);
            sql_TextView.setVisibility(View.GONE);
        } else {
            sql_spinner.setVisibility(View.GONE);
            sql_TextView.setVisibility(View.VISIBLE);
            sql_TextView.setText(lastSqlMState);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.voiceTextView:
//            case R.id.voice_layout:
                intent = new Intent(getActivity(), VoiceSocketActivity.class);
                startActivity(intent);
                break;
            case R.id.conversation_number_tv:
            case R.id.conversation_icon_tv:
                Toast.makeText(getActivity(), "conversation", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ssb_textView:
                intent = new Intent(getActivity(), SsbSelectActivity.class);
                intent.putExtra("ssbState", lastSsbState);
                startActivityForResult(intent, 1);
                break;
            case R.id.agc_layout:
            case R.id.agc_TextView:
                intent = new Intent(getActivity(), AgcSelectActivity.class);
                intent.putExtra("radioNumber", lastAgcState);
                intent.putExtra("MNumber", lastAgcMState);
                startActivityForResult(intent, 2);
                break;
            case R.id.sql_layout:
            case R.id.sql_TextView:
                intent = new Intent(getActivity(), SqlSelectActivity.class);
                intent.putExtra("radioNumber", lastSqlState);
                intent.putExtra("MNumber", lastSqlMState);
                startActivityForResult(intent, 0);
                break;
            case R.id.att_layout:
            case R.id.att_TextView:
                intent = new Intent(getActivity(), AttSelectActivity.class);
                intent.putExtra("attState", lastAttState);
                startActivityForResult(intent, 3);
                break;
            case R.id.pow_layout:
            case R.id.pow_TextView:
                intent = new Intent(getActivity(), PowSelectActivity.class);
                intent.putExtra("powState", lastPowState);
                startActivityForResult(intent, 4);
                break;
            default:
                break;
        }
    }

    public static boolean isNumeric(String str){
        for (int i = 0; i < str.length(); i++){
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        int radioNumber = data.getIntExtra("radioNumber", 0);
        String MNumber = data.getStringExtra("MNumber");
        // 根据上面发送过去的请求码来区别
        switch (requestCode) {
            case 0:
                lastSqlState = radioNumber;
                lastSqlMState = MNumber;
                if (radioNumber == 0) {
                    sql_TextView.setText(getString(R.string.homepage_sql_0));
                } else if (radioNumber == 1) {
                    sql_TextView.setText(getString(R.string.homepage_sql_1));
                } else if (radioNumber == 2) {
                    sql_TextView.setText(MNumber);
                }
                break;
            case 1:
                int ssbState = data.getIntExtra("ssbState", 0);
                lastSsbState = ssbState;
                if (ssbState == 0) {
                    ssb_TextView.setText(getString(R.string.homepage_ssb_0));
                    ssb_TextView.setTextSize(50);  //DensityUtil.px2dip(getActivity(), 50)
                } else if (ssbState == 1) {
                    ssb_TextView.setText(getString(R.string.homepage_ssb_1));
                    ssb_TextView.setTextSize(50);
                } else if (ssbState == 2) {
                    ssb_TextView.setText(getString(R.string.homepage_ssb_2));
                    ssb_TextView.setTextSize(50);
                } else if (ssbState == 3) {
                    ssb_TextView.setText(getString(R.string.homepage_ssb_3));
                    ssb_TextView.setTextSize(35);
                }
                break;
            case 2:
                lastAgcState = radioNumber;
                lastAgcMState = MNumber;
                if (radioNumber == 0) {
                    agc_TextView.setText(getString(R.string.homepage_agc_0));
                } else if (radioNumber == 1) {
                    agc_TextView.setText(getString(R.string.homepage_agc_1));
                } else if (radioNumber == 2) {
                    agc_TextView.setText(MNumber);
                }
                break;
            case 3:
                int attState = data.getIntExtra("attState", 0);
                lastAttState = attState;
                if (attState == 0) {
                    att_TextView.setText(getString(R.string.homepage_att_0));
                } else if (attState == 1) {
                    att_TextView.setText(getString(R.string.homepage_att_1));
                } else if (attState == 2) {
                    att_TextView.setText(getString(R.string.homepage_att_2));
                }
                break;
            case 4:
                int powState = data.getIntExtra("powState", 0);
                lastPowState = powState;
                if (powState == 0) {
                    pow_TextView.setText(getString(R.string.homepage_pow_0));
                } else if (powState == 1) {
                    pow_TextView.setText(getString(R.string.homepage_pow_1));
                } else if (powState == 2) {
                    pow_TextView.setText(getString(R.string.homepage_pow_2));
                }
                break;
            default:
                break;
        }
    }
}
