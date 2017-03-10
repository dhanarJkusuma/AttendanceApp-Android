package com.dhanarjkusuma.sarihusada.sarihusada.model;

/**
 * Created by Dhanar J Kusuma on 10/03/2017.
 */

public class MergeResponseLocationKloter
{
    private ResponseGetKloter responseGetKloter;
    private ResponseGetLocation responseGetLocation;

    public MergeResponseLocationKloter(ResponseGetKloter responseGetKloter, ResponseGetLocation responseGetLocation) {
        this.responseGetKloter = responseGetKloter;
        this.responseGetLocation = responseGetLocation;
    }

    public ResponseGetKloter getResponseGetKloter() {
        return responseGetKloter;
    }

    public void setResponseGetKloter(ResponseGetKloter responseGetKloter) {
        this.responseGetKloter = responseGetKloter;
    }

    public ResponseGetLocation getResponseGetLocation() {
        return responseGetLocation;
    }

    public void setResponseGetLocation(ResponseGetLocation responseGetLocation) {
        this.responseGetLocation = responseGetLocation;
    }
}
