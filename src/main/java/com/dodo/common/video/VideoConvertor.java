package com.dodo.common.video;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dodo.utils.config.DodoFrameworkConfigUtil.DodoVideoUtil;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class VideoConvertor {
    public enum ConvertorType {
        TO_AVI(DodoVideoUtil.converterToaviCommand, "convert to avi", "_avi.avi"), TO_FLV(
                DodoVideoUtil.converterToflvCommand, "convert to flv", "_flv.flv"), TO_MP4(
                DodoVideoUtil.converterTomp4Command, "convert to mp4", "_mp4.mp4"), TO_SWF(
                DodoVideoUtil.converterToswfCommand, "convert to swf", "_swf.swf");
        private String command;
        private String commandDesc;
        private String append;

        private ConvertorType(String command, String commandDesc, String append) {
            this.command = command;
            this.commandDesc = commandDesc;
            this.append = append;
        }

        public String getCommand() {
            return command;
        }

        public String getCommandDesc() {
            return commandDesc;
        }

        public String getAppend() {
            return append;
        }
    }

    private static final Logger LOGGER        = LoggerFactory.getLogger(VideoConvertor.class);
    private static String       THUMBNAILNAME = "t.jpg";
    private static Pattern      PATT_DURATION = Pattern.compile("Duration: ([\\d]{2}:[\\d]{2}:[\\d]{2})");
    private static Pattern      PATT_BITRATE  = Pattern.compile("bitrate: ([\\d]+ kb/s)");
    private static Pattern      PATT_SIZE     = Pattern.compile("[\\d]+x[\\d]+");

    public static File getThumbnailFile(File srcFile) {
        return new File(srcFile.getParentFile().getAbsolutePath() + File.separator + THUMBNAILNAME);
    }

    public static File getTargetFile(File srcFile, ConvertorType convertorType) {
        return new File(srcFile.getParentFile().getAbsolutePath() + File.separator + VideoUtil.getFilePrefix(srcFile)
                + convertorType.getAppend());
    }

    private static boolean makeThumbnail(File srcFile) {
        String distFilePath = srcFile.getParentFile().getAbsolutePath() + File.separator + THUMBNAILNAME;
        String getThumbnailCommand = DodoVideoUtil.getThumbnailCommand.replace("${in}", srcFile.getAbsolutePath())
                .replace("${out}", distFilePath);
        try {
            runCommand(getThumbnailCommand, "make thumbnail");
            if (new File(distFilePath).exists()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isSupport(String fileSuffix) {
        return StringUtils.isNotBlank(fileSuffix) && DodoVideoUtil.support.contains(fileSuffix);
    }

    public static boolean convert(File srcFile) {
        if (!DodoVideoUtil.support.contains(VideoUtil.getFileSuffix(srcFile))) {
            return false;
        }

        if (!_convert_(srcFile, ConvertorType.TO_AVI)) {
            return false;
        }
        if (!_convert_(srcFile, ConvertorType.TO_FLV)) {
            return false;
        }
        if (!_convert_(srcFile, ConvertorType.TO_MP4)) {
            return false;
        }
        if (!_convert_(srcFile, ConvertorType.TO_SWF)) {
            return false;
        }

        try {
            addTimeFrame(getTargetFile(srcFile, ConvertorType.TO_FLV));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            makeThumbnail(getTargetFile(srcFile, ConvertorType.TO_FLV));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            getTargetFile(srcFile, ConvertorType.TO_AVI).delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private static boolean _convert_(File srcFile, ConvertorType convertorType) {
        try {
            File distFile = getTargetFile(srcFile, convertorType);
            File inputSrcFile = null;
            if (convertorType == ConvertorType.TO_AVI) {
                inputSrcFile = srcFile;
            } else {
                inputSrcFile = getTargetFile(srcFile, ConvertorType.TO_AVI);
            }

            String command = convertorType.getCommand().replace("${in}", inputSrcFile.getAbsolutePath())
                    .replace("${out}", distFile.getAbsolutePath());
            runCommand(command, "convert to " + convertorType.getCommandDesc());
            if (distFile.exists()) {
                if (distFile.length() > 0) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static VideoInfo getVideoInfo(File srcFile) {
        VideoInfo videoInfo = new VideoInfo();
        videoInfo.setVideoPath(srcFile.getAbsolutePath());
        String getInfoCommand = DodoVideoUtil.getInfoCommand.replace("${in}", videoInfo.getVideoPath());
        try {
            String commandOutPut = runCommand(getInfoCommand, "get video info").toString();
            Matcher matcher = PATT_BITRATE.matcher(commandOutPut);
            if (matcher.find()) {
                videoInfo.setBitrate(matcher.group(1));
            }
            matcher = PATT_DURATION.matcher(commandOutPut);
            if (matcher.find()) {
                videoInfo.setDuration(matcher.group(1));
            }
            matcher = PATT_SIZE.matcher(commandOutPut);
            if (matcher.find()) {
                videoInfo.setSize(matcher.group());
            }
            LOGGER.info(videoInfo.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return videoInfo;
    }

    private static boolean addTimeFrame(File srcFile) {
        String timeframeAddCommand = DodoVideoUtil.timeframeAddCommand.replace("${in}", srcFile.getAbsolutePath());
        try {
            if (!runCommand(timeframeAddCommand, "add timeframe").toString().contains("ERROR")) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static StringBuilder runCommand(String command, String infoTip) throws Exception {
        LOGGER.info(command);
        final StringBuilder commandOutPut = new StringBuilder("");
        final Process cmmandProcess = Runtime.getRuntime().exec(command);
        final InputStream inputStream = cmmandProcess.getInputStream();
        final InputStream errorStream = cmmandProcess.getErrorStream();
        Thread inputStreamThread = new Thread() {
            public void run() {
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                try {
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        commandOutPut.append(line);
                        LOGGER.info(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    LOGGER.error(e.getCause().getClass().getName());
                } finally {
                    if (br != null) {
                        try {
                            br.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };
        Thread errorStreamThread = new Thread() {
            public void run() {
                BufferedReader br = new BufferedReader(new InputStreamReader(errorStream));
                try {
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        LOGGER.info(line);
                        commandOutPut.append(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    LOGGER.error(e.getCause().getClass().getName());
                } finally {
                    if (br != null) {
                        try {
                            br.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };
        inputStreamThread.start();
        errorStreamThread.start();
        cmmandProcess.waitFor();
        while (inputStreamThread.isAlive() || errorStreamThread.isAlive()) {
            LOGGER.info(infoTip + " WaitFor Thread....");
            Thread.sleep(1000);
        }
        if (cmmandProcess != null) {
            cmmandProcess.destroy();
        }
        return commandOutPut;
    }

    public static String getAllSupport() {
        StringBuilder sbBuilder = new StringBuilder();
        for (String s : DodoVideoUtil.support) {
            sbBuilder.append(s).append(",");
        }
        return sbBuilder.toString();
    }
}
