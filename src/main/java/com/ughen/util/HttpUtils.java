package com.ughen.util;

import com.ughen.constants.Constants;
import com.ughen.util.encryptionanddecode.AESUtil;
import com.ughen.util.encryptionanddecode.Base64Util;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpUtils {

    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url 发送请求的URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @param jiaMiType 加密方式  base64  与  aes
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param, String jiaMiType) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = "";
            if (Constants.BASE64.equalsIgnoreCase(jiaMiType)) {
                urlNameString = url + "?" + Base64Util.Encode("base64"+param);
            } else if (Constants.AES.equalsIgnoreCase(jiaMiType)) {
                urlNameString = url + "?" + AESUtil.aesEncode("aes="+param);
            } else {
                urlNameString = url + "?" + param;
            }
            System.out.println(urlNameString);
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (
                Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    /**
     * post提交，支付base64加密和aes加密
     *
     * @param urla 请求路径
     * @param param 加密内容
     * @param jiaMiType 加密方式 base64   与   aes
     */
    public static String JsonPost(String urla, String param, String jiaMiType) {
        String line = "";
        String message = "";
        String returnData = "";
        boolean postState = false;
        BufferedReader bufferedReader = null;

        try {
            URL urlObject = new URL(urla);
            HttpURLConnection urlConn = (HttpURLConnection) urlObject.openConnection();
            urlConn.setDoOutput(true);
            /*设定禁用缓存*/
//	          urlConn.setRequestProperty("Pragma:", "no-cache");
            urlConn.setRequestProperty("Cache-Control", "no-cache");
            /*维持长连接*/
            urlConn.setRequestProperty("Connection", "Keep-Alive");
            /*设置字符集*/
            urlConn.setRequestProperty("Charset", "UTF-8");
            /*设定输出格式为json*/
            urlConn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            /*设置使用POST的方式发送*/
            urlConn.setRequestMethod("POST");
            /*设置不使用缓存*/
            urlConn.setUseCaches(false);
            /*设置容许输出*/
            urlConn.setDoOutput(true);
            /*设置容许输入*/
            urlConn.setDoInput(true);
            urlConn.connect();

            OutputStreamWriter outStreamWriter = new OutputStreamWriter(urlConn.getOutputStream(),
                    "UTF-8");
            if (Constants.BASE64.equalsIgnoreCase(jiaMiType)) {
                outStreamWriter.write(Base64Util.Encode(param));
            } else if (Constants.AES.equalsIgnoreCase(jiaMiType)) {
                outStreamWriter.write(AESUtil.aesEncode(param));
            } else {
                outStreamWriter.write(param);
            }

            outStreamWriter.flush();
            outStreamWriter.close();

            /*若post失败*/
            if ((urlConn.getResponseCode() != 200)) {
                returnData = "{\"errcode\":41001,\"errmsg\":\"access_token missing hint: [h0b2ca0075vr32!]\"}";
                message =
                        "发送POST失败！" + "code=" + urlConn.getResponseCode() + "," + "失败消息：" + urlConn
                                .getResponseMessage();
                // 定义BufferedReader输入流来读取URL的响应
                InputStream errorStream = urlConn.getErrorStream();

                if (errorStream != null) {
                    InputStreamReader inputStreamReader = new InputStreamReader(errorStream,
                            "utf-8");
                    bufferedReader = new BufferedReader(inputStreamReader);

                    while ((line = bufferedReader.readLine()) != null) {
                        message += line;
                    }
                    inputStreamReader.close();
                }
                errorStream.close();
                System.out.println("发送失败！错误信息为：" + message);
            } else {
                /*发送成功返回发送成功状态*/
                postState = true;

                // 定义BufferedReader输入流来读取URL的响应
                InputStream inputStream = urlConn.getInputStream();

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
                bufferedReader = new BufferedReader(inputStreamReader);

                while ((line = bufferedReader.readLine()) != null) {
                    message += line;
                }
                returnData = message;
                inputStream.close();
                inputStreamReader.close();
                System.out.println("发送POST成功！返回内容为：" + message);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return returnData;
        }

    }


    /**
     * 新浪第三方登录使用此方法
     * x-www-form-urlencoded
     */
    public static final String DEFAULT_CODING = "UTF-8";

    public static String postUrlEncodeForm(String url, Map<String, Object> map, String coding)
            throws Exception {
        if (null == coding || "".equals(coding)) {
            coding = DEFAULT_CODING;
        }

        String result = "";

        //处理请求参数
        List<NameValuePair> valuePairs = new ArrayList<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            NameValuePair valuePair = new BasicNameValuePair(entry.getKey(),
                    entry.getValue().toString());
            valuePairs.add(valuePair);
        }

        //设置client参数
        HttpClient client = HttpClientBuilder.create().build();

        //发送请求
        HttpPost post = new HttpPost(url);
        HttpEntity entity = new UrlEncodedFormEntity(valuePairs, coding);
        post.setEntity(entity);
        HttpResponse response = client.execute(post);

        //处理响应结果
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != 200) {
            throw new RuntimeException("statusCode = [" + statusCode + "]");
        } else {
            HttpEntity respEntity = response.getEntity();
            result = EntityUtils.toString(respEntity, coding);
        }
        return result;
    }




    /**
     * get
     */
    public static HttpResponse doGet(String host, String path, String method,
            Map<String, String> headers,
            Map<String, String> querys)
            throws Exception {
        HttpClient httpClient = wrapClient(host);

        HttpGet request = new HttpGet(buildUrl(host, path, querys));
        for (Map.Entry<String, String> e : headers.entrySet()) {
            request.addHeader(e.getKey(), e.getValue());
        }

        return httpClient.execute(request);
    }

    /**
     * post form
     */
    public static HttpResponse doPost(String host, String path, String method,
            Map<String, String> headers,
            Map<String, String> querys,
            Map<String, String> bodys)
            throws Exception {
        HttpClient httpClient = wrapClient(host);

        HttpPost request = new HttpPost(buildUrl(host, path, querys));
        for (Map.Entry<String, String> e : headers.entrySet()) {
            request.addHeader(e.getKey(), e.getValue());
        }

        if (bodys != null) {
            List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();

            for (String key : bodys.keySet()) {
                nameValuePairList.add(new BasicNameValuePair(key, bodys.get(key)));
            }
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(nameValuePairList, "utf-8");
            formEntity.setContentType("application/x-www-form-urlencoded; charset=UTF-8");
            request.setEntity(formEntity);
        }

        return httpClient.execute(request);
    }

    /**
     * Post String
     */
    public static HttpResponse doPost(String host, String path, String method,
            Map<String, String> headers,
            Map<String, String> querys,
            String body)
            throws Exception {
        HttpClient httpClient = wrapClient(host);

        HttpPost request = new HttpPost(buildUrl(host, path, querys));
        for (Map.Entry<String, String> e : headers.entrySet()) {
            request.addHeader(e.getKey(), e.getValue());
        }

        if (StringUtils.isNotBlank(body)) {
            request.setEntity(new StringEntity(body, "utf-8"));
        }

        return httpClient.execute(request);
    }

    /**
     * Post stream
     */
    public static HttpResponse doPost(String host, String path, String method,
            Map<String, String> headers,
            Map<String, String> querys,
            byte[] body)
            throws Exception {
        HttpClient httpClient = wrapClient(host);

        HttpPost request = new HttpPost(buildUrl(host, path, querys));
        for (Map.Entry<String, String> e : headers.entrySet()) {
            request.addHeader(e.getKey(), e.getValue());
        }

        if (body != null) {
            request.setEntity(new ByteArrayEntity(body));
        }

        return httpClient.execute(request);
    }

    /**
     * Put String
     */
    public static HttpResponse doPut(String host, String path, String method,
            Map<String, String> headers,
            Map<String, String> querys,
            String body)
            throws Exception {
        HttpClient httpClient = wrapClient(host);

        HttpPut request = new HttpPut(buildUrl(host, path, querys));
        for (Map.Entry<String, String> e : headers.entrySet()) {
            request.addHeader(e.getKey(), e.getValue());
        }

        if (StringUtils.isNotBlank(body)) {
            request.setEntity(new StringEntity(body, "utf-8"));
        }

        return httpClient.execute(request);
    }

    /**
     * Put stream
     */
    public static HttpResponse doPut(String host, String path, String method,
            Map<String, String> headers,
            Map<String, String> querys,
            byte[] body)
            throws Exception {
        HttpClient httpClient = wrapClient(host);

        HttpPut request = new HttpPut(buildUrl(host, path, querys));
        for (Map.Entry<String, String> e : headers.entrySet()) {
            request.addHeader(e.getKey(), e.getValue());
        }

        if (body != null) {
            request.setEntity(new ByteArrayEntity(body));
        }

        return httpClient.execute(request);
    }

    /**
     * Delete
     */
    public static HttpResponse doDelete(String host, String path, String method,
            Map<String, String> headers,
            Map<String, String> querys)
            throws Exception {
        HttpClient httpClient = wrapClient(host);

        HttpDelete request = new HttpDelete(buildUrl(host, path, querys));
        for (Map.Entry<String, String> e : headers.entrySet()) {
            request.addHeader(e.getKey(), e.getValue());
        }

        return httpClient.execute(request);
    }

    private static String buildUrl(String host, String path, Map<String, String> querys)
            throws UnsupportedEncodingException {
        StringBuilder sbUrl = new StringBuilder();
        sbUrl.append(host);
        if (!StringUtils.isBlank(path)) {
            sbUrl.append(path);
        }
        if (null != querys) {
            StringBuilder sbQuery = new StringBuilder();
            for (Map.Entry<String, String> query : querys.entrySet()) {
                if (0 < sbQuery.length()) {
                    sbQuery.append("&");
                }
                if (StringUtils.isBlank(query.getKey()) && !StringUtils.isBlank(query.getValue())) {
                    sbQuery.append(query.getValue());
                }
                if (!StringUtils.isBlank(query.getKey())) {
                    sbQuery.append(query.getKey());
                    if (!StringUtils.isBlank(query.getValue())) {
                        sbQuery.append("=");
                        sbQuery.append(URLEncoder.encode(query.getValue(), "utf-8"));
                    }
                }
            }
            if (0 < sbQuery.length()) {
                sbUrl.append("?").append(sbQuery);
            }
        }

        return sbUrl.toString();
    }

    private static HttpClient wrapClient(String host) {
        HttpClient httpClient = new DefaultHttpClient();
        if (host.startsWith("https://")) {
            sslClient(httpClient);
        }

        return httpClient;
    }


    private static void sslClient(HttpClient httpClient) {
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkClientTrusted(X509Certificate[] xcs, String str) {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] xcs, String str) {

                }
            };
            ctx.init(null, new TrustManager[]{tm}, null);
            SSLSocketFactory ssf = new SSLSocketFactory(ctx);
            ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            ClientConnectionManager ccm = httpClient.getConnectionManager();
            SchemeRegistry registry = ccm.getSchemeRegistry();
            registry.register(new Scheme("https", 443, ssf));
        } catch (KeyManagementException ex) {
            throw new RuntimeException(ex);
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }
}