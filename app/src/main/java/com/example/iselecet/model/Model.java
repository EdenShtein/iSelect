package com.example.iselecet.model;

import android.app.Activity;

public class Model {

    private Activity mActivity;
    public final static Model instance = new Model();
    FireBaseModel fireBase = new FireBaseModel();

    public void setActivity(Activity activity){
        this.mActivity = activity;
    }
}
