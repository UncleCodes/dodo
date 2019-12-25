package com.dodo.common.framework.entity.id;

import javax.annotation.PostConstruct;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
@Configuration
@PropertySource("classpath:/idworker.properties")
public class IdWorkerImpl implements IdWorker {
    private static final Logger LOGGER             = LoggerFactory.getLogger(IdWorkerImpl.class);
    @Autowired
    private Environment         env;

    private final static long   twepoch            = 1288834974657L;
    // 机器标识位数
    private final static long   workerIdBits       = 5L;
    // 数据中心标识位数
    private final static long   datacenterIdBits   = 5L;
    // 机器ID最大值
    private final static long   maxWorkerId        = -1L ^ (-1L << workerIdBits);
    // 数据中心ID最大值
    private final static long   maxDatacenterId    = -1L ^ (-1L << datacenterIdBits);
    // 毫秒内自增位
    private final static long   sequenceBits       = 12L;
    // 机器ID偏左移12位
    private final static long   workerIdShift      = sequenceBits;
    // 数据中心ID左移17位
    private final static long   datacenterIdShift  = sequenceBits + workerIdBits;
    // 时间毫秒左移22位
    private final static long   timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;

    private final static long   sequenceMask       = -1L ^ (-1L << sequenceBits);

    private static long         lastTimestamp      = -1L;

    private long                sequence           = 0L;
    private long                workerId           = 1L;
    private long                datacenterId       = 1L;

    @PostConstruct
    public void init() {
        workerId = env.getProperty("com.dodo.id.idworker.workerId", long.class);
        datacenterId = env.getProperty("com.dodo.id.idworker.datacenterId", long.class);
        LOGGER.info("**********IdWorkerImpl****************");
        LOGGER.info("Init.........OK.......{" + datacenterId + "," + workerId + "}");
        LOGGER.info("**********IdWorkerImpl****************");
    }

    public IdWorkerImpl() {

    }

    public IdWorkerImpl(long workerId, long datacenterId) {
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException("worker Id can't be greater than %d or less than 0");
        }
        if (datacenterId > maxDatacenterId || datacenterId < 0) {
            throw new IllegalArgumentException("datacenter Id can't be greater than %d or less than 0");
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }

    public synchronized Long nextId() {
        long timestamp = timeGen();
        if (timestamp < lastTimestamp) {
            try {
                throw new Exception("Clock moved backwards.  Refusing to generate id for "
                        + (lastTimestamp - timestamp) + " milliseconds");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (lastTimestamp == timestamp) {
            // 当前毫秒内，则+1
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                // 当前毫秒内计数满了，则等待下一秒
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0;
        }
        lastTimestamp = timestamp;
        // ID偏移组合生成最终的ID，并返回ID
        long nextId = ((timestamp - twepoch) << timestampLeftShift) | (datacenterId << datacenterIdShift)
                | (workerId << workerIdShift) | sequence;

        return nextId;
    }

    private long tilNextMillis(final long lastTimestamp) {
        long timestamp = this.timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = this.timeGen();
        }
        return timestamp;
    }

    private long timeGen() {
        return System.currentTimeMillis();
    }

    @Override
    public String nextStrId() {
        return DigestUtils.md5Hex(String.valueOf(nextId()));
    }

    public static long getMaxdatacenterid() {
        return maxDatacenterId;
    }

    public static long getMaxworkerid() {
        return maxWorkerId;
    }

}
