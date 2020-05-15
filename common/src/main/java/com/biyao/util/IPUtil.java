package com.biyao.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * @ClassName: IPUtil
 * @Description: ip解析工具类
 */
public class IPUtil {

    private static final Logger log = LoggerFactory.getLogger(IPUtil.class);


    private static final String WIN = "win";


    private static final String UNKNOWN = "unknown";

    /**
     * 获取用户真实IP地址，不使用request.getRemoteAddr();的原因是有可能用户使用了代理软件方式避免真实IP地址,
     * <p>
     * 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值，究竟哪个才是真正的用户端的真实IP呢？
     * 答案是取X-Forwarded-For中第一个非unknown的有效IP字符串。
     * <p>
     * 如：X-Forwarded-For：192.168.1.110, 192.168.1.120, 192.168.1.130,
     * 192.168.1.100
     * <p>
     * 用户真实IP为： 192.168.1.110
     *
     * @param request
     * @return
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }


    /**
     * 获取服务器地址
     *
     * @return Ip地址
     */
    public static String getServerIp() {
        // 获取操作系统类型
        String sysType = System.getProperties().getProperty("os.name");
        String ip;
        // 如果是Windows系统，获取本地IP地址
        if (sysType.toLowerCase().startsWith(WIN)) {
            String localIP = null;
            try {
                localIP = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                log.error(e.getMessage(), e);
            }
            if (localIP != null) {
                return localIP;
            }
        } else {
            ip = getLinuxIp();
            if (ip != null) {
                return ip;
            }
        }
        return "获取服务器IP错误";
    }

    /**
     * 根据网络接口获取IP地址
     *
     * @return
     */
    private static String getLinuxIp() {
        try {
            // enumerates all network interfaces
            Enumeration<NetworkInterface> enu = NetworkInterface.getNetworkInterfaces();
            while (enu.hasMoreElements()) {
                NetworkInterface ni = enu.nextElement();
                if (ni.isLoopback()) {
                    continue;
                }
                Enumeration<InetAddress> addressEnumeration = ni.getInetAddresses();
                while (addressEnumeration.hasMoreElements()) {
                    InetAddress address = addressEnumeration.nextElement();
                    // ignores all invalidated addresses
                    if (address.isLinkLocalAddress() || address.isLoopbackAddress() || address.isAnyLocalAddress()) {
                        continue;
                    }
                    return address.getHostAddress();
                }
            }
        } catch (SocketException e) {
            log.error(e.getMessage(), e);
        }
        return "获取服务器IP错误";
    }
}
