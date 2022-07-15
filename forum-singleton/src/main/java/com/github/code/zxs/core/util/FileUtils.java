package com.github.code.zxs.core.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import com.github.code.zxs.core.model.enums.FileResponseTypeEnum;
import com.github.code.zxs.storage.model.enums.ContentTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.ClientAbortException;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;

@Slf4j
public class FileUtils extends FileUtil {
    public static final String[] UNITS = new String[]{"B", "KB", "MB", "GB", "TB", "PB"};

    /**
     * 文件下载，单线程，直接传
     *
     * @param file     文件对象
     * @param fileName 要保存为的文件名
     * @return 文件下载对象
     */
    public static ResponseEntity<Object> exportSingleThread(File file, String fileName) {
        if (!file.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("404 FILE NOT FOUND");
        }

        MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");

        if (StringUtils.isEmpty(fileName)) {
            fileName = file.getName();
        }

        headers.setContentDispositionFormData("attachment", URLUtil.encode(fileName));

        headers.add(HttpHeaders.PRAGMA, "no-cache");
        headers.add(HttpHeaders.EXPIRES, "0");
        headers.add(HttpHeaders.LAST_MODIFIED, new Date().toString());
        headers.add(HttpHeaders.ETAG, String.valueOf(System.currentTimeMillis()));
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(mediaType)
                .body(new FileSystemResource(file));
    }

    /**
     * 返回文件给 response，支持断点续传和多线程下载
     *
     * @param request  请求对象
     * @param response 响应对象
     * @param file     下载的文件
     */
    public static void export(HttpServletRequest request, HttpServletResponse response, File file, FileResponseTypeEnum type) {
        export(request, response, file, file.getName(), type);
    }

    /**
     * 返回文件给 response，支持断点续传和多线程下载 (动态变化的文件不支持)
     *
     * @param request  请求对象
     * @param response 响应对象
     * @param file     下载的文件
     * @param fileName 下载的文件名，为空则默认读取文件名称
     */
    public static void export(HttpServletRequest request, HttpServletResponse response, File file, String fileName, FileResponseTypeEnum type) {
        if (!file.exists()) {
            try {
                response.getWriter().write("404 FILE NOT FOUND");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (StringUtils.isEmpty(fileName)) {
            //文件名
            fileName = file.getName();
        }

        String range = request.getHeader(HttpHeaders.RANGE);

        String rangeSeparator = "-";
        // 开始下载位置
        long startByte = 0;
        // 结束下载位置
        long endByte = file.length() - 1;

        // 如果是断点续传
        if (range != null && range.contains("bytes=") && range.contains(rangeSeparator)) {
            // 设置响应状态码为 206
            response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);

            range = range.substring(range.lastIndexOf("=") + 1).trim();
            String[] ranges = range.split(rangeSeparator);
            try {
                // 判断 range 的类型
                if (ranges.length == 1) {
                    // 类型一：bytes=-2343
                    if (range.startsWith(rangeSeparator)) {
                        endByte = Long.parseLong(ranges[0]);
                    }
                    // 类型二：bytes=2343-
                    else if (range.endsWith(rangeSeparator)) {
                        startByte = Long.parseLong(ranges[0]);
                    }
                }
                // 类型三：bytes=22-2343
                else if (ranges.length == 2) {
                    startByte = Long.parseLong(ranges[0]);
                    endByte = Long.parseLong(ranges[1]);
                }
            } catch (NumberFormatException e) {
                // 传参不规范，则直接返回所有内容
                startByte = 0;
                endByte = file.length() - 1;
            }
        } else {
            // 没有 ranges 即全部一次性传输，需要用 200 状态码，这一行应该可以省掉，因为默认返回是 200 状态码
            response.setStatus(HttpServletResponse.SC_OK);
        }

        //要下载的长度（endByte 为总长度 -1，这时候要加回去）
        long contentLength = endByte - startByte + 1;
        //文件类型
        String contentType = request.getServletContext().getMimeType(fileName);

        if (type == FileResponseTypeEnum.DOWNLOAD || StrUtil.isEmpty(contentType)) {
            contentType = "attachment";
        }

        response.setHeader(HttpHeaders.ACCEPT_RANGES, "bytes");
        response.setHeader(HttpHeaders.CONTENT_TYPE, contentType);
        // 这里文件名换你想要的，inline 表示浏览器可以直接使用
        // 参考资料：https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Headers/Content-Disposition
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, contentType + ";filename=" + URLUtil.encode(fileName));
        response.setHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(contentLength));
        // [要下载的开始位置]-[结束位置]/[文件总大小]
        response.setHeader(HttpHeaders.CONTENT_RANGE, "bytes " + startByte + rangeSeparator + endByte + "/" + file.length());

        BufferedOutputStream outputStream;
        RandomAccessFile randomAccessFile = null;
        //已传送数据大小
        long transmitted = 0;
        try {
            randomAccessFile = new RandomAccessFile(file, "r");
            outputStream = new BufferedOutputStream(response.getOutputStream());
            byte[] buff = new byte[4096];
            int len = 0;
            randomAccessFile.seek(startByte);
            while ((transmitted + len) <= contentLength && (len = randomAccessFile.read(buff)) != -1) {
                outputStream.write(buff, 0, len);
                transmitted += len;
                // 本地测试, 防止下载速度过快
                // Thread.sleep(1);
            }
            // 处理不足 buff.length 部分
            if (transmitted < contentLength) {
                len = randomAccessFile.read(buff, 0, (int) (contentLength - transmitted));
                outputStream.write(buff, 0, len);
                transmitted += len;
            }

            outputStream.flush();
            response.flushBuffer();
            randomAccessFile.close();
            // log.trace("下载完毕: {}-{}, 已传输 {}", startByte, endByte, transmitted);
        } catch (ClientAbortException e) {
            // ignore 用户停止下载
            // log.trace("用户停止下载: {}-{}, 已传输 {}", startByte, endByte, transmitted);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (randomAccessFile != null) {
                    randomAccessFile.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getSuffix(String filename) {
        if (filename == null)
            return "";
        int index = filename.lastIndexOf('.');
        return index == -1 ? "" : filename.substring(index);
    }

    public static String hash(File file) throws IOException {
        //DigestUtils.md5Hex(in)的缓存区大小为1024，使用BufferedInputStream
        try (InputStream in = new BufferedInputStream(new FileInputStream(file))) {
            return DigestUtils.md5Hex(in);
        }
    }

    public static String hash(MultipartFile multipartFile) throws IOException {
        //大部分情况下为ByteArrayInputStream
        try (InputStream in = new BufferedInputStream(multipartFile.getInputStream())) {
            return DigestUtils.md5Hex(in);
        }
    }

    /**
     * 创建硬链接
     *
     * @param target 链接目标
     * @param link   链接
     * @throws IOException
     */
    public static void createLink(String link, String target) throws IOException {
        createLink(getPath(link), getPath(target));
    }

    public static void createLink(File link, File target) throws IOException {
        createLink(link.toPath(), target.toPath());
    }

    public static void createLink(Path link, Path target) throws IOException {
        Files.createLink(link, target);
    }

    public static Path getPath(String path) {
        return new File(path).toPath();
    }


    /**
     * 通过文件路径获取文件类型
     *
     * @param path
     * @return ContentType - 文件类型与对应的文件魔数枚举类
     */
    public static ContentTypeEnum getContentType(String path) {
        // 获取文件头
        try (InputStream inputStream = new FileInputStream(path)) {
            return getContentType(inputStream);
        } catch (IOException e) {
            log.error("获取文件类型出错", e);
        }
        return ContentTypeEnum.NOT_EXITS_ENUM;
    }

    /**
     * 获取文件类型
     *
     * @param multipartFile
     * @return FileTypeEnum - 文件类型与对应的文件魔数枚举类
     */
    public static ContentTypeEnum getContentType(MultipartFile multipartFile) {
        // 获取文件头
        try (InputStream inputStream = multipartFile.getInputStream()) {
            return getContentType(inputStream);
        } catch (IOException e) {
            log.error("获取文件类型出错", e);
            return ContentTypeEnum.NOT_EXITS_ENUM;
        }
    }

    /**
     * 通过文件流获取文件类型
     *
     * @param inputStream
     * @return ContentType - 文件类型与对应的文件魔数枚举类
     */
    public static ContentTypeEnum getContentType(InputStream inputStream) {
        // 获取文件头
        String magicNumberCode = getFileHeaderHexByInputStream(inputStream);
        if (StringUtils.isNotBlank(magicNumberCode)) {
            return ContentTypeEnum.getByMagicNumberCode(magicNumberCode.toUpperCase());
        }
        return ContentTypeEnum.NOT_EXITS_ENUM;
    }

    /**
     * 获取文件头（即文件魔数），根据通过文件流。文件流需要自己关闭
     *
     * @param inputStream 输入流
     * @return fileHeaderHex - 文件头，即文件魔数
     */
    private static String getFileHeaderHexByInputStream(InputStream inputStream) {
        byte[] b = new byte[28];
        try {
            inputStream.read(b, 0, 28);
        } catch (IOException e) {
            log.error("获取文件头失败", e);
            return null;
        }
        return bytesToHexString(b);
    }

    /**
     * 将文件二进制流（即字节数组）转换成16进制字符串数据
     *
     * @param b
     * @return fileHeaderHex - 文件头，即文件魔数
     */
    private static String bytesToHexString(byte[] b) {
        StringBuilder stringBuilder = new StringBuilder();
        if (b == null || b.length <= 0) {
            return null;
        }

        for (byte value : b) {
            int v = value & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public static void copyFileIfAbsent(InputStream in, File file) throws IOException {
        if (!file.exists())
            Files.copy(in, file.toPath());
    }

    /**
     * 文件大小（单位字节）转换为友好值
     *
     * @param byteSize
     * @return
     */
    public static String prettySize(long byteSize) {
        int index = 0;
        double size = byteSize;
        while (size >= 1024) {
            index++;
            size /= 1024;
        }
        return size + UNITS[index];
    }
}

