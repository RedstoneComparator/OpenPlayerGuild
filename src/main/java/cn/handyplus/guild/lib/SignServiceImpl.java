package cn.handyplus.guild.lib;

import cn.handyplus.guild.auth.AuthorizationIdentity;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.regex.Matcher;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.bukkit.entity.Player;

class SignServiceImpl implements SignService {
    private static final int MAX_RETRY_NUMBER = 6;
    private static final long VERIFY_DELAY = 1200L;
    private static final String SIGN_VERSION = "v15";
    private static final int NONCE_LENGTH = 32;
    private static final int GCM_IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH = 128;
    private static final int AES_KEY_LENGTH = 16;
    private static final int CONNECT_TIMEOUT = 10000;
    private static final int READ_TIMEOUT = 10000;
    private static final String REQUEST_TYPE_JSON = "application/json; charset=utf-8";
    private static final String SUCCEED_MSG_TEMPLATE = "&a%s插件验证成功,感谢您的支持";
    private static final String FAILURE_MSG_TEMPLATE = "&a%s插件未检测到激活码,请联系作者购买QQ:956812056";
    private static final String ERROR_MSG_TEMPLATE = "&a%s检测到网络不稳定,一分钟后自动重新验证";
    protected static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCpS1CL2SK8gRDkv80AjX5+5C1CxCEKb9C5iL9+hp5f9B/OGiBNYSmbHWxXdijar7x9FPEwlWdyBA44ef+3ww8JF2tEb962RxsNbW207yf7eJQAmK+BPI4pBBtcS4w3umjpBheQLrvnFHkTOv16hnaPMtKrWyP9elfTjvJszVhfpQIDAQAB";
    private static volatile String URL = "https://admin.ljxmc.top";
    private static final String VERIFY_SIGN = "/api/public/verifySign";
    private static final Map<Integer, String> SECRET_KEY_MAP = buildSecretKeyMap();
    private static volatile boolean API_RESULT;
    private static volatile boolean DB_RESULT;
    private static volatile String SECRET_KEY;
    private static volatile boolean VERIFY_SCHEDULED;

    SignServiceImpl() {
    }

    public boolean isSuccess() {

        return true;
    }

    public boolean isInit() {
        return API_RESULT;
    }

    public void setDbResult(Boolean dbInit) {
        DB_RESULT = dbInit;
    }

    public void noticeOp(Player player) {
        if (!player.isOp()) {
            return;
        }
        if (!isSuccess()) {
            MessageUtil.sendMessage(player, formatMsg(FAILURE_MSG_TEMPLATE));
        }
    }

    public boolean isFailure(String command) {

        return false;
    }

    public void set(String url) {
        if (StrUtil.isNotEmpty(url)) {
            URL = url;
        }
    }

    public String get() {
        return URL;
    }

    public void init(int serviceId) {
        SECRET_KEY = getSecretKeyByPluginId(serviceId);
        if (StrUtil.isNotEmpty(SECRET_KEY)) {
            init();
        }
    }

    public void init() {
        if (StrUtil.isEmpty(SECRET_KEY)) {
            return;
        }
        synchronized (SignServiceImpl.class) {
            if (VERIFY_SCHEDULED) {
                return;
            }
            VERIFY_SCHEDULED = true;
        }
        scheduleVerify(new AtomicInteger(MAX_RETRY_NUMBER), 0L);
    }

    private void scheduleVerify(AtomicInteger retryNumber, long delay) {
        HandySchedulerUtil.runTaskLaterAsynchronously(() -> verify(retryNumber), delay);
    }

    private void verify(AtomicInteger retryNumber) {
        try {
            String sign = BaseConstants.CONFIG.getString("sign", "123456");
            String nonce = generateNonce();
            Map<String, String> headers = buildHeaders();
            String signToken = headers.get("signToken");
            String json = post(URL + VERIFY_SIGN, buildRequestJson(sign, nonce), headers);
            Map<String, String> resultMap = JsonUtil.toMap(json);
            boolean result = Boolean.parseBoolean(resultMap.get("result"));
            if (result) {
                parseAndInitDb(resultMap.get("param"), nonce, signToken, sign);
            }
            API_RESULT = result;
            MessageUtil.sendConsoleMessage(formatMsg(API_RESULT ? SUCCEED_MSG_TEMPLATE : FAILURE_MSG_TEMPLATE));
            VERIFY_SCHEDULED = false;
        } catch (Exception exception) {
            handleVerifyError(retryNumber, exception);
        }
    }

    private Map<String, String> buildHeaders() {
        Map<String, String> headers = MapUtil.newHashMapWithExpectedSize(2);
        String token;
        if (isMacSignType()) {
            token = NetUtil.getLocalMacAddress() + "_" + SECRET_KEY + "_" + AuthorizationIdentity.pluginName();
        } else {
            token = SECRET_KEY + "_" + AuthorizationIdentity.pluginName();
        }
        headers.put("signToken", SecureUtil.md5Str(token));
        headers.put("signVersion", SIGN_VERSION);
        return headers;
    }

    private String buildRequestJson(String sign, String nonce) {
        Map<String, String> paramMap = MapUtil.newHashMapWithExpectedSize(7);
        paramMap.put("sign", sign);
        paramMap.put("port", String.valueOf(InitApi.PLUGIN.getServer().getPort()));
        paramMap.put("pluginName", AuthorizationIdentity.pluginName());
        paramMap.put("version", AuthorizationIdentity.pluginVersion());
        paramMap.put("secretKey", SECRET_KEY);
        paramMap.put("nonce", nonce);
        if (isMacSignType()) {
            paramMap.put("mac", NetUtil.getLocalMacAddress());
        }
        return JsonUtil.toJson(paramMap);
    }

    private void parseAndInitDb(String param, String nonce, String signToken, String sign) throws Exception {
        String decrypted = decryptWithNonce(param, nonce, signToken, sign);
        String plain = decryptByPublicKey(decrypted);
        Map<String, String> map = JsonUtil.toMap(plain);
        boolean valid = Boolean.parseBoolean(map.get("result"))
                && nonce.equals(map.get("nonce"))
                && AuthorizationIdentity.pluginName().equals(map.get("pluginName"))
                && sha256(sign).equals(map.get("signHash"));
        if (!valid) {
            throw new SecurityException("sign response validation failed");
        }
    }

    private String decryptWithNonce(String encryptedText, String nonce, String signToken, String sign) throws Exception {
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText);
        if (encryptedBytes.length <= GCM_IV_LENGTH) {
            throw new SecurityException("invalid sign response");
        }
        byte[] iv = Arrays.copyOfRange(encryptedBytes, 0, GCM_IV_LENGTH);
        byte[] cipherText = Arrays.copyOfRange(encryptedBytes, GCM_IV_LENGTH, encryptedBytes.length);
        String source = SIGN_VERSION + "|" + nonce + "|" + signToken + "|" + sign;
        byte[] hash = MessageDigest.getInstance("SHA-256").digest(source.getBytes(StandardCharsets.UTF_8));
        SecretKeySpec key = new SecretKeySpec(Arrays.copyOf(hash, AES_KEY_LENGTH), "AES");
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(GCM_TAG_LENGTH, iv));
        return new String(cipher.doFinal(cipherText), StandardCharsets.UTF_8);
    }

    private String generateNonce() {
        byte[] nonce = new byte[NONCE_LENGTH];
        new SecureRandom().nextBytes(nonce);
        return Base64.getEncoder().encodeToString(nonce);
    }

    private String sha256(String input) throws Exception {
        byte[] bytes = MessageDigest.getInstance("SHA-256").digest(input.getBytes(StandardCharsets.UTF_8));
        StringBuilder result = new StringBuilder(bytes.length * 2);
        for (byte value : bytes) {
            int number = value & 0xFF;
            if (number < 16) {
                result.append('0');
            }
            result.append(Integer.toHexString(number));
        }
        return result.toString();
    }

    private void handleVerifyError(AtomicInteger retryNumber, Exception exception) {
        if (retryNumber.get() < MAX_RETRY_NUMBER) {
            MessageUtil.sendConsoleMessage(formatMsg(ERROR_MSG_TEMPLATE));
        }
        retryNumber.getAndDecrement();
        if (retryNumber.get() > 0) {
            scheduleVerify(retryNumber, VERIFY_DELAY);
            return;
        }
        VERIFY_SCHEDULED = false;
        if (BaseConstants.DEBUG) {
            InitApi.PLUGIN.getLogger().log(Level.SEVERE, "start error", exception);
        }
    }

    private String decryptByPublicKey(String encryptedText) throws Exception {
        byte[] publicKeyBytes = Base64.getDecoder().decode(PUBLIC_KEY);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText);
        int inputLength = encryptedBytes.length;
        int maxBlockSize = 128;
        int offset = 0;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        while (inputLength - offset > 0) {
            int blockSize = Math.min(inputLength - offset, maxBlockSize);
            byte[] decryptedBlock = cipher.doFinal(encryptedBytes, offset, blockSize);
            outputStream.write(decryptedBlock);
            offset += blockSize;
        }
        return new String(outputStream.toByteArray(), StandardCharsets.UTF_8);
    }

    private String getSecretKeyByPluginId(int pluginId) {
        return SECRET_KEY_MAP.get(pluginId);
    }

    private boolean isMacSignType() {
        return "mac".equalsIgnoreCase(BaseConstants.CONFIG.getString("signType", "mac"));
    }

    private String formatMsg(String template) {
        return String.format(template, InitApi.PLUGIN.getName());
    }

    private String post(String url, String requestContent, Map<String, String> headers) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        try {
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setConnectTimeout(CONNECT_TIMEOUT);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setRequestProperty("Accept", "text/xml,text/javascript,text/html,application/json");
            connection.setRequestProperty("Content-Type", REQUEST_TYPE_JSON);
            if (MapUtil.isNotEmpty(headers)) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    connection.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }
            if (StrUtil.isNotEmpty(requestContent)) {
                try (OutputStream outputStream = connection.getOutputStream()) {
                    outputStream.write(requestContent.getBytes(StandardCharsets.UTF_8));
                }
            }
            String response = readResponse(connection);
            if (BaseConstants.DEBUG) {
                String msg = "url: " + url + " method: POST requestContent: " + requestContent + " headers: " + JsonUtil.toJson(headers);
                MessageUtil.sendConsoleDebugMessage(msg);
            }
            return response;
        } finally {
            connection.disconnect();
        }
    }

    private String readResponse(HttpURLConnection connection) throws IOException {
        InputStream inputStream = connection.getErrorStream();
        if (inputStream == null) {
            inputStream = connection.getInputStream();
        }
        String charset = StandardCharsets.UTF_8.name();
        String contentType = connection.getContentType();
        if (StrUtil.isNotEmpty(contentType)) {
            Matcher matcher = PatternUtil.HTTP_CHARSET.matcher(contentType);
            if (matcher.find()) {
                charset = matcher.group(1);
            }
        }
        try (InputStream stream = inputStream;
             Reader reader = new InputStreamReader(stream, charset)) {
            StringBuilder response = new StringBuilder();
            char[] buffer = new char[1024];
            int length;
            while ((length = reader.read(buffer)) > 0) {
                response.append(buffer, 0, length);
            }
            return response.toString();
        }
    }

    private static Map<Integer, String> buildSecretKeyMap() {
        Map<Integer, String> secretKeyMap = MapUtil.newHashMapWithExpectedSize(9);
        secretKeyMap.put(20801, "1749424898136682496");
        secretKeyMap.put(8144, "1285435377473163264");
        secretKeyMap.put(6913, "1285435373509545984");
        secretKeyMap.put(12551, "1477494807545909248");
        secretKeyMap.put(15977, "1554047410630692864");
        secretKeyMap.put(18238, "1648656493580374016");
        secretKeyMap.put(25044, "1899754037107015680");
        secretKeyMap.put(28512, "2004033978125725696");
        secretKeyMap.put(30863, "2046032048008265728");
        return secretKeyMap;
    }
}
