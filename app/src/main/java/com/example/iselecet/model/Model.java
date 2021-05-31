package com.example.iselecet.model;

import android.app.Activity;

import com.example.iselecet.model.user.User;

public class Model {

    private Activity mActivity;
    public final static Model instance = new Model();
    FireBaseModel fireBase = new FireBaseModel();

    public void setActivity(Activity activity){
        this.mActivity = activity;
    }

    public void signUpFB(User user, String password) {
        fireBase.signUpToFireBase(user,password,mActivity);
    }

    public interface AddUserListener {
        void onComplete();
    }

    public void addUser(final User user, final AddUserListener listener) {
        fireBase.addUser(user, new AddUserListener() {
            @Override
            public void onComplete() {
                listener.onComplete();
            }
        });
    }

    public interface StringListener {
        void onComplete(String data);
    }

    public void getOwnerId(String userId, StringListener data) {
        fireBase.getUserRole(userId,data);
    }


}
