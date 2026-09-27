package cn.handyplus.guild.lib;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.logging.Level;

public final class NetUtil {
    private static volatile boolean missingMacWarned;

    private NetUtil() {
    }

    public static String getLocalMacAddress() {
        Optional<String> mac = oooOoo(iiIIii(), "-");
        if (mac.isPresent()) {
            return mac.get();
        }
        if (!missingMacWarned) {
            missingMacWarned = true;
            if (InitApi.PLUGIN != null) {
                InitApi.PLUGIN.getLogger().warning("Unable to read a local MAC address; the public IP fallback is disabled. "
                        + "Use signType other than \"mac\" if this server is licensed by IP.");
            }
        }
        return "";
    }

    private static Optional<String> oooOoo(InetAddress address, String separator) {
        if (address == null) {
            return Optional.empty();
        }
        byte[] mac = PPPPPp(address);
        if (mac == null) {
            return Optional.empty();
        }
        StringBuilder builder = new StringBuilder();
        for (int index = 0; index < mac.length; index++) {
            if (index != 0) {
                builder.append(separator);
            }
            String hex = Integer.toHexString(mac[index] & 0xFF);
            builder.append(hex.length() == 1 ? 0 + hex : hex);
        }
        return Optional.of(builder.toString());
    }

    private static InetAddress iiIIii() {
        LinkedHashSet<InetAddress> addresses = XXxXxx(address -> !address.isLoopbackAddress() && address instanceof Inet4Address);
        if (CollUtil.isNotEmpty(addresses)) {
            InetAddress siteLocal = null;
            for (InetAddress address : addresses) {
                if (!address.isSiteLocalAddress()) {
                    return address;
                }
                if (siteLocal == null) {
                    siteLocal = address;
                }
            }
            if (siteLocal != null) {
                return siteLocal;
            }
        }
        try {
            return InetAddress.getLocalHost();
        } catch (UnknownHostException error) {
            if (BaseConstants.DEBUG) {
                InitApi.PLUGIN.getLogger().log(Level.SEVERE, "error:", error);
            }
            return null;
        }
    }

    private static LinkedHashSet<InetAddress> XXxXxx(F<InetAddress> filter) {
        Enumeration<NetworkInterface> interfaces;
        try {
            interfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException error) {
            throw new RuntimeException(error);
        }
        LinkedHashSet<InetAddress> result = new LinkedHashSet<>();
        while (interfaces.hasMoreElements()) {
            Enumeration<InetAddress> addresses = interfaces.nextElement().getInetAddresses();
            while (addresses.hasMoreElements()) {
                InetAddress address = addresses.nextElement();
                if (address == null || filter != null && !filter.XXXxxx(address)) {
                    continue;
                }
                result.add(address);
            }
        }
        return result;
    }

    private static byte[] PPPPPp(InetAddress address) {
        if (address == null) {
            return null;
        }
        try {
            NetworkInterface networkInterface = NetworkInterface.getByInetAddress(address);
            if (networkInterface != null) {
                return networkInterface.getHardwareAddress();
            }
        } catch (SocketException error) {
            throw new RuntimeException(error);
        }
        return null;
    }
}
