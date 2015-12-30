package com.example.lin541019.lotteryget;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class Celemory extends Activity {

    ListView listView = null;
    private TextView textView = null;
    Button btnReturn = null;
    private String[] list = {"0", "0", "0", "0", "0"};
    private String[] lotteryName = {"銘謝惠顧獎", "100元中獎獎項", "200元中獎獎項", "500元中獎獎項", "1000元中獎獎項"};
    Intent intent = new Intent();

    final String ID_NAME = "name", ID_PRICE = "price";

    public static String SDcard = Environment.getExternalStorageDirectory().toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_celemory);

        listView = (ListView)findViewById(R.id.ListView1);
        textView = (TextView)findViewById(R.id.tvPrice);
        btnReturn = (Button)findViewById(R.id.btnReturn);

        readCount();
        // list item 1 顯示單層listview
        // listAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        //listView.setAdapter(listAdapter);
        setOwnArrayList();

        setButtonEnable();

        btnReturn.setOnClickListener(btnListener);

    }

    public void setButtonEnable(){
        btnReturn.setEnabled(false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                btnReturn.setEnabled(true);
            }
        }, 3000);
    }

    private Button.OnClickListener btnListener = new Button.OnClickListener() {

        @Override
        public void onClick(View v) {
            new AlertDialog.Builder(Celemory.this)
                    .setTitle("圓夢小框框") .setMessage("是否繼續遊戲？")
                    .setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            intent.setClass(Celemory.this, Welcome.class);
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            finish();
                        }
                    })
                    .setNegativeButton("否", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new AlertDialog.Builder(Celemory.this)
                                    .setTitle("回到現實") .setMessage("請返回遊戲主畫面按下EXIT按鈕!!")
                                    .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            intent.setClass(Celemory.this, Welcome.class);
                                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                            finish();
                                        }
                                    })
                                    .show();
                        }
                    }).show();
        }
    };

    private void setOwnArrayList(){
        ArrayList<HashMap<String,String>> myListData = new ArrayList<HashMap<String,String>>();
        for( int i=0;i<5 ; i++) {
            HashMap<String,String> item = new HashMap<String,String>();
            item.put(ID_NAME,lotteryName[i]);
            item.put(ID_PRICE,list[i]);
            myListData.add(item);
        }
        listView.setAdapter( new SimpleAdapter(
                        this,
                        myListData,
                        android.R.layout.simple_list_item_2,
                        new String[] { ID_NAME, ID_PRICE },
                        new int[] { android.R.id.text1, android.R.id.text2 } ));
    }

    /*private void checkPrice(int price){

        if(price == 0){
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("越刮越樂股份有限公司") .setMessage("銘謝惠顧")
                    .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            askExit();
                        }
                    })
                    .show();
        }else{
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("圓夢小框框") .setMessage("兌什麼獎!? 還不趕快去工作!!")
                    .setPositiveButton("QQ", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();
        }

    }*/


    private void askExit(){

        new AlertDialog.Builder(Celemory.this)
                .setTitle("圓夢小框框") .setMessage("是否繼續遊戲？")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        intent.setClass(Celemory.this, Welcome.class);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();
                    }
                })
                .setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new AlertDialog.Builder(Celemory.this)
                                .setTitle("回到現實") .setMessage("遊戲即將結束!!")
                                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        intent.setClass(Celemory.this, Welcome.class);
                                        startActivity(intent);
                                        System.exit(0);
                                    }
                                })
                                .show();
                    }
                }).show();
    }

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
                list[i] = temp;
                i++;
            }
            //Toast.makeText(getApplicationContext(), "讀檔成功!!", Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
