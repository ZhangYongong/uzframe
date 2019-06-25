package com.ughen.util;

import org.slf4j.Logger;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 埋点数据异步处理
 */

public class DataLogHelper {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(DataLogHelper.class);

    private static AtomicLong logseq = new AtomicLong(0);

    // hostname
    private static String hostname = "unknown";

    // appid
    private static String appId = "uz_frame";

    // id prefix
    private static String idprefix = String.format("%s-%x-", appId, System.currentTimeMillis()
            - 45L * 365 * 24 * 3600 * 1000);

    // module name
    private String moduleName;

    // session id
    private String sessionUniqId = null;

    static {
        try {
            hostname = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    /**
     *  log format
     */
    public String log(String metric, String logs) {
        if (metric == null || logs == null) {
            return null;
        }

        StringBuffer sb = new StringBuffer("{");
        sb.append(String.format("\"m_name\": \"%s\", ", metric));
        sb.append(String.format("\"m_host\": \"%s\", ", hostname));
        sb.append(String.format("\"m_time\": \"%s\", ", String.valueOf(System.currentTimeMillis())));
        sb.append(String.format("\"m_appid\": \"%s\", ", appId));
        sb.append(String.format("\"m_module\": \"%s\", ", moduleName));
        sb.append(String.format("\"m_id\": \"%s\", ", sessionUniqId != null ? sessionUniqId : getUniqId()));
        if (logs.startsWith("{")) {
            // "{}"
            if (logs.substring(1).trim().equals("}")) {
                sb.append("\"value\": \"{}\"}");
            } else {
                sb.append(logs.substring(1));
            }
        } else if (logs.startsWith("[")) {
            sb.append("\"value\": ");
            sb.append(logs).append("}");
        } else {
            sb.append(String.format("\"value\": \"%s\"}", logs.replace("\"", "'")));
        }

        logger.info("{}", sb.toString());

        return sb.toString();
    }


    public static DataLogHelper getLogger(String moduleName) {
        return new DataLogHelper(moduleName);
    }


    public static DataLogHelper getSessionLogger(String moduleName) {
        return new DataLogHelper(moduleName, "session");
    }

    private DataLogHelper(String module) {
        moduleName = module;
        sessionUniqId = null;
    }

    private DataLogHelper(String module, String xx) {
        moduleName = module;
        sessionUniqId = getUniqId();
    }

    private String getUniqId() {
        return idprefix + logseq.getAndIncrement();
    }
}

