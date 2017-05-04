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
import com.qiuhong.qhlibrary.QHTitleView.QHTitleView;

import java.util.ArrayList;
import java.util.List;


public class FunctionFragment extends Fragment {

    String TAG = "FunctionFragment";

    private ListView mList;

    public static FunctionFragment newInstance(String param1) {
        FunctionFragment fragment = new FunctionFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        fragment.setArguments(args);
        return fragment;
    }

    public FunctionFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_function, container, false);
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
            }
        });

        return view;
    }

    private void initTitleView(View view) {
        QHTitleView qhTitleView = (QHTitleView) view.findViewById(R.id.qhTitleView);
        qhTitleView.setTitle(getString(R.string.main_tab_name_4));
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
        String[] contentList = getResources().getStringArray(R.array.functionList);
        for (String str : contentList ){
            data.add(str);
        }
        return data;
    }

}
