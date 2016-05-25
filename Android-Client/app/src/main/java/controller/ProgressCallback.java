package controller;

/**
 * Created by manishautomatic on 18/05/16.
 */
public interface ProgressCallback {


    public void initRequest(String message);
    public void requestSuccess(String message);
    public void requestFailure(String message);
    public void requestError(String message);


}
