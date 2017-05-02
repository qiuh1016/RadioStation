package com.cetcme.radiostation.Fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.cetcme.radiostation.R;

import java.util.ArrayList;
import java.util.List;

public class MessageFragment extends Fragment {

    String TAG = "MessageFragment";

    private ListView mList;


    public static MessageFragment newInstance(String param1) {
        MessageFragment fragment = new MessageFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        fragment.setArguments(args);
        return fragment;
    }

    public MessageFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
//        Bundle bundle = getArguments();
//        String agrs1 = bundle.getString("agrs1");

        mList = (ListView) view.findViewById(R.id.messageList);
        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(),R.layout.list_cell_message, R.id.titleTextView, getData());
        mList.setAdapter(arrayAdapter);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "onItemClick: " + position);
            }
        });

        return view;
    }

    private List<String> getData() {
        List<String> data = new ArrayList<>();
        String[] messageList = getResources().getStringArray(R.array.messageList);
        for (String str : messageList ){
            data.add(str);
        }
        return data;
    }

}
