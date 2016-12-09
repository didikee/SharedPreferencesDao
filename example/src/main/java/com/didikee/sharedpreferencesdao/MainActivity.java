package com.didikee.sharedpreferencesdao;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.didikee.spdaolib.SPDao;
import com.didikee.spdaolib.utils.SPDaoChecker;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_name;
    private SPDao spDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spDao = new SPDao(this);
        tv_name = (TextView) findViewById(R.id.tv_name);

        ((Button) findViewById(R.id.bt_1)).setOnClickListener(this);
        ((Button) findViewById(R.id.bt_2)).setOnClickListener(this);
        ((Button) findViewById(R.id.bt_3)).setOnClickListener(this);
        ((Button) findViewById(R.id.bt_4)).setOnClickListener(this);

        char value1 = spDao.getValue(MySPKey.FLAG_1);
        char value2 = spDao.getValue(MySPKey.FLAG_2);
        char value3 = spDao.getValue(MySPKey.FLAG_3);

        String show="";
        if (value1 == MySPValue.TRUE){
            show+="button 1 Yes Yes"+"\n";
        }else {
            show+="button 1 No No No"+"\n";
        }

        if (value2 == MySPValue.TRUE){
            show+="button 2 Yes Yes"+"\n";
        }else {
            show+="button 2 No No No"+"\n";
        }

        if (value3 == MySPValue.TRUE){
            show+="button 3 Yes Yes"+"\n";
        }else {
            show+="button 3 No No No"+"\n";
        }

        tv_name.setText(show);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_1:
                spDao.putValue(MySPKey.FLAG_1,MySPValue.TRUE);
                break;
            case R.id.bt_2:
                spDao.putValue(MySPKey.FLAG_2,MySPValue.TRUE);
                break;
            case R.id.bt_3:
                spDao.putValue(MySPKey.FLAG_3,MySPValue.TRUE);
                break;
            case R.id.bt_4:
                boolean check = SPDaoChecker.check(MySPKey.class);
                if (check){
                    Toast.makeText(this, "没有重复", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this, "重复!!!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
