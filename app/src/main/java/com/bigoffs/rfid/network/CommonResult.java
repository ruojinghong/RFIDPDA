package com.bigoffs.rfid.network;


public class CommonResult {

    // 状态码 1为成功 0为失败
    public int status;
    public String json;
    // 接口版本号
    public String version;
    // 10位时间戳 单位是秒
    public long timestamp;
    // 是否需要强制更新 1为需要
    public int force;
    // 错误信息
    public Error error;

    public class Error {
        // 错误码
        public int code;
        // 错误消息
        public String message;
    }

    public boolean isSuccess() {
        return status == 1;
    }

    public String getErrorMessage() {
        if (status == 0) {
            return error.message;
        } else {
            return "请求成功";
        }
    }

    @Override
    public String toString() {
        if (status == 1) {
            return "status:" + status + ",version:" + version + ",timestamp:"
                    + timestamp + ",force:" + force + ",result:" + json;
        } else {
            return "status:" + status + ", errCode:" + error.code
                    + ", errMessage:" + error.message;
        }
    }

    public String getResult() {
        return json;
    }

}
