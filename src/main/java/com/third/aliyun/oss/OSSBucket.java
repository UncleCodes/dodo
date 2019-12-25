package com.third.aliyun.oss;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class OSSBucket {
    private String           name;
    private String           domain;
    private BucketAccessRole accessRole;

    public OSSBucket() {
        super();
    }

    public OSSBucket(String name, String domain, BucketAccessRole accessRole) {
        super();
        this.name = name;
        this.domain = domain;
        this.accessRole = accessRole;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public BucketAccessRole getAccessRole() {
        return accessRole;
    }

    public void setAccessRole(BucketAccessRole accessRole) {
        this.accessRole = accessRole;
    }
}
