package com.cetcme.radiostation.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.cetcme.radiostation.R;
import com.qiuhong.qhlibrary.QHTitleView.QHTitleView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LogDangerReceiveFragment extends Fragment {

    String TAG = "LogDangerReceive";

    private ListView mList;
    List<Map<String, Object>> dataList;


    public static LogDangerReceiveFragment newInstance(String param1) {
        LogDangerReceiveFragment fragment = new LogDangerReceiveFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        fragment.setArguments(args);
        return fragment;
    }

    public LogDangerReceiveFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_log_receive, container, false);
//        Bundle bundle = getArguments();
//        String agrs1 = bundle.getString("agrs1");


        mList = (ListView) view.findViewById(R.id.list);
        SimpleAdapter simpleAdapter = new SimpleAdapter(
                getActivity(), getData() ,R.layout.list_cell_log,
                new String[]{"number", "date", "time", "type"},
                new int[]{R.id.numberTextView, R.id.dateTextView, R.id.timeTextView, R.id.typeTextView});
        mList.setAdapter(simpleAdapter);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "onItemClick: " + position);
            }
        });

        return view;
    }

    private List<Map<String, Object>> getData() {
        dataList = new ArrayList<>();

        Map<String, Object> map = new HashMap<>();
        map.put("number","1");
        map.put("date", "26/07/2014");
        map.put("time", "08:23");
        map.put("type", "个人呼叫");
        dataList.add(map);

        map = new HashMap<>();
        map.put("number","2");
        map.put("date", "25/07/2014");
        map.put("time", "09:10");
        map.put("type", "船队呼叫");
        dataList.add(map);

        map = new HashMap<>();
        map.put("number","3");
        map.put("date", "24/07/2014");
        map.put("time", "05:18");
        map.put("type", "海区呼叫");
        dataList.add(map);

        map = new HashMap<>();
        map.put("number","4");
        map.put("date", "23/07/2014");
        map.put("time", "15:10");
        map.put("type", "测试呼叫");
        dataList.add(map);

        map = new HashMap<>();
        map.put("number","5");
        map.put("date", "22/07/2014");
        map.put("time", "21:20");
        map.put("type", "个人呼叫");
        dataList.add(map);

        return dataList;
    }

}
