package com.example.lin541019.lotteryget;

import android.app.Application;

/**
 * Created by lin541019 on 12/29/15.
 */
public class VarApp extends Application{
    private int isExit = 0;
    public void setIsExit(int isExit){
        this.isExit = isExit;
    }
    public int getIsExit(){
        return isExit;
    }


}

