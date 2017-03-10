package com.dhanarjkusuma.sarihusada.sarihusada.presenter;

import android.content.Context;
import android.content.pm.PackageInstaller;
import android.util.Log;

import com.dhanarjkusuma.sarihusada.sarihusada.model.ResponseLogin;
import com.dhanarjkusuma.sarihusada.sarihusada.network.RetrofitService;
import com.dhanarjkusuma.sarihusada.sarihusada.utils.SessionManager;
import com.dhanarjkusuma.sarihusada.sarihusada.view.ui_interface.DoRequestInterface;


import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.Subject;


/**
 * Created by Dhanar J Kusuma on 07/03/2017.
 */

public class LoginPresenter {
    private DoRequestInterface doRequestInterface;
    private Disposable loginDisposable;


    public LoginPresenter(DoRequestInterface doRequestInterface){
        this.doRequestInterface = doRequestInterface;
    }

    public void doLogin(String username, String password) {
        doRequestInterface.doRequest();


        RetrofitService
                .getService()
                .doLogin(username, password)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<ResponseLogin>() {
                    @Override
                    public void accept(@NonNull ResponseLogin responseLogin) throws Exception {
                        doRequestInterface.doneRequest(responseLogin);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                }, new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        loginDisposable = disposable;
                    }
                });

    }

    public void breakLogin(){
        if(this.loginDisposable != null){
            loginDisposable.dispose();
            doRequestInterface.failureRequest("Proses login dibatalkan oleh pengguna.");
        }
    }
}
