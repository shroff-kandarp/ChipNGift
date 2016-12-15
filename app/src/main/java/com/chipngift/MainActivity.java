package com.chipngift;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.general.files.StartActProcess;

public class MainActivity extends AppCompatActivity {

    Button btn_signup, btn_login;
    TextView tv_skip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_signup = (Button) findViewById(R.id.btn_signup);
        btn_login = (Button) findViewById(R.id.btn_login);
        tv_skip = (TextView) findViewById(R.id.tv_skip);

        btn_signup.setOnClickListener(new setOnClickList());
        btn_login.setOnClickListener(new setOnClickList());
        tv_skip.setOnClickListener(new setOnClickList());

    }

    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_signup:
                    (new StartActProcess(getActContext())).startAct(SignupActivity.class);
                    break;
                case R.id.btn_login:
                    (new StartActProcess(getActContext())).startAct(LoginActivity.class);
                    break;
                case R.id.tv_skip:
                    (new StartActProcess(getActContext())).startAct(DashboardActivity.class);
                    break;
            }
        }
    }

    public Context getActContext() {
        return MainActivity.this;
    }
}
