package com.example.lin541019.lotteryget;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Welcome extends Activity {

    Button btnEnter, btnExit;
    Intent intent;
    private EditText edtName ;
    private TextView tvName;
    private boolean isCheck = false;
    private String name ;
    public static String SDcard = Environment.getExternalStorageDirectory().toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Initialize();
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        tvName.setVisibility(View.VISIBLE);
        edtName.setVisibility(View.VISIBLE);
        btnEnter.setBackgroundColor(Color.GRAY);
        btnEnter.setText("Check");
        isCheck = false;
    }

    private void Initialize(){
        isFile();

        btnEnter = (Button)findViewById(R.id.btnEnter);
        btnExit = (Button)findViewById(R.id.btnExit);
        edtName = (EditText)findViewById(R.id.edtName);
        tvName = (TextView)findViewById(R.id.tvName);

        btnEnter.setBackgroundColor(Color.GRAY);
        btnEnter.setText("Check");

        btnEnter.setOnClickListener(btnKeyListener);
        btnExit.setOnClickListener(btnKeyListener);
    }

    private boolean checkName(){
        name = edtName.getText().toString();
        if(name.equals(name)) {
            isCheck = true;
            return true;
        }else{
            return false;
        }
    }

    //如果沒有檔案，優先建檔
    private void isFile(){
        //新增資料夾
        File file = new File(SDcard + "/checking");
        file.mkdirs();

        String filename = SDcard + "/checking/lott001";
        File file1 = new File(filename);

        try {
            if(!file1.exists()){
                FileOutputStream fileOutputStream = new FileOutputStream(filename);
                String CountData = "";
                for(int i=0;i<5;i++){
                    CountData += String.valueOf('0' + "\n");
                }
                fileOutputStream.write(CountData.getBytes());
                fileOutputStream.close();
                Log.d("Welcome","初始檔案建置成功！！");
            }
        }   catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Button.OnClickListener btnKeyListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.btnEnter:
                    if(isCheck){
                        showMainFrame();
                    }else{
                        if(checkName()){
                            tvName.setVisibility(v.INVISIBLE);
                            edtName.setVisibility(v.INVISIBLE);
                            btnEnter.setText("PLAY!!");
                            btnEnter.setBackgroundColor(Color.CYAN);
                        }
                    }
                    break;
                case R.id.btnExit:
                    finish();
                    break;
            }
        }
    };

    public void showMainFrame() {
        new AlertDialog.Builder(Welcome.this)
                .setTitle("進入遊戲").setMessage("玩家" + edtName.getText().toString() + "\n準備好您的運氣與之搏鬥的嗎？")
                .setPositiveButton("是的!!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        intent = new Intent(Welcome.this, MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }
                }).show();
    }



}
