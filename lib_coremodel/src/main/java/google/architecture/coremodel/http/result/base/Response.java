package google.architecture.coremodel.http.result.base;

import java.io.Serializable;

/**
 * @author Jewel
 * @version 1.0
 * @since 2017/6/14 0014
 * <p>
 * 定义统一的请求结果解析
 */
public class Response<T> implements Serializable {

    //失败时返回的业务码，通过业务码可以明确各个系统错误内容
    public String bizCode;
    // 1-成功;0-失败
    public String code;
    public T data;
    //提示消息
    public String message;

    private String responseTime;//服务器返回时间

    public String getBizCode() {
        return bizCode;
    }

    public void setBizCode(String bizCode) {
        this.bizCode = bizCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getResponseTime() {
        try {
            return Long.parseLong(responseTime);
        } catch (Exception e) {
            return 0;
        }
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }

    @Override
    public String toString() {
        return "Response{" +
                "bizCode='" + bizCode + '\'' +
                ", code='" + code + '\'' +
                ", data=" + data +
                ", message='" + message + '\'' +
                ", responseTime='" + responseTime + '\'' +
                '}';
    }
}
