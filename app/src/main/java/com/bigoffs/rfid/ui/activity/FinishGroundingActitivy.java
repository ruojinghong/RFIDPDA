package com.bigoffs.rfid.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bigoffs.rfid.R;


/**
 * @author okbuy-001
 * @desc
 * @time 2018/12/24 17:34
 */
public class FinishGroundingActitivy extends BaseActivity{


        private TextView tvNum;
        private Button btn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       setContentView(R.layout.activity_finish_grounding);
       tvNum = (TextView) findViewById(R.id.tv_num);
       tvNum.setText("本次入库数量："+getIntent().getIntExtra("num",0));
       btn = (Button) findViewById(R.id.btn);
       btn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent it = new Intent(FinishGroundingActitivy.this,GroundingActivity.class);
               startActivity(it);
               finish();
           }
       });

    }
}
