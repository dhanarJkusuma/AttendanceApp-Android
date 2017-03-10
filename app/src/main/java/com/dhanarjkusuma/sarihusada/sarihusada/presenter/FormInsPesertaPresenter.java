package com.dhanarjkusuma.sarihusada.sarihusada.presenter;

import android.content.Context;

import android.util.Log;

import com.dhanarjkusuma.sarihusada.sarihusada.model.ResponseGetKloter;
import com.dhanarjkusuma.sarihusada.sarihusada.model.ResponsePost;
import com.dhanarjkusuma.sarihusada.sarihusada.network.ApiService;
import com.dhanarjkusuma.sarihusada.sarihusada.network.RetrofitService;
import com.dhanarjkusuma.sarihusada.sarihusada.utils.SessionManager;
import com.dhanarjkusuma.sarihusada.sarihusada.view.ui_interface.FormInsPesertaInterface;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

/**
 * Created by Dhanar J Kusuma on 08/03/2017.
 */

public class FormInsPesertaPresenter {
    private FormInsPesertaInterface formInsInterf;
    private SessionManager session;
    private Retrofit retrofit;
    public FormInsPesertaPresenter(Context context, FormInsPesertaInterface formInsInterf){
        this.formInsInterf = formInsInterf;
        this.session = new SessionManager(context);
    }

    public void doPrepareForm(){
        formInsInterf.doPrepareForm();
        RetrofitService
                .getApiHeaderService(session.getToken())
                .getKloters()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                        new Consumer<ResponseGetKloter>() {
                            @Override
                            public void accept(@NonNull ResponseGetKloter responseGetKloter) throws Exception {
                                formInsInterf.donePrepareForm(responseGetKloter);
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                if(throwable.getMessage().contains("401")){
                                    formInsInterf.onUnAuthorized();
                                }else{
                                    formInsInterf.failurePrepareForm(throwable.getMessage());
                                }
                            }
                        });
    }

    public void doRequestInsert(String name, String alamat, String kloter){
        formInsInterf.doRequest();
        Log.d(this.getClass().getSimpleName(), session.getToken());
        RetrofitService
                .getApiHeaderService(session.getToken())
                .postPeserta(name, alamat, kloter, session.getUser().getReps().getId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                        new Consumer<ResponsePost>() {
                            @Override
                            public void accept(@NonNull ResponsePost responsePost) throws Exception {
                                if(responsePost.getCode() == ApiService.UNAUTHORIZED_TOKEN){
                                    formInsInterf.onUnAuthorized();
                                }else{
                                    formInsInterf.doneRequest(responsePost);
                                }
                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                if(throwable.getMessage().contains("401")){
                                    formInsInterf.onUnAuthorized();
                                }else{
                                    formInsInterf.failurePrepareForm(throwable.getMessage());
                                }

                            }
                        },
                        new Action() {
                            @Override
                            public void run() throws Exception {

                            }
                        },
                        new Consumer<Disposable>() {
                            @Override
                            public void accept(@NonNull Disposable disposable) throws Exception {

                            }
                        });
    }
}

