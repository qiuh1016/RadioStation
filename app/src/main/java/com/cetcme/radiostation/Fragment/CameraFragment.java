package com.cetcme.radiostation.Fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cetcme.radiostation.R;
import com.cetcme.radiostation.RecordActivity;
import com.cetcme.radiostation.VoiceShareActivity;
import com.cetcme.radiostation.pcm.AudioPlayerDemoActivity;


public class CameraFragment extends Fragment {

    public static CameraFragment newInstance(String param1) {
        CameraFragment fragment = new CameraFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        fragment.setArguments(args);
        return fragment;
    }

    public CameraFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);
        Bundle bundle = getArguments();
        String agrs1 = bundle.getString("agrs1");

        view.findViewById(R.id.record_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RecordActivity.class);
                startActivity(intent);
            }
        });

        view.findViewById(R.id.play_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AudioPlayerDemoActivity.class);
                startActivity(intent);
            }
        });

        view.findViewById(R.id.share_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), VoiceShareActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

}
