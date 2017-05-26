package com.cetcme.radiostation.Fragment;

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
import com.qiuhong.qhlibrary.QHTitleView.QHTitleView;

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
}
