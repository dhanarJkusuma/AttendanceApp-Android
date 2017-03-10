package com.dhanarjkusuma.sarihusada.sarihusada.view.ui_interface;

/**
 * Created by Dhanar J Kusuma on 09/03/2017.
 */

public interface FormUpdPesertaInterface {
    public void doPrepareForm();
    public void donePrepareForm(Object data);
    public void failurePrepareForm(String message);
    public void doRequest();
    public void doneRequest(Object data);
    public void failureRequest(String message);
    public void onUnAuthorized();
}
