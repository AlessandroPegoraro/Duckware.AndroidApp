package com.swe.duckware.megalexa;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class StatusActivity extends AppCompatActivity {

    private static final String TAG_STATUS_FRAGMENT = "TAG_STATUS_FRAGMENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        int status = getIntent().getIntExtra("REQUEST_STATUS", -1);
        String actionType = getIntent().getStringExtra("ACTION_TYPE");

        FragmentManager fm = getSupportFragmentManager();

        if (savedInstanceState == null) {
            FragmentTransaction ft = fm.beginTransaction();

            switch (status) {
                case 200:
                    ft.add(R.id.status_activity_frame, new SuccessRegisterFragment(), TAG_STATUS_FRAGMENT);
                    break;
                case 500:
                        if ((actionType != null) && actionType.equals("LOGIN_TYPE"))
                            ft.add(R.id.status_activity_frame, new UsernameInvalidFragment(), TAG_STATUS_FRAGMENT);
                        else
                            ft.add(R.id.status_activity_frame, new DuplicateUsernameFragment(), TAG_STATUS_FRAGMENT);
                    break;
                default:
                    ft.add(R.id.status_activity_frame, new ConnectionErrorFragment(), TAG_STATUS_FRAGMENT);
                    break;
            }

            ft.commitNow();
        } else {
            fm.findFragmentByTag(TAG_STATUS_FRAGMENT);
        }
    }
}
