package com.dodo.testing.tests;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;

import com.dodo.common.framework.bean.file.DodoFile;
import com.dodo.utils.JacksonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;

/**
 * Jackson工具类测试
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class JacksonTest {

    @Test
    public void testObjectToJson() throws JsonProcessingException {
        DodoFile file = new DodoFile();
        file.setFileName("test.file");
        // 普通对象转Json
        String s = JacksonUtil.toJackson(file);
        System.err.println(s);
    }

    @Test
    public void testJsonToObject() throws IOException {
        DodoFile file = new DodoFile();
        file.setFileName("test.file");
        String s = JacksonUtil.toJackson(file);
        System.err.println(s);

        // Json转普通对象
        DodoFile file2 = JacksonUtil.toObject(s, DodoFile.class);
        System.err.println(file2);
    }

    @Test
    public void testMapToJson() throws JsonProcessingException {
        Map<String, DodoFile> files = new HashMap<String, DodoFile>();
        DodoFile file = new DodoFile();
        file.setFileName("test.file");
        files.put("key1", file);
        // 集合对象转Json
        String s = JacksonUtil.toJackson(files);
        System.err.println(s);
    }

    @Test
    public void testJsonToMap() throws IOException {
        Map<String, DodoFile> files = new HashMap<String, DodoFile>();
        DodoFile file = new DodoFile();
        file.setFileName("test.file");
        files.put("key1", file);
        String s = JacksonUtil.toJackson(files);
        System.err.println(s);
        // Json转集合对象，方式1
        Map<String, DodoFile> files2 = JacksonUtil.toObject(s, new TypeReference<Map<String, DodoFile>>() {});
        System.err.println(files2);

        // Json转集合对象，方式2
        Map<String, DodoFile> files3 = JacksonUtil.toCollectionObject(s, Map.class, String.class, DodoFile.class);
        System.err.println(files3);
    }
}
