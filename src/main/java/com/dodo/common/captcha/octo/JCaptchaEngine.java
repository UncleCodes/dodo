package com.dodo.common.captcha.octo;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.io.FileUtils;

import com.dodo.common.doc2swf.Doc2swfUtil;
import com.dodo.utils.CommonUtil;
import com.octo.captcha.CaptchaException;
import com.octo.captcha.component.image.backgroundgenerator.FileReaderRandomBackgroundGenerator;
import com.octo.captcha.component.image.color.RandomListColorGenerator;
import com.octo.captcha.component.image.fontgenerator.RandomFontGenerator;
import com.octo.captcha.component.image.textpaster.DecoratedRandomTextPaster;
import com.octo.captcha.component.image.textpaster.textdecorator.TextDecorator;
import com.octo.captcha.component.image.wordtoimage.ComposedWordToImage;
import com.octo.captcha.component.word.wordgenerator.RandomWordGenerator;
import com.octo.captcha.engine.image.ImageCaptchaEngine;
import com.octo.captcha.image.ImageCaptchaFactory;
import com.octo.captcha.image.gimpy.GimpyFactory;

/**
 * 验证码引擎
 * 
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class JCaptchaEngine extends ImageCaptchaEngine {
    private Integer minAcceptedWordLength = 4;
    private Integer maxAcceptedWordLength = 4;
    private String  acceptedChars         = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /**
     * 初始化验证码引擎
     * */
    public void init() {
        buildInitialFactories();
        checkFactoriesSize();
    }

    /**
     * 设置验证码最小长度
     * */
    public void setMinAcceptedWordLength(Integer minAcceptedWordLength) {
        this.minAcceptedWordLength = minAcceptedWordLength;
    }

    /**
     * 设置验证码最大长度
     * */
    public void setMaxAcceptedWordLength(Integer maxAcceptedWordLength) {
        this.maxAcceptedWordLength = maxAcceptedWordLength;
    }

    /**
     * 设置验证码字符来源
     * */
    public void setAcceptedChars(String acceptedChars) {
        this.acceptedChars = acceptedChars;
    }

    /**
     * 添加验证码生成工厂
     * */
    @SuppressWarnings("unchecked")
    public boolean addFactory(ImageCaptchaFactory factory) {
        return (factory != null) && (this.factories.add(factory));
    }

    /**
     * 添加验证码生成工厂
     * */
    @SuppressWarnings("unchecked")
    public void addFactories(ImageCaptchaFactory[] factories) {
        checkNotNullOrEmpty(factories);
        this.factories.addAll(Arrays.asList(factories));
    }

    private void checkFactoriesSize() {
        if (this.factories.size() == 0) {
            throw new CaptchaException(
                    "This gimpy has no factories. Please initialize it properly with the buildInitialFactory() called by the constructor or the addFactory() mehtod later!");
        }
    }

    private static final Font[]  fonts  = { new Font("nyala", 1, 16), new Font("Arial", 1, 16),
            new Font("Bell MT", 1, 16), new Font("Credit valley", 1, 16), new Font("Impact", 1, 16) };
    private static final Color[] colors = { new Color(255, 255, 255), new Color(255, 220, 220),
            new Color(220, 255, 255), new Color(220, 220, 255), new Color(255, 255, 220), new Color(220, 255, 220) };

    /**
     * 根据配置 初始化验证码工厂
     * */
    protected void buildInitialFactories() {
        String bgImgDir = CommonUtil.getWebRootPath() + "WEB-INF" + File.separator + "classes" + File.separator
        //				+ (File.separator.equals("\\") ? JCaptchaEngine.class
        //						.getPackage().getName().replaceAll("\\.", "\\\\")
        //						: JCaptchaEngine.class.getPackage().getName()
        //								.replaceAll("\\.", "/")) + File.separator
                + "bgimg";

        File bgImgFile = new File(bgImgDir);
        try {
            String resourceDir = JCaptchaEngine.class.getClassLoader()
                    .getResource(JCaptchaEngine.class.getPackage().getName().replaceAll("\\.", "/")).getFile();
            if (resourceDir.contains("jar!")) {
                if (bgImgFile.exists()) {
                    FileUtils.deleteDirectory(bgImgFile);
                }
                bgImgFile = new File(bgImgDir);
                bgImgFile.mkdirs();
                resourceDir = URLDecoder.decode(resourceDir, "utf-8");
                String resourcePrefix = JCaptchaEngine.class.getPackage().getName().replaceAll("\\.", "/") + "/bgimg/";
                JarFile jarRootFile = new JarFile(resourceDir.substring(0, resourceDir.lastIndexOf("!")).replace(
                        "file:", ""));
                Enumeration<JarEntry> jarEntries = jarRootFile.entries();
                JarEntry jarEntry = null;
                String jarEntryName = null;
                while (jarEntries.hasMoreElements()) {
                    jarEntry = jarEntries.nextElement();
                    jarEntryName = jarEntry.getName();
                    if (jarEntryName.startsWith(resourcePrefix)) {
                        if (jarEntry.isDirectory() && (!jarEntryName.equals(resourcePrefix))) {
                            new File(bgImgDir + File.separator + jarEntryName.replace(resourcePrefix, "")).mkdirs();
                        } else if (!jarEntry.isDirectory()) {
                            Doc2swfUtil.writeFile(jarRootFile.getInputStream(jarEntry), new File(bgImgDir
                                    + File.separator + jarEntryName.replace(resourcePrefix, "")));
                        }
                    }
                }
                jarRootFile.close();
            } else {
                bgImgDir = CommonUtil.getWebRootPath()
                        + "WEB-INF"
                        + File.separator
                        + "classes"
                        + File.separator
                        + (File.separator.equals("\\") ? JCaptchaEngine.class.getPackage().getName()
                                .replaceAll("\\.", "\\\\") : JCaptchaEngine.class.getPackage().getName()
                                .replaceAll("\\.", "/")) + File.separator + "bgimg";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        addFactory(new GimpyFactory(new RandomWordGenerator(acceptedChars), new ComposedWordToImage(
                new RandomFontGenerator(16, 16, fonts), new FileReaderRandomBackgroundGenerator(80, 28, bgImgDir),
                new DecoratedRandomTextPaster(minAcceptedWordLength, maxAcceptedWordLength,
                        new RandomListColorGenerator(colors), new TextDecorator[0]))));
    }
}