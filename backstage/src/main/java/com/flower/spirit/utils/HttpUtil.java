package com.flower.spirit.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.flower.spirit.config.Global;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 这个方法中有大量遗弃方法不再调用
 */
@SuppressWarnings("deprecation")
public class HttpUtil {

    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    /**
     * web 端
     * 
     * @param url
     * @param param
     * @return
     */
    public static String httpGet(String url, String param) {
        HttpClient httpClient = new HttpClient();
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
        GetMethod getMethod = new GetMethod(url);
        getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 5000);
        getMethod.getParams().setParameter("user-agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36");
        getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
        String response = "";
        try {
            int statusCode = httpClient.executeMethod(getMethod);
            if (statusCode != HttpStatus.SC_OK) {
                System.err.println("请求出错: " + getMethod.getStatusLine());
            }
            byte[] responseBody = getMethod.getResponseBody();
            response = new String(responseBody, param);
        } catch (HttpException e) {
            System.out.println("请检查输入的URL!");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("发生网络异常!");
            e.printStackTrace();
        } finally {
            /* 6 .释放连接 */
            getMethod.releaseConnection();
        }
        return response;
    }

    /**
     * 用于验证bili登录接口 并取出cookie
     * 
     * @param url
     * @param param
     * @return
     */
    public static String httpGetBypoll(String url, String param) {
        HttpClient httpClient = new HttpClient();
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
        GetMethod getMethod = new GetMethod(url);
        getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 5000);
        getMethod.getParams().setParameter("user-agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36");
        getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
        String response = "";
        try {
            int statusCode = httpClient.executeMethod(getMethod);
            if (statusCode != HttpStatus.SC_OK) {
                return null;
            }
            byte[] responseBody = getMethod.getResponseBody();
            response = new String(responseBody, param);
            String code = JSONObject.parseObject(response).getJSONObject("data").getString("code");
            if (code.equals("0")) {
                // 登录成功
                Header[] headers = getMethod.getResponseHeaders();
                String cookie = "";
                for (Header h : headers) {
                    if (h.getName().equals("Set-Cookie")) {
                        String str = h.getValue().split(";")[0];
                        cookie = cookie + ";" + str;
                    }
                }
                return cookie.substring(1, cookie.length());
            } else {
                return null;
            }

        } catch (HttpException e) {
            System.out.println("请检查输入的URL!");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("发生网络异常!");
            e.printStackTrace();
        } finally {
            /* 6 .释放连接 */
            getMethod.releaseConnection();
        }
        return null;
    }

    /**
     * json 字符串
     * 
     * @param url
     * @param param
     * @return
     */
    public static String getSerchPersion(String url, String param) {
        HttpClient httpClient = new HttpClient();
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
        GetMethod getMethod = new GetMethod(url);
        getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 5000);
        getMethod.getParams().setParameter("user-agent",Global.configInfo.getSerchPersion.getValue());
        getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
        StringBuilder response = new StringBuilder();
        try {
            int statusCode = httpClient.executeMethod(getMethod);
            if (statusCode != HttpStatus.SC_OK) {
                System.err.println("请求出错: " + getMethod.getStatusLine());
            } else {
                try (InputStream is = getMethod.getResponseBodyAsStream();
                     BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                }
            }
        } catch (HttpException e) {
            System.out.println("请检查输入的URL!");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("发生网络异常!");
            e.printStackTrace();
        } finally {
            getMethod.releaseConnection();
        }
        return response.toString();
    }

    public static String httpGetBili(String url, String param, String cookie) {
        HttpClient httpClient = new HttpClient();

        httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
        GetMethod getMethod = new GetMethod(url);

        getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 5000);
        getMethod.getParams().setParameter("user-agent",Global.configInfo.getSerchPersion.getValue());
        getMethod.addRequestHeader("user-agent",Global.configInfo.getSerchPersion.getValue());
        if (null != cookie && !cookie.equals("")) {
            getMethod.addRequestHeader("cookie", cookie);
        }

        getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
        StringBuilder response = new StringBuilder();
        try {
            int statusCode = httpClient.executeMethod(getMethod);
            if (statusCode != HttpStatus.SC_OK) {
                System.err.println("请求出错: " + getMethod.getStatusLine());
            } else {
                try (InputStream is = getMethod.getResponseBodyAsStream();
                     InputStreamReader isr = new InputStreamReader(is, "UTF-8");
                     BufferedReader reader = new BufferedReader(isr)) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                }
            }
        } catch (HttpException e) {
            System.out.println("请检查输入的URL!");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("发生网络异常!");
            e.printStackTrace();
        } finally {
            getMethod.releaseConnection();
        }
        return response.toString();
    }
    
    public static String httpGetBili(String url, String cookie,String origin,String referer ) {
    	String ua = null!=Global.useragent && !"".equals(Global.useragent)?Global.useragent:"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/135.0.0.0 Safari/537.36";
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)    // 连接超时
                .readTimeout(5, TimeUnit.SECONDS)       // 读取超时
                .build();
        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .get()
                .header("User-Agent", ua);
        if (origin != null && !origin.isEmpty()) {
            requestBuilder.header("Origin", origin);
        }
        if (referer != null && !referer.isEmpty()) {
            requestBuilder.header("Referer", referer);
        }
        if (cookie != null && !cookie.isEmpty()) {
            requestBuilder.header("Cookie", cookie);
        }
        Request request = requestBuilder.build();
        String responseStr = "";
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.err.println("请求出错: " + response.code() + " - " + response.message());
            } else {
                ResponseBody body = response.body();
                if (body != null) {
                    byte[] bytes = body.bytes(); 
                    responseStr = new String(bytes, Charset.forName("UTF-8"));
                }
            }
        } catch (IOException e) {
            System.out.println("发生网络异常!");
            e.printStackTrace();
        }
        return responseStr;
    }
    
    public static byte[] httpGetBiliBytes(String url, String cookie, String origin, String referer) {
        String ua = (Global.useragent != null && !"".equals(Global.useragent))
                ? Global.useragent
                : "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/135.0.0.0 Safari/537.36";

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .build();

        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .get()
                .header("User-Agent", ua);

        if (origin != null && !origin.isEmpty()) {
            requestBuilder.header("Origin", origin);
        }
        if (referer != null && !referer.isEmpty()) {
            requestBuilder.header("Referer", referer);
        }
        if (cookie != null && !cookie.isEmpty()) {
            requestBuilder.header("Cookie", cookie);
        }

        Request request = requestBuilder.build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.err.println("请求出错: " + response.code() + " - " + response.message());
                return null;
            }
            ResponseBody body = response.body();
            if (body != null) {
                return body.bytes(); // 直接返回原始字节
            }
        } catch (IOException e) {
            System.out.println("发生网络异常!");
            e.printStackTrace();
        }
        return null;
    }

    
    
    

    /**
     * post请求
     * 
     * @param url
     * @param json
     * @return
     */
    public static JSONObject doPost(String url, JSONObject json) {
        DefaultHttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        JSONObject response = null;
        try {
            StringEntity s = new StringEntity(json.toString());
            s.setContentEncoding("UTF-8");
            s.setContentType("application/json;charset=UTF-8");// 发送json数据需要设置contentType
            post.setEntity(s);
            HttpResponse res = client.execute(post);
            if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String result = EntityUtils.toString(res.getEntity());// 返回json格式：
                result = new String(result.getBytes("ISO-8859-1"), "utf-8");
                response = JSONObject.parseObject(result);
            }
        } catch (Exception e) {
            client.close();
        }
        return response;
    }

    public static JSONObject doPostNew(String url, JSONObject json) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);
        post.addHeader("Content-Type", "application/json");
        JSONObject response = null;
        try {
            post.setEntity(new StringEntity(json.toString(), "UTF-8"));
            HttpResponse res = httpClient.execute(post);
            if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String result = EntityUtils.toString(res.getEntity());
                result = new String(result.getBytes("ISO-8859-1"), "utf-8");
                response = JSONObject.parseObject(result);
            }
        } catch (Exception e) {

        } finally {
            try {
                httpClient.close();
            } catch (Exception e2) {
            }
        }
        return response;
    }

    public static String downBiliFromUrl(String urlStr, String fileName, String savePath) throws Exception {
        return downBiliFromUrl(urlStr, fileName, savePath, null);
    }

    public static String downBiliFromUrl(String urlStr, String fileName, String savePath, String cookie) throws Exception {
        if (urlStr == null || urlStr.isEmpty() || fileName == null || fileName.isEmpty()
                || savePath == null || savePath.isEmpty()) {
            throw new IllegalArgumentException("urlStr, fileName, savePath 不能为空");
        }

        int maxRetries = 3;
        int retryCount = 0;
        long retryDelay = 5000;

        while (retryCount < maxRetries) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .build();

            File saveDir = new File(savePath);
            File finalFile = new File(saveDir, fileName);
            File tmpFile = new File(saveDir, fileName + ".downloading");
            long downloaded = 0;
            boolean needRetry = false;

            try {
                if (!saveDir.exists()) {
                    saveDir.mkdirs();
                }

                Request.Builder requestBuilder = new Request.Builder()
                        .url(urlStr)
                        .addHeader("User-Agent", Global.configInfo.BiliDroid.getValue())
                        .addHeader("referer", "https://www.bilibili.com");

                if (cookie != null && !cookie.isEmpty()) {
                    requestBuilder.addHeader("Cookie", cookie);
                }

                Request request = requestBuilder.build();

                try (Response response = client.newCall(request).execute()) {
                    if (!response.isSuccessful()) {
                        logger.info("下载失败: " + response.code());
                        logger.info(urlStr);
                        logger.info(fileName);
                        return "1";
                    }

                    long fileLength = response.body().contentLength();
                    if (finalFile.exists() && fileLength > 0 && finalFile.length() == fileLength) {
                        logger.info("文件已存在且大小一致，跳过: {}", fileName);
                        return "0";
                    }

                    try (BufferedInputStream bis = new BufferedInputStream(response.body().byteStream());
                         BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(tmpFile))) {

                        byte[] buffer = new byte[32 * 1024];
                        int len;
                        long startTime = System.currentTimeMillis();
                        long lastLogTime = startTime;

                        while ((len = bis.read(buffer)) != -1) {
                            bos.write(buffer, 0, len);
                            downloaded += len;

                            long now = System.currentTimeMillis();
                            if (fileLength > 0 && now - lastLogTime >= 1000) {
                                int progress = (int) (downloaded * 100 / fileLength);
                                if (progress % 10 == 0) {
                                    double speed = (downloaded / 1024.0) / ((now - startTime) / 1000.0);
                                    if (logger.isDebugEnabled()) {
                                        logger.debug("下载进度: {}%, 速度: {:.2f} KB/s, 文件: {}", progress, speed, fileName);
                                    }
                                }
                                lastLogTime = now;
                            }
                        }
                        bos.flush();
                    }

                    // 文件大小校验
                    if (fileLength > 0 && tmpFile.length() != fileLength) {
                        logger.info("文件大小不匹配，下载失败");
                        return "1";
                    }

                }

                // 下载成功后尝试重命名
                Files.move(tmpFile.toPath(), finalFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                logger.info("文件下载完成: {}", finalFile.getAbsolutePath());
                return "0";

            } catch (SocketTimeoutException e) {
                logger.warn("下载超时: 第{}次: {}", retryCount + 1, fileName);
                needRetry = true;
            } catch (IOException e) {
                logger.error("下载异常: {}", e.getMessage(), e);
                return "1";
            } finally {
                if (needRetry && tmpFile.exists()) {
                    tmpFile.delete();
                }
            }

            retryCount++;
            if (retryCount < maxRetries) {
                Thread.sleep(retryDelay);
            }
        }

        logger.error("下载失败，重试多次: {}", fileName);
        return "1";
    }


    @SuppressWarnings("unused")
    private static class InputStreamWithProgress extends FilterInputStream {
        private long totalBytes;
        private long bytesRead;

        protected InputStreamWithProgress(InputStream in, long totalBytes) {
            super(in);
            this.totalBytes = totalBytes;
            this.bytesRead = 0;
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            int bytesRead = super.read(b, off, len);
            if (bytesRead != -1) {
                this.bytesRead += bytesRead;
            }
            return bytesRead;
        }

        public int getProgress() {
            if (totalBytes > 0) {
                return (int) ((bytesRead * 100) / totalBytes);
            } else {
                return 0;
            }
        }
    }

    public static void downDouFromUrl(String urlStr, String fileName, String savePath, String cookie) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5 * 1000);
            conn.setRequestProperty("User-Agent", DouUtil.ua);
            if (cookie != null) {
                conn.setRequestProperty("cookie", cookie);
            }
            InputStream input = conn.getInputStream();
            byte[] getData = readInputStream(input);
            File saveDir = new File(savePath);
            if (!saveDir.exists()) {
                FileUtils.createDirectory(savePath);
            }
            File file = new File(saveDir + File.separator + fileName);
            FileOutputStream output = new FileOutputStream(file);
            output.write(getData);
            if (output != null) {
                output.close();
            }
            if (input != null) {
                input.close();
            }
        } catch (Exception e) {

        }
    }



    public static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[10240];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }

    public static void downloadFile(String urlStr, String fileName, String savePath, Map<String, String> headers)
            throws IOException {
        if (urlStr == null || urlStr.isEmpty() || fileName == null || fileName.isEmpty() ||
                savePath == null || savePath.isEmpty()) {
            throw new IllegalArgumentException("urlStr, fileName, savePath 不能为空");
        }
        int maxRetries = 3;
        int retryCount = 0;
        long retryDelay = 5000;
        while (retryCount < maxRetries) {
            HttpURLConnection conn = null;
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;
            File saveDir = new File(savePath);
            File file = new File(saveDir, fileName);
            long downloaded = 0;
            long lastReadTime = System.currentTimeMillis(); // ✅ 初始化放到循环前
            boolean needRetry = false;
            try {
                if (!saveDir.exists()) {
                    saveDir.mkdirs();
                }
                URL url = new URL(urlStr);
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(20000);
                conn.setReadTimeout(60000);
                conn.setUseCaches(false);
                conn.setDoInput(true);
                conn.setDoOutput(false);
                conn.setRequestProperty("Connection", "keep-alive");
                conn.setRequestProperty("Keep-Alive", "300");
                if (headers != null) {
                    for (Map.Entry<String, String> entry : headers.entrySet()) {
                        conn.setRequestProperty(entry.getKey(), entry.getValue());
                    }
                }
                int fileLength = conn.getContentLength(); // ✅ 可能为 -1
                if (file.exists() && fileLength > 0 && file.length() == fileLength) {
                    logger.info("文件已存在且大小相同,跳过下载: {}", fileName);
                    return;
                }
                if (fileLength <= 0) {
                    logger.warn("无法获取远程文件大小: {}", fileName);
                }
                bis = new BufferedInputStream(conn.getInputStream());
                bos = new BufferedOutputStream(new FileOutputStream(file));
                byte[] buffer = new byte[16 * 1024];
                int len;
                long startTime = System.currentTimeMillis();
                long lastProgressTime = startTime;
                while ((len = bis.read(buffer)) != -1) {
                    bos.write(buffer, 0, len);
                    downloaded += len;
                    long currentTime = System.currentTimeMillis();
                    lastReadTime = currentTime;
                    if (fileLength > 0 && currentTime - lastProgressTime >= 15000) {
                        int progress = (int) (downloaded * 100 / fileLength);
                        double speed = (downloaded / 1024.0) / ((currentTime - startTime) / 1000.0);
                        long remainingTime = (long) ((fileLength - downloaded) / (speed * 1024));
                        logger.info("下载进度: {}%, 速度: {:.2f} KB/s, 剩余时间: {} 秒, 文件: {}",
                                progress, speed, remainingTime, fileName);
                        lastProgressTime = currentTime;
                    }
                    if (currentTime - lastReadTime > 30000) {
                        throw new SocketTimeoutException("读取超时");
                    }
                }
                bos.flush();
                if (fileLength > 0 && file.length() != fileLength) {
                    throw new IOException("文件下载不完整");
                }
                logger.info("文件下载完成: {}", fileName);
                return;
            } catch (SocketTimeoutException e) {
                logger.warn("下载超时(第 {} 次重试): {}", retryCount + 1, fileName);
                needRetry = true;
            } catch (IOException e) {
                logger.error("下载出错: {}", e.getMessage(), e);
                throw e; // 非超时直接抛出
            } finally {
                try {
                    if (bos != null)
                        bos.close();
                    if (bis != null)
                        bis.close();
                    if (conn != null)
                        conn.disconnect();
                } catch (IOException e) {
                    logger.error("关闭资源时出错: {}", e.getMessage(), e);
                }
                // ✅ 放到 finally 中统一判断是否删除不完整文件
                if (needRetry && file.exists()) {
                    file.delete();
                }
            }
            retryCount++;
            if (retryCount < maxRetries) {
                try {
                    Thread.sleep(retryDelay);
                } catch (InterruptedException ignored) {
                }
            }
        }
        throw new IOException("下载失败，已重试多次: " + fileName);
    }

    public static String downloadFileWithOkHttp(String urlStr, String fileName, String savePath, Map<String, String> headers) throws IOException {
        if (urlStr == null || urlStr.isEmpty() || fileName == null || fileName.isEmpty() || savePath == null || savePath.isEmpty()) {
            logger.info("----------------打印调试参数-------------------");
            logger.info(urlStr);
            logger.info(fileName);
            logger.info("----------------打印调试参数-------------------");
            throw new IllegalArgumentException("urlStr, fileName, savePath 不能为空");
        }

        int maxRetries = 3;
        int retryCount = 0;
        long retryDelay = 5000;

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();

        while (retryCount < maxRetries) {
            File saveDir = new File(savePath);
            File finalFile = new File(saveDir, fileName);
            File tmpFile = new File(saveDir, fileName + ".downloading");

            long downloaded = 0;
            boolean needRetry = false;

            try {
                if (!saveDir.exists()) {
                    saveDir.mkdirs();
                }

                Request.Builder requestBuilder = new Request.Builder()
                        .url(urlStr)
                        .addHeader("Connection", "keep-alive");

                if (headers != null) {
                    for (Map.Entry<String, String> entry : headers.entrySet()) {
                        requestBuilder.addHeader(entry.getKey(), entry.getValue());
                    }
                }

                try (Response response = client.newCall(requestBuilder.build()).execute()) {
                    if (!response.isSuccessful()) {
                        logger.info("下载失败: " + response.code());
                        logger.info("----------------打印调试参数-------------------");
                        logger.info(urlStr);
                        logger.info(fileName);
                        logger.info("----------------打印调试参数-------------------");
                        return "1";
                    }

                    long fileLength = response.body().contentLength();
                    if (finalFile.exists() && fileLength > 0 && finalFile.length() == fileLength) {
                        logger.info("文件已存在且大小相同,跳过下载: {}", fileName);
                        return "0";
                    }

                    long startTime = System.currentTimeMillis();
                    long lastProgressTime = startTime;
                    long lastBytesRead = 0;

                    boolean downloadSuccess = false;

                    try (BufferedInputStream bis = new BufferedInputStream(response.body().byteStream());
                         BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(tmpFile))) {

                        byte[] buffer = new byte[16 * 1024];
                        int len;

                        while ((len = bis.read(buffer)) != -1) {
                            bos.write(buffer, 0, len);
                            downloaded += len;

                            long currentTime = System.currentTimeMillis();

                            if (fileLength > 0 && currentTime - lastProgressTime >= 15000) {
                                int progress = (int) (downloaded * 100 / fileLength);
                                double averageSpeed = (downloaded / 1024.0) / Math.max(1, (currentTime - startTime) / 1000.0);
                                double instantSpeed = ((downloaded - lastBytesRead) / 1024.0) / Math.max(1, (currentTime - lastProgressTime) / 1000.0);
                                long remainingBytes = fileLength - downloaded;
                                long remainingTime = averageSpeed > 0 ? (long) (remainingBytes / (averageSpeed * 1024)) : 0;

                                logger.info("下载进度: {}%, 平均速度: {} KB/s, 实时速度: {} KB/s, 剩余时间: {} 秒, 文件: {}",
                                        progress,
                                        String.format("%.2f", averageSpeed),
                                        String.format("%.2f", instantSpeed),
                                        remainingTime,
                                        fileName);

                                lastProgressTime = currentTime;
                                lastBytesRead = downloaded;
                            }
                        }

                        bos.flush();
                        if (fileLength > 0 && tmpFile.length() != fileLength) {
                            logger.info("文件下载不完整");
                            return "1";
                        }

                        downloadSuccess = true;
                    }

                    // ⬇️ 下载成功，关闭流之后执行重命名
                    if (downloadSuccess) {
                    	Files.move(tmpFile.toPath(), finalFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        logger.info("文件下载完成: {}", fileName);
                        return "0";
                    }

                }
            } catch (SocketTimeoutException e) {
                logger.warn("下载超时(第 {} 次重试): {}", retryCount + 1, fileName);
                needRetry = true;
            } catch (IOException  e) {
                logger.error("下载出错: {}", e.getMessage(), e);
                logger.info("----------------打印调试参数-------------------");
                logger.info(urlStr);
                logger.info(fileName);
                logger.info("----------------打印调试参数-------------------");
                return "1";
            } finally {
                if (needRetry && tmpFile.exists()) {
                    tmpFile.delete();
                }
            }

            retryCount++;
            if (retryCount < maxRetries) {
                try {
                    Thread.sleep(retryDelay);
                } catch (InterruptedException ignored) {
                }
            }
        }

        logger.info("----------------打印调试参数-------------------");
        logger.info(urlStr);
        logger.info(fileName);
        logger.info("----------------打印调试参数-------------------");
        logger.error("下载失败，已重试多次: " + fileName);
        return "1";
    }


    public static String downloadFileWithOkHttp(String urlStr, String fileName, String savePath, Map<String, String> headers, int threadCount)
            throws IOException, InterruptedException {
        if (urlStr == null || urlStr.isEmpty() || fileName == null || fileName.isEmpty() || savePath == null || savePath.isEmpty()) {
            logger.info("----------------打印调试参数-------------------");
            logger.info(urlStr);
            logger.info(fileName);
            logger.info("----------------打印调试参数-------------------");
            throw new IllegalArgumentException("urlStr, fileName, savePath 不能为空");
        }

        OkHttpClient client = new OkHttpClient.Builder()
                .protocols(Collections.singletonList(Protocol.HTTP_1_1))
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();

        File saveDir = new File(savePath);
        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }

        File tmpFile = new File(saveDir, fileName + ".tmp");
        File finalFile = new File(saveDir, fileName);

        // 探测文件大小（请求0-0字节）
        Request.Builder rangeBuilder = new Request.Builder()
                .url(urlStr)
                .addHeader("Range", "bytes=0-0")
                .addHeader("Connection", "keep-alive");
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                rangeBuilder.addHeader(entry.getKey(), entry.getValue());
            }
        }

        Response rangeResponse = client.newCall(rangeBuilder.build()).execute();
        if (!rangeResponse.isSuccessful()) {
            logger.error("探测文件大小失败: {}", rangeResponse.code());
            return "1";
        }

        String contentRange = rangeResponse.header("Content-Range"); // 例如 "bytes 0-0/123456"
        rangeResponse.close();

        long totalLength = -1;
        if (contentRange != null && contentRange.contains("/")) {
            try {
                totalLength = Long.parseLong(contentRange.split("/")[1]);
            } catch (Exception e) {
                logger.error("无法解析 Content-Range: {}", contentRange);
            }
        }

        if (totalLength <= 0) {
            logger.error("无法获取文件长度，可能不支持分片下载");
            return "1";
        }

        logger.info("开始多线程下载: {}, 大小: {} 字节, 分片数: {}", fileName, totalLength, threadCount);

        // 预分配文件空间
        try (RandomAccessFile raf = new RandomAccessFile(tmpFile, "rw")) {
            raf.setLength(totalLength);
        }

        long partSize = totalLength / threadCount;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        AtomicLong[] downloadedBytes = new AtomicLong[threadCount];
        boolean[] finished = new boolean[threadCount]; // 标记分片是否下载完成
        for (int i = 0; i < threadCount; i++) {
            downloadedBytes[i] = new AtomicLong(0);
            finished[i] = false;
        }

        ScheduledExecutorService progressExecutor = Executors.newSingleThreadScheduledExecutor();
        final long[] lastBytes = new long[threadCount];

        final long finalTotalLength = totalLength;
        final long finalPartSize = partSize;

        // 定时打印所有分片进度（15秒一次）
        progressExecutor.scheduleAtFixedRate(() -> {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < threadCount; i++) {
                if (finished[i]) {
                    sb.append(String.format("分片 %d 已完成; ", i));
                    continue;
                }

                long current = downloadedBytes[i].get();
                long thisPartSize = (i == threadCount - 1) ? (finalTotalLength - finalPartSize * i) : finalPartSize;

                // 限制当前已下载不超过分片大小，避免进度超过100%
                if (current > thisPartSize) {
                    current = thisPartSize;
                    downloadedBytes[i].set(thisPartSize);
                }

                long diff = current - lastBytes[i];
                lastBytes[i] = current;

                double speedKBs = diff / 1024.0 / 15.0;
                double progressPercent = Math.min((current * 100.0) / thisPartSize, 100.0);

                sb.append(String.format("分片 %d 进度: %.2f%%, 速度: %.2f KB/s; ", i, progressPercent, speedKBs));
            }
            logger.info(sb.toString());
        }, 15, 15, TimeUnit.SECONDS);

        // 启动分片下载任务
        for (int i = 0; i < threadCount; i++) {
            final int part = i;
            final long start = part * partSize;
            final long end = (part == threadCount - 1) ? totalLength - 1 : (start + partSize - 1);

            executor.execute(() -> {
                try {
                    Thread.sleep(part * 2500L); // 延迟启动，防止同时请求过多
                } catch (InterruptedException e) {
                    logger.warn("分片 {} 启动延迟被中断", part);
                }

                final int maxRetry = 5;
                int retry = 0;
                boolean success = false;

                while (retry < maxRetry && !success) {
                    try {
                        Request.Builder requestBuilder = new Request.Builder()
                                .url(urlStr)
                                .addHeader("Range", "bytes=" + start + "-" + end)
                                .addHeader("Connection", "keep-alive");

                        if (headers != null) {
                            for (Map.Entry<String, String> entry : headers.entrySet()) {
                                requestBuilder.addHeader(entry.getKey(), entry.getValue());
                            }
                        }

                        try (Response response = client.newCall(requestBuilder.build()).execute()) {
                            if (!response.isSuccessful()) {
                                logger.error("5秒后重试当前分片,分片下载失败: part {} code {}", part, response.code());
                                retry++;
                                Thread.sleep(5000);
                                continue;
                            }

                            try (BufferedInputStream bis = new BufferedInputStream(response.body().byteStream());
                                 RandomAccessFile raf = new RandomAccessFile(tmpFile, "rw")) {
                                raf.seek(start);
                                byte[] buffer = new byte[16 * 1024];
                                int len;
                                while ((len = bis.read(buffer)) != -1) {
                                    raf.write(buffer, 0, len);
                                    downloadedBytes[part].addAndGet(len);
                                }
                            }
                            success = true;
                            finished[part] = true; // 标记该分片下载完成
                            logger.info("分片 {} 下载完成 [{} - {}]", part, start, end);
                        }
                    } catch (IOException | InterruptedException e) {
                        logger.error("分片 {} 下载异常: {}", part, e.getMessage());
                        retry++;
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ex) {
                            logger.warn("分片 {} 重试等待被中断", part);
                        }
                    }
                }

                if (!success) {
                    logger.error("分片 {} 下载最终失败", part);
                }
                latch.countDown();
            });
        }

        latch.await();
        executor.shutdown();
        progressExecutor.shutdown();

        // 校验文件大小
        if (!tmpFile.exists() || tmpFile.length() != totalLength) {
            logger.error("文件长度校验失败，预期: {}, 实际: {}", totalLength, tmpFile.length());
            return "1";
        }

        // 重命名临时文件为最终文件
        if (finalFile.exists()) {
            finalFile.delete();
        }
        if (!tmpFile.renameTo(finalFile)) {
            logger.error("重命名文件失败: {} -> {}", tmpFile.getName(), finalFile.getName());
            return "1";
        }

        logger.info("文件下载完成: {}", finalFile.getName());
        return "0";
    }

}
