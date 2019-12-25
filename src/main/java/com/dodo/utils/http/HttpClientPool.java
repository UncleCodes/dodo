package com.dodo.utils.http;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
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
@SuppressWarnings("deprecation")
public class HttpClientPool {
    private static final Logger                       LOGGER          = LoggerFactory.getLogger(HttpClientPool.class);
    private static PoolingHttpClientConnectionManager poolConnManager = null;
    static {
        try {
            SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build();
            HostnameVerifier hostnameVerifier = SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, hostnameVerifier);
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
                    .<ConnectionSocketFactory> create()
                    .register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", sslsf).build();
            poolConnManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            poolConnManager.setMaxTotal(200);
            poolConnManager.setDefaultMaxPerRoute(20);
            SocketConfig socketConfig = SocketConfig.custom().setSoTimeout(30000).build();
            poolConnManager.setDefaultSocketConfig(socketConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static CloseableHttpClient getConnection() {
        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(30000)
                .setConnectTimeout(30000).setSocketTimeout(30000).build();
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(poolConnManager)
                .setDefaultRequestConfig(requestConfig).build();
        if (poolConnManager != null && poolConnManager.getTotalStats() != null) {
            LOGGER.info("now client pool {}", poolConnManager.getTotalStats().toString());
        }
        return httpClient;
    }
}
