package com.cetcme.radiostation.Fragment;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.cetcme.radiostation.R;
import com.cetcme.radiostation.RecordActivity;
import com.cetcme.radiostation.VoiceShareActivity;
import com.cetcme.radiostation.pcm.AudioPlayerDemoActivity;
import com.qiuhong.qhlibrary.QHTitleView.QHTitleView;


public class HomepageFragment extends Fragment {


    private Spinner agc_spinner;
    private Spinner att_spinner;
    private Spinner sql_spinner;
    private Spinner pow_spinner;

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

        agc_spinner = (Spinner) view.findViewById(R.id.agc_spinner);
        att_spinner = (Spinner) view.findViewById(R.id.att_spinner);
        sql_spinner = (Spinner) view.findViewById(R.id.sql_spinner);
        pow_spinner = (Spinner) view.findViewById(R.id.pow_spinner);




        return view;
    }

    private void initTitleView(View view) {
        QHTitleView qhTitleView = (QHTitleView) view.findViewById(R.id.qhTitleView);
        qhTitleView.setTitle(getString(R.string.main_tab_name_1));
        qhTitleView.setBackView(0);
        qhTitleView.setRightView(0);
        qhTitleView.setClickCallback(new QHTitleView.ClickCallback() {
            @Override
            public void onBackClick() {
                //
            }

            @Override
            public void onRightClick() {
                //
            }
        });
    }



}
