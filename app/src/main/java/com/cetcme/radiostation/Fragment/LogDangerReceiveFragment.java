package com.cetcme.radiostation.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.cetcme.radiostation.Call.PersonalCallActivity;
import com.cetcme.radiostation.Call.PersonalCallDetailActivity;
import com.cetcme.radiostation.MainActivity;
import com.cetcme.radiostation.R;
import com.qiuhong.qhlibrary.Dialog.QHDialog;
import com.qiuhong.qhlibrary.QHTitleView.QHTitleView;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class LogDangerReceiveFragment extends Fragment {

    String TAG = "LogDangerReceive";

    private ListView mList;
    private List<Map<String, Object>> dataList;
    private MyListAdapter myListAdapter;


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
//        SimpleAdapter simpleAdapter = new SimpleAdapter(
//                getActivity(), getData() ,R.layout.list_cell_log,
//                new String[]{"number", "date", "time", "type"},
//                new int[]{R.id.numberTextView, R.id.dateTextView, R.id.timeTextView, R.id.typeTextView});
        myListAdapter = new MyListAdapter(getActivity(), getData(), R.layout.list_cell_log);
        mList.setAdapter(myListAdapter);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "onItemClick: " + position);
                dataList.get(position).put("readed", "1");
                mList.setAdapter(myListAdapter);

                //TODO: 打开查看界面
                Intent intent = new Intent(getActivity(), PersonalCallDetailActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_left_in_no_alpha, R.anim.push_left_out_no_alpha);

            }
        });
        mList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
                QHDialog qhDialog = new QHDialog(getActivity(),"提示", "是否删除第" + dataList.get(position).get("number") + "条消息？");
                qhDialog.setPositiveButton("删除", R.drawable.button_background_alert, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        deleteData(position);
                        dialog.dismiss();
                    }
                });
                qhDialog.setNegativeButton("取消", 0, null);
                qhDialog.setCancelable(false);
                qhDialog.show();
                return false;
            }
        });
        return view;
    }

    class MyListAdapter extends BaseAdapter {

        private final LayoutInflater mInflater;
        private List<? extends Map<String, ?>> mData;
        private int mResource;

        public MyListAdapter(Context context, List<? extends Map<String, ?>> data,
                             @LayoutRes int resource) {
            mData = data;
            mResource = resource;

            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v;

            if (convertView == null) {
                v = mInflater.inflate(mResource, parent, false);
                TextView numberTV = (TextView) v.findViewById(R.id.numberTextView);
                TextView dateTV = (TextView) v.findViewById(R.id.dateTextView);
                TextView timeTV = (TextView) v.findViewById(R.id.timeTextView);
                TextView typeTV = (TextView) v.findViewById(R.id.typeTextView);

                numberTV.setText(mData.get(position).get("number").toString());
                dateTV.setText(mData.get(position).get("date").toString());
                timeTV.setText(mData.get(position).get("time").toString());
                typeTV.setText(mData.get(position).get("type").toString());

                if (mData.get(position).get("readed") == "0") {
                    setTextView(numberTV, R.color.red, true);
                    setTextView(dateTV, R.color.red, true);
                    setTextView(timeTV, R.color.red, true);
                    setTextView(typeTV, R.color.red, true);
                } else {
                    setTextView(numberTV, R.color.homepage_text_color, false);
                    setTextView(dateTV, R.color.homepage_text_color, false);
                    setTextView(timeTV, R.color.homepage_text_color, false);
                    setTextView(typeTV, R.color.homepage_text_color, false);
                }

            } else {
                v = convertView;
            }

            return v;
        }


    }

    private void setTextView(TextView tv, int color, boolean isBold) {
        tv.setTextColor(getResources().getColor(color));
        tv.getPaint().setFakeBoldText(isBold);
    }

    private List<Map<String, Object>> getData() {
        dataList = new ArrayList<>();

        Map<String, Object> map = new HashMap<>();
        map.put("number","1");
        map.put("date", "26/07/2014");
        map.put("time", "08:23");
        map.put("type", "个人呼叫");
        map.put("readed", "0");
        dataList.add(map);

        map = new HashMap<>();
        map.put("number","2");
        map.put("date", "25/07/2014");
        map.put("time", "09:10");
        map.put("type", "船队呼叫");
        map.put("readed", "1");
        dataList.add(map);

        map = new HashMap<>();
        map.put("number","3");
        map.put("date", "24/07/2014");
        map.put("time", "05:18");
        map.put("type", "海区呼叫");
        map.put("readed", "0");
        dataList.add(map);

        map = new HashMap<>();
        map.put("number","4");
        map.put("date", "23/07/2014");
        map.put("time", "15:10");
        map.put("type", "测试呼叫");
        map.put("readed", "0");
        dataList.add(map);

        map = new HashMap<>();
        map.put("number","5");
        map.put("date", "22/07/2014");
        map.put("time", "21:20");
        map.put("type", "个人呼叫");
        map.put("readed", "1");
        dataList.add(map);

        return dataList;
    }

    private void deleteData(int position) {
        Toast.makeText(getActivity(), "已删除", Toast.LENGTH_SHORT).show();
        dataList.remove(position);
        mList.setAdapter(myListAdapter);
    }

}
