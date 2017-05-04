package com.cetcme.radiostation.Fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.cetcme.radiostation.Setting.AddressActivity;
import com.cetcme.radiostation.R;
import com.cetcme.radiostation.Setting.DSCSettingActivity;
import com.cetcme.radiostation.Setting.SSBSettingActivity;
import com.qiuhong.qhlibrary.QHTitleView.QHTitleView;

import java.util.ArrayList;
import java.util.List;

public class SettingFragment extends Fragment {

    String TAG = "SettingFragment";

    private ListView mList;

    public static SettingFragment newInstance(String param1) {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        fragment.setArguments(args);
        return fragment;
    }

    public SettingFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
//        Bundle bundle = getArguments();
//        String agrs1 = bundle.getString("agrs1");

        initTitleView(view);

        mList = (ListView) view.findViewById(R.id.list);
        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(),R.layout.list_cell_one_title, R.id.titleTextView, getData());
        mList.setAdapter(arrayAdapter);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "onItemClick: " + position);
                Intent intent = null;
                switch (position) {
                    case 0:
                        intent = new Intent(getActivity(), AddressActivity.class);
                        break;
                    case 1:
                        intent = new Intent(getActivity(), SSBSettingActivity.class);
                        break;
                    case 2:
                        intent = new Intent(getActivity(), DSCSettingActivity.class);
                        break;
                }
                if (intent == null) {
                    return;
                }
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_left_in_no_alpha, R.anim.push_left_out_no_alpha);
            }
        });

        return view;
    }

    private void initTitleView(View view) {
        QHTitleView qhTitleView = (QHTitleView) view.findViewById(R.id.qhTitleView);
        qhTitleView.setTitle(getString(R.string.main_tab_name_5));
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

    private List<String> getData() {
        List<String> data = new ArrayList<>();
        String[] contentList = getResources().getStringArray(R.array.settingList);
        for (String str : contentList ){
            data.add(str);
        }
        return data;
    }

}
