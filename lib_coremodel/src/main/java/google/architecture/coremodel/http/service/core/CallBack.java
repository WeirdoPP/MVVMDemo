package google.architecture.coremodel.http.service.core;


import google.architecture.coremodel.http.result.base.Response;

/**
 * @author Jewel
 * @version 1.0
 * @since 2017/6/14 0014
 */

public interface CallBack<T extends Response> {

    /**
     * 请求数据成功，处理正常业务
     */
    void handlerSuccess(T data);

    /**
     * 请求数据成功，当非正常业务时的处理
     */
    void fail(String code, String message);

    /**
     * 异常时的处理
     */
    void error(Throwable throwable);

    /**
     * {@link CallBack#fail(String, String)} 和 {@link CallBack#error(Throwable)}的集合.非{@link CallBack#handlerSuccess(Response)}的逻辑都会执行。
     */
    void handleBreak(String code, String message, Throwable throwable);

    /**
     * 请求结束时的处理，不管请求成功、失败、异常都会执行。
     */
    void finish();
}
