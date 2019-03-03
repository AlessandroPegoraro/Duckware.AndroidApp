package com.swe.duckware.megalexa;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class SuccessRegisterFragment extends Fragment {

    public SuccessRegisterFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_success_register, container, false);

        Button button = view.findViewById(R.id.backToHome);
        button.setOnClickListener(x -> backToHome());

        return view;
    }

    public void backToHome() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        getActivity().startActivity(intent);
    }
}
