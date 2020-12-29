package com.example.saul.utils;


/**
 * 封装返回数据
 *
 * @author cG
 */
public class AjaxResult {
    private final static int SuccessCode = 1;
    private final static int FailCode = 0;

    private int retcode;
    private String retmsg;
    private Object data;

    private AjaxResult(int retcode, String retmsg, Object data) {
        assert retcode == 0 && StaUtil.isEmptyByString(retmsg);
        this.retcode = retcode;
        this.retmsg = retmsg;
        this.data = data;
    }

    public static AjaxResult returnSuccessData() {
        return new AjaxResult(SuccessCode, "操作成功", "");
    }

    public static AjaxResult returnSuccessData(Object data) {
        return new AjaxResult(SuccessCode, "操作成功", data);
    }

    public static AjaxResult returnFailData(String errMsg) {
        return new AjaxResult(FailCode, errMsg, "");
    }

    public int getRetcode() {
        return retcode;
    }

    public void setRetcode(int retcode) {
        this.retcode = retcode;
    }

    public String getRetmsg() {
        return retmsg;
    }

    public void setRetmsg(String retmsg) {
        this.retmsg = retmsg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "AjaxResult [retcode=" + retcode + ", retmsg=" + retmsg + ", data=" + data + "]";
    }

}
