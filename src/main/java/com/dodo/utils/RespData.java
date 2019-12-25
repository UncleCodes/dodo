package com.dodo.utils;

/**
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class RespData {

    private int    ec = 200;

    private String em = "Success";

    private Object data;

    public static RespData success(String msg, Object data) {
        return new RespData(msg, data);
    }

    public static RespData success(Object data) {
        return new RespData(data);
    }

    public static RespData success() {
        return new RespData();
    }

    public static RespData fail(int ec, String msg) {
        return new RespData(ec, msg, null);
    }

    public RespData() {
        super();
    }

    public RespData(Object data) {
        super();
        this.data = data;
    }

    public RespData(String em, Object data) {
        super();
        this.em = em;
        this.data = data;
    }

    public RespData(int ec, String em, Object data) {
        super();
        this.ec = ec;
        this.em = em;
        this.data = data;
    }

    public int getEc() {
        return ec;
    }

    public void setEc(int ec) {
        this.ec = ec;
    }

    public String getEm() {
        return em;
    }

    public void setEm(String em) {
        this.em = em;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "RespData [em=" + em + ", ec=" + ec + ", data=" + data + "]";
    }
}