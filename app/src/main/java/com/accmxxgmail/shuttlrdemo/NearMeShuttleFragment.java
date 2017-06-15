package com.accmxxgmail.shuttlrdemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class NearMeShuttleFragment extends Fragment {

    TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String str = this.getArguments().getString("fragnum");
        View view = inflater.inflate(R.layout.fragment_near_me_shuttle, container, false);
        textView = (TextView)view.findViewById(R.id.near_me_time);
        textView.setText(str);
        return view;
    }

    public void getTime(String text){
        textView.setText(text);
    }
}
