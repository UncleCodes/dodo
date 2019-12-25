package com.dodo.weixin.bean.multicustservice;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class PagerForRecord {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private Long                           starttime;
    private Long                           endtime;
    private Date                           starttimeDate;
    private Date                           endtimeDate;
    private String                         openid;
    private Integer                        pagesize;
    private Integer                        pageindex;

    public PagerForRecord() {
        this(null);
    }

    public PagerForRecord(String userOpenId) {
        this.openid = userOpenId;
        pageindex = 1;
        pagesize = 1000;
        endtimeDate = new Date(System.currentTimeMillis());
        LocalDateTime dateTime = LocalDateTime.ofInstant(endtimeDate.toInstant(), ZoneId.systemDefault());
        starttimeDate = Date.from(LocalDate.parse(formatter.format(dateTime), formatter).atStartOfDay()
                .atZone(ZoneId.systemDefault()).toInstant());
        starttime = starttimeDate.getTime() / 1000;
        endtime = endtimeDate.getTime() / 1000;
    }

    public Long getStarttime() {
        return starttime;
    }

    public Long getEndtime() {
        return endtime;
    }

    public Date getStarttimeDate() {
        return starttimeDate;
    }

    public Date getEndtimeDate() {
        return endtimeDate;
    }

    public void setStarttime(Long starttime) {
        this.starttime = starttime;
    }

    public void setEndtime(Long endtime) {
        this.endtime = endtime;
    }

    public void setStarttimeDate(Date starttimeDate) {
        this.starttimeDate = starttimeDate;
    }

    public void setEndtimeDate(Date endtimeDate) {
        this.endtimeDate = endtimeDate;
    }

    public String getOpenid() {
        return openid;
    }

    public Integer getPagesize() {
        return pagesize;
    }

    public Integer getPageindex() {
        return pageindex;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public void setPagesize(Integer pagesize) {
        this.pagesize = pagesize;
    }

    public void setPageindex(Integer pageindex) {
        this.pageindex = pageindex;
    }

    @Override
    public String toString() {
        return "PagerForRecord [starttime=" + starttime + ", endtime=" + endtime + ", starttimeDate=" + starttimeDate
                + ", endtimeDate=" + endtimeDate + ", openid=" + openid + ", pagesize=" + pagesize + ", pageindex="
                + pageindex + "]";
    }
}
