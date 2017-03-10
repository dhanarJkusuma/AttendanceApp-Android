package com.dhanarjkusuma.sarihusada.sarihusada.network;

import com.dhanarjkusuma.sarihusada.sarihusada.model.Kloter;
import com.dhanarjkusuma.sarihusada.sarihusada.model.ResponseGetKloter;
import com.dhanarjkusuma.sarihusada.sarihusada.model.ResponseGetLocation;
import com.dhanarjkusuma.sarihusada.sarihusada.model.ResponseGetPeserta;
import com.dhanarjkusuma.sarihusada.sarihusada.model.ResponseLogin;
import com.dhanarjkusuma.sarihusada.sarihusada.model.ResponsePost;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Dhanar J Kusuma on 07/03/2017.
 */

public interface ApiService {
    public static final String URL_ENDPOINT = "https://api-absenku.herokuapp.com/";
    public static final int UNAUTHORIZED_TOKEN = 401;

    @POST("user/login")
    @FormUrlEncoded
    Observable<ResponseLogin> doLogin(
            @Field("username") String username,
            @Field("password") String password
    );

    @GET("kloter")
    Observable<ResponseGetKloter> getKloters();

    @GET("location")
    Observable<ResponseGetLocation> getLocations();

    @POST("peserta")
    @FormUrlEncoded
    Observable<ResponsePost> postPeserta(
            @Field("name") String name,
            @Field("alamat") String alamat,
            @Field("kloter") String kloter,
            @Field("location") String location
    );

    @GET("peserta")
    Observable<ResponseGetPeserta> getPeserta(@Query("page") int page);

    @POST("peserta/bykloter")
    @FormUrlEncoded
    Observable<ResponseGetPeserta> getPesertaByKloter(
            @Query("page") int page,
            @Field("kloter") String kloter,
            @Field("location") String location
    );

    @POST("peserta/update/{id}")
    @FormUrlEncoded
    Observable<ResponsePost> updatePeserta(
            @Path("id") String id,
            @Field("name") String name,
            @Field("alamat") String alamat,
            @Field("kloter") String kloter,
            @Field("location") String location
    );

    @POST("peserta/delete/{id}")
    Observable<ResponsePost> deletePeserta(
            @Path("id") String id
    );
}
