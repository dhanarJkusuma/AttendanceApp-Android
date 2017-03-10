package com.dhanarjkusuma.sarihusada.sarihusada.presenter;

import android.content.Context;

import com.dhanarjkusuma.sarihusada.sarihusada.model.ResponseGetKloter;
import com.dhanarjkusuma.sarihusada.sarihusada.network.ApiService;
import com.dhanarjkusuma.sarihusada.sarihusada.network.RetrofitService;
import com.dhanarjkusuma.sarihusada.sarihusada.utils.SessionManager;
import com.dhanarjkusuma.sarihusada.sarihusada.view.ui_interface.DoRequestInterface;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Dhanar J Kusuma on 10/03/2017.
 */

public class PesertaKloterPresenter
{
    private DoRequestInterface doRequestInterface;
    private SessionManager session;
    public PesertaKloterPresenter(Context context, DoRequestInterface doRequestInterface) {
        this.doRequestInterface = doRequestInterface;
        this.session = new SessionManager(context);
    }

    public void doGetKloter(){
        doRequestInterface.doRequest();
        RetrofitService
                .getApiHeaderService(session.getToken())
                .getKloters()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                        new Consumer<ResponseGetKloter>() {
                            @Override
                            public void accept(@NonNull ResponseGetKloter responseGetKloter) throws Exception {
                                if(responseGetKloter.getCode() == ApiService.UNAUTHORIZED_TOKEN){
                                    doRequestInterface.onUnAuthorized();
                                }else {
                                    doRequestInterface.doneRequest(responseGetKloter);
                                }


                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                if(throwable.getMessage().contains("401")){
                                    doRequestInterface.onUnAuthorized();
                                }else{
                                    doRequestInterface.failureRequest(throwable.getMessage());
                                }
                            }
                        });
    }
}
