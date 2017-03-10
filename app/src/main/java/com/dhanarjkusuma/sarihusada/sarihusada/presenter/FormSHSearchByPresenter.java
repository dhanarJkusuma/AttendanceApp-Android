package com.dhanarjkusuma.sarihusada.sarihusada.presenter;

import android.content.Context;

import com.dhanarjkusuma.sarihusada.sarihusada.model.MergeResponseLocationKloter;
import com.dhanarjkusuma.sarihusada.sarihusada.model.ResponseGetKloter;
import com.dhanarjkusuma.sarihusada.sarihusada.model.ResponseGetLocation;
import com.dhanarjkusuma.sarihusada.sarihusada.model.ResponseGetPeserta;
import com.dhanarjkusuma.sarihusada.sarihusada.network.RetrofitService;
import com.dhanarjkusuma.sarihusada.sarihusada.utils.SessionManager;
import com.dhanarjkusuma.sarihusada.sarihusada.view.ui_interface.DoRequestInterface;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by Dhanar J Kusuma on 10/03/2017.
 */

public class FormSHSearchByPresenter
{
    private DoRequestInterface doRequestInterface;
    private SessionManager session;

    public FormSHSearchByPresenter(DoRequestInterface doRequestInterface, Context context) {
        this.doRequestInterface = doRequestInterface;
        this.session = new SessionManager(context);
    }

    public void doFillSpinner(){
        doRequestInterface.doRequest();
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
                        doRequestInterface.doneRequest(response);
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
