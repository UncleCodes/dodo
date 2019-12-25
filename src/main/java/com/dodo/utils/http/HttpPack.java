package com.dodo.utils.http;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class HttpPack {
    private static final Logger LOGGER      = LoggerFactory.getLogger(HttpPack.class);

    private Boolean             isMultipart = false;

    private String              url         = "";

    private Map<String, String> headers;

    private List<String>        paramsKey;

    private List<Object>        paramsValue;

    private String              body;

    private Charset             charset     = Charset.forName("utf-8");

    private HttpHost            proxy;

    private HttpPack(String url) {
        this.url = url;
    }

    public static HttpPack create(String url) {
        return new HttpPack(url);
    }

    public HttpPack header(String key, String value) {
        initHeaders();
        this.headers.put(key, value);
        return this;
    }

    public HttpPack headers(Map<String, String> headers) {
        initHeaders();
        this.headers.putAll(headers);
        return this;
    }

    public HttpPack param(String key, Object value) {
        initParams();
        if (StringUtils.isNotBlank(key)) {
            this.paramsKey.add(key);
            this.paramsValue.add(value == null ? "" : value);
            if (value instanceof InputStream || value instanceof File) {
                isMultipart = Boolean.TRUE;
            }
        }
        return this;
    }

    public HttpPack params(Map<String, Object> params) {
        initParams();
        if (params != null && params.size() > 0) {
            Set<String> paramKey = params.keySet();
            for (String s : paramKey) {
                this.param(s, params.get(s));
            }
        }
        return this;
    }

    public HttpPack body(String body) {
        this.body = body;
        return this;
    }

    public HttpPack proxy(HttpHost proxy) {
        this.proxy = proxy;
        return this;
    }

    public HttpPack charset(Charset charset) {
        this.charset = charset;
        return this;
    }

    private void initHeaders() {
        if (headers == null) {
            headers = new HashMap<String, String>();
        }
    }

    private void initParams() {
        if (paramsKey == null) {
            paramsKey = new ArrayList<String>();
            paramsValue = new ArrayList<Object>();
        }
    }

    protected HttpGet get() {
        HttpGet get = null;
        if (paramsKey == null) {
            LOGGER.info("GET:{}", this.url);
            get = new HttpGet(this.url);
        } else {
            StringBuilder sbBuilder = new StringBuilder(this.url);
            if (!this.url.contains("?")) {
                sbBuilder.append("?");
            }
            int size = paramsKey.size();
            for (int i = 0; i < size; i++) {
                sbBuilder.append(paramsKey.get(i)).append("=").append(paramsValue.get(i).toString()).append("&");
            }
            sbBuilder.deleteCharAt(sbBuilder.length() - 1);
            LOGGER.info("GET:{}", sbBuilder);
            get = new HttpGet(sbBuilder.toString());
        }
        if (this.headers != null) {
            Set<String> headerKey = this.headers.keySet();
            for (String s : headerKey) {
                get.addHeader(s, this.headers.get(s));
            }
        }
        // get.addHeader("If-None-Match", eTag);
        for (Header h : get.getAllHeaders()) {
            LOGGER.info("SEND:{}:{}", h.getName(), h.getValue());
        }
        LOGGER.info(get.getRequestLine().toString());

        if (proxy != null) {
            get.setConfig(RequestConfig.custom().setProxy(proxy).build());
        }
        return get;
    }

    public <T> T get(HttpWork<T> work) {
        return HttpUtils.get(this, work);
    }

    public <T> T post(HttpWork<T> work) {
        return HttpUtils.post(this, work);
    }

    public <T> T get(HttpClient client, HttpWork<T> work) {
        return HttpUtils.get(client, this, work);
    }

    public <T> T post(HttpClient client, HttpWork<T> work) {
        return HttpUtils.post(client, this, work);
    }

    protected HttpPost post() {
        LOGGER.info("POST:{}", this.url);
        HttpPost httppost = new HttpPost(this.url);
        // httppost.addHeader("If-None-Match", eTag);
        if (this.headers != null) {
            Set<String> headerKey = this.headers.keySet();
            for (String s : headerKey) {
                httppost.addHeader(s, this.headers.get(s));
            }
        }
        if (body != null) {
            httppost.setEntity(_stringEntity());
        } else if (paramsKey != null) {
            if (isMultipart) {
                httppost.setEntity(_multipartEntity());
            } else {
                httppost.setEntity(_urlEncodedFormEntity());
            }
        }

        if (proxy != null) {
            httppost.setConfig(RequestConfig.custom().setProxy(proxy).build());
        }
        return httppost;
    }

    private HttpEntity _stringEntity() {
        return new StringEntity(this.body, charset);
    }

    private HttpEntity _urlEncodedFormEntity() {
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        int size = paramsKey.size();
        for (int i = 0; i < size; i++) {
            LOGGER.info("{}######{}", paramsKey.get(i), paramsValue.get(i));
            nvps.add(new BasicNameValuePair(paramsKey.get(i), paramsValue.get(i).toString()));
        }
        return new UrlEncodedFormEntity(nvps, charset);
    }

    private HttpEntity _multipartEntity() {
        MultipartEntityBuilder entity = MultipartEntityBuilder.create();
        entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        int size = this.paramsKey.size();
        Object postValue = null;
        for (int i = 0; i < size; i++) {
            postValue = this.paramsValue.get(i);
            if (postValue instanceof File) {
                entity.addBinaryBody(this.paramsKey.get(i), (File) postValue);
            } else if (postValue instanceof InputStream) {
                entity.addBinaryBody(this.paramsKey.get(i), (InputStream) postValue);
            } else {
                entity.addTextBody(this.paramsKey.get(i), postValue.toString(),
                        ContentType.create(ContentType.TEXT_PLAIN.getMimeType(), charset));
            }
        }
        return entity.build();
    }
}
