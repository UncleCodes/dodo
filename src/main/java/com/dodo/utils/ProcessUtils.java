package com.dodo.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
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
public class ProcessUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessUtils.class);

    public static boolean findRunningProcess(String processName) {
        String platform = System.getProperty("os.name");
        LOGGER.info("当前平台名称：{}", platform);
        LOGGER.info("查找进程名称：{}", processName);
        if (platform.contains("Windows")) {
            return findRunningWindowsProcess(processName);
        } else if (platform.toLowerCase().contains("linux") || platform.toLowerCase().contains("centos")) {
            return findRunningLinuxProcess(processName);
        } else {
            throw new RuntimeException("Unknown platform " + platform);
        }
    }

    public static boolean isOsWindows() {
        String platform = System.getProperty("os.name");
        return platform.contains("Windows");
    }

    public static boolean isOsLinux() {
        String platform = System.getProperty("os.name");
        return platform.toLowerCase().contains("linux") || platform.toLowerCase().contains("centos");
    }

    private static boolean findRunningLinuxProcess(String processName) {
        LOGGER.info("查找 Linux 进程：{}", processName);
        String processNameLinux = StringUtils.substringBefore(processName, ".");

        try {
            List<String>[] outPut = execCommand("ps -A");
            List<String> inputOutPut = outPut[0];
            List<String> errorOutPut = outPut[1];
            for (String string : inputOutPut) {
                LOGGER.info("findRunningLinuxProcess:StdOutPut:{}", string);
                if (string.contains(processNameLinux)) {
                    LOGGER.info("findRunningLinuxProcess:Found:{}", processNameLinux);
                    return true;
                }
            }
            for (String string : errorOutPut) {
                LOGGER.info("findRunningLinuxProcess:ErrorOutPut:{}", string);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        LOGGER.info("findRunningLinuxProcess:WeNotFound:{}", processNameLinux);
        return false;
    }

    private static boolean findRunningWindowsProcess(String processName) {
        LOGGER.info("查找 windows 进程：{}", processName);
        BufferedReader bufferedReader = null;
        Process proc = null;
        try {
            proc = Runtime.getRuntime().exec("tasklist /FI \"IMAGENAME eq " + processName + "\"");
            bufferedReader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                LOGGER.info("查找 windows 进程：line:{}", line);
                if (line.contains(processName)) {
                    return true;
                }
            }
            return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (Exception ex) {
                }
            }
            if (proc != null) {
                try {
                    proc.destroy();
                } catch (Exception ex) {
                }
            }
        }
    }

    public static boolean killRunningProcess(String processName) {
        String platform = System.getProperty("os.name");
        if (platform.contains("Windows")) {
            return killRunningWindowsProcess(processName);
        } else if (platform.toLowerCase().contains("linux") || platform.toLowerCase().contains("centos")) {
            return killRunningLinuxProcess(processName);
        }
        throw new RuntimeException("Unkown platform " + platform);
    }

    private static boolean killRunningWindowsProcess(String processName) {
        LOGGER.info("杀死 Windows 进程：{}", processName);
        BufferedReader bufferedReader = null;
        Process proc = null;
        try {
            proc = Runtime.getRuntime().exec("taskkill /F /IM " + processName);
            bufferedReader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                LOGGER.info(line);
            }
            LOGGER.info("kill process successful");
            LOGGER.info("Process {} was killed. Mission completed.", processName);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.error("kill process fail");
            LOGGER.error("Misson failed.");
            return false;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (Exception ex) {
                }
            }
            if (proc != null) {
                try {
                    proc.destroy();
                } catch (Exception ex) {
                }
            }
        }
    }

    private static boolean killRunningLinuxProcess(String processName) {
        LOGGER.info("杀死 Linux 进程：" + processName);
        try {
            List<String>[] outPut = execCommand("pkill " + StringUtils.substringBefore(processName, "."));
            List<String> inputOutPut = outPut[0];
            List<String> errorOutPut = outPut[1];
            for (String string : inputOutPut) {
                LOGGER.info("killRunningLinuxProcess:StdOutPut:{}", string);
            }
            for (String string : errorOutPut) {
                LOGGER.info("killRunningLinuxProcess:ErrorOutPut:{}", string);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    public static List<String>[] execCommand(String command) throws InterruptedException, IOException {
        final List<String> processOutPut = new ArrayList<String>();
        final List<String> processErrorPut = new ArrayList<String>();
        LOGGER.info("RunNow:" + command);
        final Process execProcess = Runtime.getRuntime().exec(command);
        final InputStream inputStream = execProcess.getInputStream();
        final InputStream errorStream = execProcess.getErrorStream();
        Thread inputStreamThread = new Thread() {
            public void run() {
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                try {
                    String line = null;

                    while ((line = br.readLine()) != null) {
                        processOutPut.add(line);
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
                        //LOGGER.info(line);
                        processErrorPut.add(line);
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
        execProcess.waitFor();
        while (inputStreamThread.isAlive() || errorStreamThread.isAlive()) {
            LOGGER.info(" WaitFor Thread....");
            Thread.sleep(1000);
        }
        if (execProcess != null) {
            execProcess.destroy();
        }
        return new List[] { processOutPut, processErrorPut };
    }
}
