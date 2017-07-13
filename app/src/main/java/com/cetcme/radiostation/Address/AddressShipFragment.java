package com.cetcme.radiostation.Address;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.cetcme.radiostation.Call.PersonalCallDetailActivity;
import com.cetcme.radiostation.R;
import com.cetcme.radiostation.Setting.AddressActivity;
import com.qiuhong.qhlibrary.Dialog.QHDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AddressShipFragment extends Fragment {

    String TAG = "AddressShipFragment";

    private ListView mList;
    private List<Map<String, Object>> dataList;
    private SimpleAdapter simpleAdapter;

    public static AddressShipFragment newInstance(String param1) {
        AddressShipFragment fragment = new AddressShipFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        fragment.setArguments(args);
        return fragment;
    }

    public AddressShipFragment() {

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
        simpleAdapter = new SimpleAdapter(
                getActivity(), getData() ,R.layout.list_cell_address,
                new String[]{"number", "short", "full"},
                new int[]{R.id.numberTextView, R.id.shortTextView, R.id.fullTextView});
        mList.setAdapter(simpleAdapter);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (AddressActivity.forAddress) {
                    Intent mIntent = new Intent();
                    mIntent.putExtra("address", dataList.get(position).get("full").toString());
                    getActivity().setResult(1, mIntent);
                    getActivity().onBackPressed();
                }

                Log.i(TAG, "onItemClick: " + position);

            }
        });
        mList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
                QHDialog qhDialog = new QHDialog(getActivity(),"提示", "是否删除第" + dataList.get(position).get("number") + "条地址？");
                qhDialog.setPositiveButton("删除", R.drawable.button_background_alert, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        deleteData(position);
                        dialog.dismiss();
                    }
                });
                qhDialog.setNegativeButton("取消", 0, null);
                qhDialog.show();
                return false;
            }
        });

        return view;
    }


    private List<Map<String, Object>> getData() {
        dataList = new ArrayList<>();

        Map<String, Object> map = new HashMap<>();
        map.put("number","1");
        map.put("short", "12D");
        map.put("full", "123456789");
        dataList.add(map);

        map = new HashMap<>();
        map.put("number","2");
        map.put("short", "13A");
        map.put("full", "374562823");
        dataList.add(map);

        map = new HashMap<>();
        map.put("number","3");
        map.put("short", "10B");
        map.put("full", "987654321");
        dataList.add(map);


        return dataList;
    }

    private void deleteData(int position) {
        Toast.makeText(getActivity(), "已删除", Toast.LENGTH_SHORT).show();
        dataList.remove(position);
        mList.setAdapter(simpleAdapter);
    }

}
