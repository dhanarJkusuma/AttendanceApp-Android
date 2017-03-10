package com.dhanarjkusuma.sarihusada.sarihusada.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.dhanarjkusuma.sarihusada.sarihusada.model.Reps;
import com.dhanarjkusuma.sarihusada.sarihusada.model.User;

/**
 * Created by Dhanar J Kusuma on 08/03/2017.
 */

public class SessionManager {
    private static String TAG = SessionManager.class.getSimpleName();

    //SharedPreferences Login
    SharedPreferences __prefLogin;
    SharedPreferences.Editor __editorLogin;
    //End SharedPreferences Login

    int PRIVATE_MODE = 0;

    private static final String PREF_LOGIN = "sarihusada_auth";

    //KEY LOGIN
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_TOKEN = "userToken";
    private static final String KEY_USER_JSON = "userJSON";
    //END KEY LOGIN

    //KEY USER
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_LEVEL = "user_level";
    private static final String KEY_USER_REPS_ID = "user_reps_id";
    private static final String KEY_USER_REPS_NAME = "user_reps_name";
    //END KEY USER

    public SessionManager(Context context){
        __prefLogin = context.getSharedPreferences(PREF_LOGIN, PRIVATE_MODE);
        __editorLogin = __prefLogin.edit();
    }


    public void setLogin(boolean isLoggidIn, String token, User user){
        __editorLogin.putBoolean(KEY_IS_LOGGED_IN, isLoggidIn);
        __editorLogin.putString(KEY_TOKEN, token);
        populateInsertedUser(user);
        __editorLogin.commit();
    }

    private void populateInsertedUser(User user){
        __editorLogin.putString(KEY_USER_ID, user.getId());
        __editorLogin.putString(KEY_USER_NAME, user.getUsername());
        __editorLogin.putString(KEY_USER_LEVEL, user.getLevel());
        if(user.getReps()!= null){
            __editorLogin.putString(KEY_USER_REPS_ID, user.getReps().getId());
            __editorLogin.putString(KEY_USER_REPS_NAME, user.getReps().getName());
        }
    }

    private User populateGetUser(){
        User user = new User();
        user.setId(__prefLogin.getString(KEY_USER_ID, null));
        user.setUsername(__prefLogin.getString(KEY_USER_NAME, null));
        user.setLevel(__prefLogin.getString(KEY_USER_LEVEL, null));

        Reps reps = null;
        if(__prefLogin.getString(KEY_USER_REPS_ID, null) != null){
            reps = new Reps();
            reps.setId(__prefLogin.getString(KEY_USER_REPS_ID, null));
            reps.setName(__prefLogin.getString(KEY_USER_REPS_NAME, null));
        }

        user.setReps(reps);
        return user;
    }

    public boolean isLoggedIn(){
        return __prefLogin.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public String getToken(){
        return __prefLogin.getString(KEY_TOKEN, "");
    }

    public User getUser(){
        User user = null;
        if(isLoggedIn()){
            user = populateGetUser();
        }
        return user;
    }

    public void logout(){
        __editorLogin.putBoolean(KEY_IS_LOGGED_IN, false);
        __editorLogin.putString(KEY_TOKEN, null);
        __editorLogin.putString(KEY_USER_ID, null);
        __editorLogin.putString(KEY_USER_NAME, null);
        __editorLogin.putString(KEY_USER_LEVEL, null);
        __editorLogin.putString(KEY_USER_REPS_ID, null);
        __editorLogin.putString(KEY_USER_REPS_NAME, null);
        __editorLogin.commit();
    }
}
