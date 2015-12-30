package com.example.lin541019.lotteryget;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends Activity {

    Button btnCheck, btnMode;
    private Drawl bDrawl = null;
    private LinearLayout freeDrawLayout;
    Intent intent;
    public static String SDcard = Environment.getExternalStorageDirectory().toString();

    private int lotteryNumber = -1;

    private int[] lotteryCounts = {0, 0, 0, 0, 0};
    private String[] lotteryName = {"0", "100", "200", "500", "1000"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnMode = (Button)findViewById(R.id.btnMode);
        btnCheck = (Button)findViewById(R.id.btnCheck);

        freeDrawLayout = (LinearLayout)findViewById(R.id.canvasLayout);

        lotteryNumber = (int) (Math.random() * 12);
        System.out.println(lotteryNumber);

        bDrawl = new Drawl(this);
        if(bDrawl.getLotteryNumber() == -1) {
            bDrawl.setLotteryNumber(lotteryNumber);
        }

        bDrawl.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                // TODO Auto-generated method stub
                btnCheck.setEnabled(true);
                return false;
            }
        });
        freeDrawLayout.addView(bDrawl);

        btnMode.setOnClickListener(btnKeyListener);
        btnCheck.setOnClickListener(btnKeyListener);
    }

    private Button.OnClickListener btnKeyListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.btnMode:
                    if(bDrawl.getPaintTight()) {
                        bDrawl.setPaintTight(false);
                        btnMode.setText("大快朵頤");
                        bDrawl.setEraseWidth(150);
                    }else {
                        bDrawl.setPaintTight(true);
                        btnMode.setText("細細品嚐");
                        bDrawl.setEraseWidth(50);
                    }
                    break;
                case R.id.btnCheck:
                    readCount();
                    int lot = -1;
                    if(lotteryNumber > -1 && lotteryNumber < 7){
                        lotteryCounts[0]++;
                        lot = 0;
                    }else if(lotteryNumber == 7 || lotteryNumber == 8){
                        lotteryCounts[1]++;
                        lot = 1;
                    }else if(lotteryNumber >8 && lotteryNumber < 12){
                        lotteryCounts[lotteryNumber-7]++;
                        lot = lotteryNumber - 7;
                    }
                    saveCount();

                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("越刮越樂股份有限公司") .setMessage("恭喜你獲得刮刮樂總獎金額：\n" + lotteryName[lot] + "元")
                            .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    intent = new Intent(MainActivity.this, Celemory.class);
                                    startActivity(intent);
                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                    finish();
                                }
                            })
                            .show();

                    break;
            }
        }
    };


    private void readCount(){
        String filename = SDcard + "/checking/lott001";
        String temp;
        int i = 0;
        try {
            FileInputStream fileInputStream = new FileInputStream(filename);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();
            while ((temp = bufferedReader.readLine()) != null) {
                stringBuffer.append(temp);
                lotteryCounts[i] = Integer.parseInt(temp);
                i++;
            }
            //Toast.makeText(getApplicationContext(), "讀檔成功!!", Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveCount(){
        String filename = SDcard + "/checking/lott001";
        String CountData = "";

        for(int i=0;i<5;i++){
            CountData += String.valueOf(lotteryCounts[i]) + "\n";
        }

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(filename);
            fileOutputStream.write(CountData.getBytes());
            fileOutputStream.close();
            //Toast.makeText(getApplicationContext(), "存檔成功!!", Toast.LENGTH_LONG).show();
        }   catch (IOException e) {
            e.printStackTrace();
        }
    }

}
