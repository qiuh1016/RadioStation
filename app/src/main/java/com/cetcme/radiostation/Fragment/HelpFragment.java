package com.cetcme.radiostation.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cetcme.radiostation.R;

public class HelpFragment extends Fragment {

    public static HelpFragment newInstance(String param1) {
        HelpFragment fragment = new HelpFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        fragment.setArguments(args);
        return fragment;
    }

    public HelpFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_help, container, false);
        Bundle bundle = getArguments();
        String agrs1 = bundle.getString("agrs1");

        return view;
    }

}
