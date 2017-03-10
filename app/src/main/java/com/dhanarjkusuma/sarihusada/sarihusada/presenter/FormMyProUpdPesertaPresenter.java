package com.dhanarjkusuma.sarihusada.sarihusada.presenter;

import android.content.Context;

import com.dhanarjkusuma.sarihusada.sarihusada.model.MergeResponseLocationKloter;
import com.dhanarjkusuma.sarihusada.sarihusada.model.Peserta;
import com.dhanarjkusuma.sarihusada.sarihusada.model.ResponseGetKloter;
import com.dhanarjkusuma.sarihusada.sarihusada.model.ResponseGetLocation;
import com.dhanarjkusuma.sarihusada.sarihusada.model.ResponsePost;
import com.dhanarjkusuma.sarihusada.sarihusada.network.ApiService;
import com.dhanarjkusuma.sarihusada.sarihusada.network.RetrofitService;
import com.dhanarjkusuma.sarihusada.sarihusada.utils.SessionManager;
import com.dhanarjkusuma.sarihusada.sarihusada.view.ui_interface.FormUpdPesertaInterface;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Dhanar J Kusuma on 10/03/2017.
 */

public class FormMyProUpdPesertaPresenter {
    private FormUpdPesertaInterface formUpdPesertaInterface;
    private SessionManager session;

    public FormMyProUpdPesertaPresenter(FormUpdPesertaInterface formUpdPesertaInterface, Context context) {
        this.formUpdPesertaInterface = formUpdPesertaInterface;
        this.session = new SessionManager(context);
    }

    public void doPrepareForm(){
        formUpdPesertaInterface.doPrepareForm();
        Observable<ResponseGetLocation> location = RetrofitService.getApiHeaderService(session.getToken()).getLocations();
        Observable<ResponseGetKloter> kloter = RetrofitService.getApiHeaderService(session.getToken()).getKloters();

        Observable
                .zip(
                        location,
                        kloter,
                        new BiFunction<ResponseGetLocation, ResponseGetKloter, Object>() {
                            @Override
                            public Object apply(@NonNull ResponseGetLocation responseGetLocation, @NonNull ResponseGetKloter responseGetKloter) throws Exception {
                                return new MergeResponseLocationKloter(responseGetKloter, responseGetLocation);
                            }
                        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        MergeResponseLocationKloter response = (MergeResponseLocationKloter) o;
                        formUpdPesertaInterface.donePrepareForm(response);
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

    public void doUpdatePeserta(Peserta peserta, String name, String alamat, String kloter, String location){
        formUpdPesertaInterface.doRequest();
        RetrofitService
                .getApiHeaderService(session.getToken())
                .updatePeserta(
                        peserta.getId(),
                        name,
                        alamat,
                        kloter,
                        location
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
