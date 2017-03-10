package com.dhanarjkusuma.sarihusada.sarihusada.view.ui_interface;

/**
 * Created by Dhanar J Kusuma on 07/03/2017.
 */

public interface DoRequestInterface
{
    public void doRequest();
    public void doneRequest(Object data);
    public void failureRequest(String message);
    public void onUnAuthorized();
}
