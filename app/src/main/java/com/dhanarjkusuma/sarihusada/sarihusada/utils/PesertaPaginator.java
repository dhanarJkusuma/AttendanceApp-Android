package com.dhanarjkusuma.sarihusada.sarihusada.utils;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.dhanarjkusuma.sarihusada.sarihusada.adapter.AdapterPeserta;
import com.dhanarjkusuma.sarihusada.sarihusada.model.Peserta;
import com.dhanarjkusuma.sarihusada.sarihusada.model.ResponseGetPeserta;
import com.dhanarjkusuma.sarihusada.sarihusada.network.ApiService;
import com.dhanarjkusuma.sarihusada.sarihusada.network.RetrofitService;
import com.dhanarjkusuma.sarihusada.sarihusada.view.ui_interface.DoRequestInterface;
import com.dhanarjkusuma.sarihusada.sarihusada.view.ui_interface.ListDataOverflowInterface;
import com.srx.widget.PullCallback;
import com.srx.widget.PullToLoadView;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Dhanar J Kusuma on 09/03/2017.
 */

public class PesertaPaginator {

    Context c;
    private PullToLoadView pullToLoadView;
    RecyclerView rv;
    private AdapterPeserta adapter;
    private boolean isLoading = false;
    private boolean hasLoadedAll = false;
    private int nextPage;
    private SessionManager session;
    private ListDataOverflowInterface listDataOverflowInterface;
    private String kloter;
    private String location;
    private DoRequestInterface doRequestInterface;

    public PesertaPaginator(Context context, PullToLoadView pullToLoadView, ListDataOverflowInterface listDataOverflowInterface, String kloter, DoRequestInterface doRequestInterface) {
        this.c = context;
        this.pullToLoadView = pullToLoadView;
        this.listDataOverflowInterface = listDataOverflowInterface;
        this.session = new SessionManager(context);
        this.kloter = kloter;
        this.doRequestInterface = doRequestInterface;
        //RECYCLERVIEW
        RecyclerView rv=pullToLoadView.getRecyclerView();
        rv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false));
        adapter=new AdapterPeserta(context, new ArrayList<Peserta>(), listDataOverflowInterface);
        rv.setAdapter(adapter);
    }

    public PesertaPaginator(Context context, PullToLoadView pullToLoadView, ListDataOverflowInterface listDataOverflowInterface, String kloter, String location, DoRequestInterface doRequestInterface) {
        this.c = context;
        this.pullToLoadView = pullToLoadView;
        this.listDataOverflowInterface = listDataOverflowInterface;
        this.session = new SessionManager(context);
        this.kloter = kloter;
        this.location = location;
        this.doRequestInterface = doRequestInterface;
        //RECYCLERVIEW
        RecyclerView rv=pullToLoadView.getRecyclerView();
        rv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false));
        adapter=new AdapterPeserta(context, new ArrayList<Peserta>(), listDataOverflowInterface);
        rv.setAdapter(adapter);
    }
    /*
    PAGE DATA
     */
    public void initializePaginator()
    {
        pullToLoadView.isLoadMoreEnabled(true);
        pullToLoadView.setPullCallback(new PullCallback() {
            //LOAD MORE DATA
            @Override
            public void onLoadMore() {
                doGetPeserta(nextPage);
            }
            //REFRESH AND TAKE US TO FIRST PAGE
            @Override
            public void onRefresh() {
                adapter.clearItem();
                hasLoadedAll=false;
                doGetPeserta(1);
            }
            //IS LOADING
            @Override
            public boolean isLoading() {
                return isLoading;
            }
            //CURRENT PAGE LOADED
            @Override
            public boolean hasLoadedAllItems() {
                return hasLoadedAll;
            }
        });
        pullToLoadView.initLoad();
    }
    /*
     LOAD MORE DATA
     SIMULATE USING HANDLERS
     */
    public void doGetPeserta(final int page)
    {
        Observable<ResponseGetPeserta> pesertaObservable = null;
        if(location == null){
            pesertaObservable = RetrofitService
                    .getApiHeaderService(session.getToken())
                    .getPesertaByKloter(page, kloter, session.getUser().getReps().getId());
        }else{
            pesertaObservable = RetrofitService
                    .getApiHeaderService(session.getToken())
                    .getPesertaByKloter(page, kloter, location);
        }

        isLoading=true;

        pesertaObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<ResponseGetPeserta>() {
                    @Override
                    public void accept(@NonNull ResponseGetPeserta responseGetPeserta) throws Exception {
                        if (responseGetPeserta.getCode() == ApiService.UNAUTHORIZED_TOKEN) {
                            doRequestInterface.onUnAuthorized();
                        } else {
                            adapter.addBulk(responseGetPeserta.getData());
                            pullToLoadView.setComplete();
                            isLoading = false;
                            if (responseGetPeserta.getPage() == responseGetPeserta.getTotalPage()) {
                                hasLoadedAll = true;
                            } else {
                                nextPage = page + 1;
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        isLoading = false;
                        if(throwable.getMessage().contains("401")){
                            doRequestInterface.onUnAuthorized();
                        }else{
                            doRequestInterface.failureRequest(throwable.getMessage());
                        }
                    }
                });


    }
}
