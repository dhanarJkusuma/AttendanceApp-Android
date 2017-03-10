package com.dhanarjkusuma.sarihusada.sarihusada.presenter;

import android.content.Context;

import com.dhanarjkusuma.sarihusada.sarihusada.model.ResponsePost;
import com.dhanarjkusuma.sarihusada.sarihusada.network.ApiService;
import com.dhanarjkusuma.sarihusada.sarihusada.network.RetrofitService;
import com.dhanarjkusuma.sarihusada.sarihusada.utils.SessionManager;
import com.dhanarjkusuma.sarihusada.sarihusada.view.ui_interface.DoRequestInterface;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Dhanar J Kusuma on 09/03/2017.
 */

public class ReadPesertaPresenter {
    private DoRequestInterface doRequestInterface;
    private Context context;
    private SessionManager session;
    private Disposable disposableDeleteReq = null;
    public ReadPesertaPresenter(Context context, DoRequestInterface doRequestInterface) {
        this.doRequestInterface = doRequestInterface;
        this.context = context;
        this.session = new SessionManager(context);
    }

    public void doDeletePeserta(String id){
        doRequestInterface.doRequest();
        RetrofitService
                .getApiHeaderService(session.getToken())
                .deletePeserta(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                        new Consumer<ResponsePost>() {
                            @Override
                            public void accept(@NonNull ResponsePost responsePost) throws Exception {
                                if(responsePost.getCode() == ApiService.UNAUTHORIZED_TOKEN){
                                    doRequestInterface.onUnAuthorized();
                                }else {
                                    doRequestInterface.doneRequest(responsePost);
                                }
                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                if(throwable.getMessage().contains("401")){
                                    doRequestInterface.onUnAuthorized();
                                }else{
                                    doRequestInterface.failureRequest(throwable.getMessage());
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
                                disposableDeleteReq = disposable;
                            }
                        });
    }

    public void disposeRequest(){
        if(disposableDeleteReq!=null){
            disposableDeleteReq.dispose();
        }
        doRequestInterface.failureRequest("Proses menghapus peserta dibatalkan oleh pengguna.");
    }

}
