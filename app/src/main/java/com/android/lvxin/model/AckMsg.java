package com.android.lvxin.model;

/**
 * Created by luotianjia on 15-1-29.
 */
public abstract class AckMsg {

    private int recode;
    private String msg;

    /**
     * json数据判断
     *
     * @param json
     * @return
     */
    public abstract boolean fromJson(String json);


    public int getRecode() {
        return recode;
    }

    public void setRecode(int recode) {
        this.recode = recode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
