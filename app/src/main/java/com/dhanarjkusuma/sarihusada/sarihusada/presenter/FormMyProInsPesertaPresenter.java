package com.dhanarjkusuma.sarihusada.sarihusada.presenter;

import android.content.Context;
import android.util.Log;

import com.dhanarjkusuma.sarihusada.sarihusada.model.MergeResponseLocationKloter;
import com.dhanarjkusuma.sarihusada.sarihusada.model.ResponseGetKloter;
import com.dhanarjkusuma.sarihusada.sarihusada.model.ResponseGetLocation;
import com.dhanarjkusuma.sarihusada.sarihusada.model.ResponsePost;
import com.dhanarjkusuma.sarihusada.sarihusada.network.ApiService;
import com.dhanarjkusuma.sarihusada.sarihusada.network.RetrofitService;
import com.dhanarjkusuma.sarihusada.sarihusada.utils.SessionManager;
import com.dhanarjkusuma.sarihusada.sarihusada.view.ui_interface.FormInsPesertaInterface;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Dhanar J Kusuma on 10/03/2017.
 */

public class FormMyProInsPesertaPresenter {
    private FormInsPesertaInterface formInsPesertaInterface;
    private Context context;
    private SessionManager session;

    public FormMyProInsPesertaPresenter(FormInsPesertaInterface formInsPesertaInterface, Context context) {
        this.formInsPesertaInterface = formInsPesertaInterface;
        this.context = context;
        this.session = new SessionManager(context);
    }

    public void doPrepareForm(){
        formInsPesertaInterface.doPrepareForm();
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
                        formInsPesertaInterface.donePrepareForm(response);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        if(throwable.getMessage().contains("401")){
                            formInsPesertaInterface.onUnAuthorized();
                        }else{
                            formInsPesertaInterface.failurePrepareForm(throwable.getMessage());
                        }
                    }
                });
    }

    public void doInsertData(String name, String alamat, String kloter, String location){
        formInsPesertaInterface.doRequest();
        Log.d(this.getClass().getSimpleName(), session.getToken());
        RetrofitService
                .getApiHeaderService(session.getToken())
                .postPeserta(name, alamat, kloter, location)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                        new Consumer<ResponsePost>() {
                            @Override
                            public void accept(@NonNull ResponsePost responsePost) throws Exception {
                                if(responsePost.getCode() == ApiService.UNAUTHORIZED_TOKEN){
                                    formInsPesertaInterface.onUnAuthorized();
                                }else{
                                    formInsPesertaInterface.doneRequest(responsePost);
                                }
                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                if(throwable.getMessage().contains("401")){
                                    formInsPesertaInterface.onUnAuthorized();
                                }else{
                                    formInsPesertaInterface.failurePrepareForm(throwable.getMessage());
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
