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

import com.cetcme.radiostation.Call.PersonalCallActivity;
import com.cetcme.radiostation.R;
import com.qiuhong.qhlibrary.QHTitleView.QHTitleView;

import java.util.ArrayList;
import java.util.List;

public class MessageFragment extends Fragment implements AdapterView.OnItemClickListener{

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

        initTitleView(view);

        mList = (ListView) view.findViewById(R.id.list);
        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(),R.layout.list_cell_one_title, R.id.titleTextView, getData());
        mList.setAdapter(arrayAdapter);
        mList.setOnItemClickListener(this);

        return view;
    }

    private void initTitleView(View view) {
        QHTitleView qhTitleView = (QHTitleView) view.findViewById(R.id.qhTitleView);
        qhTitleView.setTitle(getString(R.string.main_tab_name_2));
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
        String[] contentList = getResources().getStringArray(R.array.messageList);
        for (String str : contentList ){
            data.add(str);
        }
        return data;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i(TAG, "onItemClick: " + position);
        Intent intent = new Intent(getActivity(), PersonalCallActivity.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.push_left_in_no_alpha, R.anim.push_left_out_no_alpha);
    }
}
