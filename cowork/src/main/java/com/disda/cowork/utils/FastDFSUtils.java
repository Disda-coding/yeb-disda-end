package com.disda.cowork.utils;

import org.csource.fastdfs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * fastDFS工具类
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
public class FastDFSUtils {
    //日志
    private static Logger logger = LoggerFactory.getLogger(FastDFSUtils.class);

    /**
     * fastDFS配置文件初始化
     * ClientGlobal.init(path);
     * 读取配置文件，并初始化对应属性
     */
    static {
        try {
            String path = new ClassPathResource("fdfs_client.conf").getFile().getAbsolutePath();
            ClientGlobal.init(path);
        } catch (Exception e) {
            logger.error("fastDFS配置文件初始化失败",e.getMessage());
        }
    }

    /**
     * 上传文件
     * @param file
     * @return
     */
    public static String[] upload(MultipartFile file){
        String filename = file.getOriginalFilename();
        logger.info("文件名："+filename);
        //后缀名
        String ext = filename.substring(filename.lastIndexOf(".") + 1);
        //storage客服端
        StorageClient storageClient = null;
        //返回结果
        String[] uploadResults = null;
        try {
            storageClient = getStorageClient();
            //上传文件
            uploadResults  = storageClient.upload_file(file.getBytes(), ext, null);
        } catch (Exception e) {
            logger.error("上传文件失败",e.getMessage());
        }
        //有storage客服端，没有返回结果，上传失败
        if (uploadResults==null){
            logger.error("上传文件失败,uploadResults空");
        }
        return uploadResults;
    }

    /**
     * 获取文件信息
     * @param groupName
     * @param remoteFileName
     * @return
     */
    private static FileInfo getFileInfo(String groupName,String remoteFileName){
        //storage客服端
        StorageClient storageClient = null;
        //获取的消息
        FileInfo fileInfo = null;
        try {
            storageClient = getStorageClient();
            fileInfo = storageClient.get_file_info(groupName, remoteFileName);
        } catch (Exception e) {
            logger.error("获取文件信息失败",e.getMessage());
        }
        if (fileInfo==null){
            logger.error("获取文件信息失败，fileInfo为空");
        }
        return fileInfo;
    }

    /**
     * 下载文件
     * @param groupName
     * @param remoteFileName
     * @return
     */
    public static InputStream downFile(String groupName,String remoteFileName){
        //storage客服端
        StorageClient storageClient = null;
        try {
            storageClient = getStorageClient();
            byte[] bytes = storageClient.download_file(groupName, remoteFileName);
            InputStream inputStream = new ByteArrayInputStream(bytes);
            return inputStream;
        } catch (Exception e) {
            logger.error("下载文件失败",e.getMessage());
        }
        return null;
    }

    /**
     * 删除文件s
     * @param groupName
     * @param remoteFileName
     */
    public static void deleteFile(String groupName,String remoteFileName){
        //storage客服端
        StorageClient storageClient = null;
        try {
            storageClient = getStorageClient();
            int i = storageClient.delete_file(groupName, remoteFileName);
            logger.info("文件删除成功:"+i);
        } catch (Exception e) {
            logger.error("文件删除失败",e.getMessage());
        }
    }

    /**
     * StorageClient
     * Storage客服端
     * @return
     * @throws IOException
     */
    private static StorageClient getStorageClient() throws IOException {
        TrackerServer trackerServer = getTrackerServer();
        StorageClient storageClient = new StorageClient(trackerServer, null);
        return storageClient;
    }

    /**
     * TrackerServer 服务器
     * @return
     * @throws IOException
     */
    private static TrackerServer getTrackerServer() throws IOException {
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();
        return trackerServer;
    }

    /**
     * 获取文件路径
     * @return
     */
    public static String getTrackerUrl(){
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = null;
        StorageServer storeStorage = null;
        try {
            trackerServer = getTrackerServer();
            storeStorage= trackerClient.getStoreStorage(trackerServer);
        } catch (Exception e) {
            logger.error("获取文件路径失败",e.getMessage());
        }
        String path = "http://"+storeStorage.getInetSocketAddress().getHostString()+":8888/";
        return path;
    }

}
