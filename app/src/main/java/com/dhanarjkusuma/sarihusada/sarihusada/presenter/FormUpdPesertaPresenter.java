package com.dhanarjkusuma.sarihusada.sarihusada.presenter;

import android.content.Context;

import com.dhanarjkusuma.sarihusada.sarihusada.model.Peserta;
import com.dhanarjkusuma.sarihusada.sarihusada.model.ResponseGetKloter;
import com.dhanarjkusuma.sarihusada.sarihusada.model.ResponsePost;
import com.dhanarjkusuma.sarihusada.sarihusada.network.ApiService;
import com.dhanarjkusuma.sarihusada.sarihusada.network.RetrofitService;
import com.dhanarjkusuma.sarihusada.sarihusada.utils.SessionManager;
import com.dhanarjkusuma.sarihusada.sarihusada.view.ui_interface.FormUpdPesertaInterface;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Dhanar J Kusuma on 09/03/2017.
 */

public class FormUpdPesertaPresenter {
    private FormUpdPesertaInterface formUpdPesertaInterface;
    private SessionManager session;

    public FormUpdPesertaPresenter(FormUpdPesertaInterface formUpdPesertaInterface, Context context) {
        this.formUpdPesertaInterface = formUpdPesertaInterface;
        this.session = new SessionManager(context);
    }

    public void doPrepareForm(){
        formUpdPesertaInterface.doPrepareForm();
        RetrofitService
                .getApiHeaderService(session.getToken())
                .getKloters()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<ResponseGetKloter>() {
                    @Override
                    public void accept(@NonNull ResponseGetKloter responseGetKloter) throws Exception {
                        if (responseGetKloter.getCode() == ApiService.UNAUTHORIZED_TOKEN) {
                            formUpdPesertaInterface.onUnAuthorized();
                        } else {
                            formUpdPesertaInterface.donePrepareForm(responseGetKloter);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        if(throwable.getMessage().contains("401")){
                            formUpdPesertaInterface.onUnAuthorized();
                        }else{
                            formUpdPesertaInterface.failurePrepareForm(throwable.getMessage());
                        }
                    }
                });
    }

    public void doUpdatePeserta(Peserta peserta, String name, String alamat, String kloter){
        formUpdPesertaInterface.doRequest();
        RetrofitService
                .getApiHeaderService(session.getToken())
                .updatePeserta(
                        peserta.getId(),
                        name,
                        alamat,
                        kloter,
                        session.getUser().getReps().getId()
                )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<ResponsePost>() {
                    @Override
                    public void accept(@NonNull ResponsePost responsePost) throws Exception {
                        if (responsePost.getCode() == ApiService.UNAUTHORIZED_TOKEN) {
                            formUpdPesertaInterface.onUnAuthorized();
                        } else {
                            formUpdPesertaInterface.doneRequest(responsePost);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        if(throwable.getMessage().contains("401")){
                            formUpdPesertaInterface.onUnAuthorized();
                        }else{
                            formUpdPesertaInterface.failureRequest(throwable.getMessage());
                        }
                    }
                });
    }
}
