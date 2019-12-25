package com.dodo.testing.tests;

import java.nio.charset.Charset;

import org.testng.annotations.Test;

import com.dodo.utils.http.HttpPack;
import com.dodo.utils.http.HttpUtils;

/**
 * Http 工具类测试
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class HttpTest {

    @Test
    public void testGet() {
        // 创建一个Http请求包
        HttpPack pack = HttpPack.create("https://admin.0yi0.com/test.json")
        // 指定字符集，默认UTF-8
                .charset(Charset.forName("utf-8"))
                // 添加请求头（如有必要）
                .header("headerName", "headerValue")
                // 设置代理（如有必要）
                //.proxy(new HttpHost("127.0.0.1", 80))
                // 请求表单参数
                .param("page", "1");
        // 发起Get请求
        String result = pack.get(response -> {
            // 获得 HttpResponse 对象，读取内容等操作
            // 这个示例，是读取响应内容，转化为String
                return HttpUtils.read(response);
            });

        // 打印结果
        System.err.println(result);

    }

    @Test
    public void testPost() {
        // 创建一个Http请求包
        HttpPack pack = HttpPack.create("https://admin.0yi0.com/test.json")
        // 指定字符集，默认UTF-8
                .charset(Charset.forName("utf-8"))
                // 添加请求头（如有必要）
                .header("headerName", "headerValue")
                // 设置代理（如有必要）
                //.proxy(new HttpHost("127.0.0.1", 80))
                // 请求表单参数
                .param("page", "1");
        // 发起Post请求
        String result = pack.post(response -> {
            // 获得 HttpResponse 对象，读取内容等操作
            // 这个示例，是读取响应内容，转化为String
                return HttpUtils.read(response);
            });

        // 打印结果
        System.err.println(result);

    }

    @Test
    public void testPostBody() {
        // 创建一个Http请求包
        HttpPack pack = HttpPack.create("https://admin.0yi0.com/test.json")
        // 指定字符集，默认UTF-8
                .charset(Charset.forName("utf-8"))
                // 添加请求头（如有必要）
                .header("headerName", "headerValue")
                // 设置代理（如有必要）
                //.proxy(new HttpHost("127.0.0.1", 80))
                // 请求body设置
                .body("this a body");
        // 发起Post请求
        String result = pack.post(response -> {
            // 获得 HttpResponse 对象，读取内容等操作
            // 这个示例，是读取响应内容，转化为String
                return HttpUtils.read(response);
            });

        // 打印结果
        System.err.println(result);

    }
}
