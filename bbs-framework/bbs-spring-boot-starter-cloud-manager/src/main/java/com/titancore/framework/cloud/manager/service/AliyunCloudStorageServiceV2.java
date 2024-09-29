package com.titancore.framework.cloud.manager.service;


import com.aliyun.oss.model.OSSObjectSummary;
import com.titancore.framework.cloud.manager.domain.dto.FileDelDTO;
import com.titancore.framework.cloud.manager.domain.dto.FileDownloadDTO;
import com.titancore.framework.cloud.manager.domain.entity.File;
import com.titancore.framework.cloud.manager.domain.vo.FileListVo;
import com.titancore.framework.cloud.manager.properties.CloudProperties;
import com.titancore.framework.cloud.manager.urils.AliyunOssUtilV2;
import com.titancore.framework.cloud.manager.urils.AliyunSmsUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Slf4j
public class AliyunCloudStorageServiceV2 implements CloudService {

    private final CloudProperties cloudProperties;
    public AliyunCloudStorageServiceV2(CloudProperties cloudProperties) {
        this.cloudProperties=cloudProperties;
    }
    @Value("${titan.cloud.maxSize}")
    private int maxSize;

    private AliyunSmsUtils initSms(){
        return new  AliyunSmsUtils(
                cloudProperties.getAliyun().getAsms().getRegionId(),
                cloudProperties.getAliyun().getAsms().getAccessKeyId(),
                cloudProperties.getAliyun().getAsms().getAccessKeySecret(),
                cloudProperties.getAliyun().getAsms().getTemplateCode(),
                cloudProperties.getAliyun().getAsms().getSignName()
        );
    }
    private AliyunOssUtilV2 initOss(){
        return new AliyunOssUtilV2(
                cloudProperties.getAliyun().getAoss().getEndpoint(),
                cloudProperties.getAliyun().getAoss().getAccessKeyId(),
                cloudProperties.getAliyun().getAoss().getAccessKeySecret(),
                cloudProperties.getAliyun().getAoss().getBucketName()
        );
    }


    /**
     * 发送短信
     * @param phoneNumber
     * @param verificationCode
     * @return
     */
    @Override
    public String sendMessage(String phoneNumber, String verificationCode) {
        return  initSms().sendMessage(phoneNumber, verificationCode);
    }

    @Override
    public String uploadAvatar(byte[] file, Long userId) {
        return null;
    }

    @Override
    public String uploadBackground(byte[] file, Long userId, Long articleId) {
        return null;
    }

    @Override
    public String uploadFile(byte[] file, Long userId, String objectName) {
        return null;
    }


    /**
     * 根据用户id查询用户上传的文件
     * 路径 userId/file/ 下的文件
     *
     * @param userId
     * @return
     */
    @Override
    public FileListVo queryFileListByUserId(long userId) {
        String fileNamePath = userId+"/file";
        List<OSSObjectSummary> ossObjectSummaries = initOss().queryFileListByFileNamePath(fileNamePath);
        List<File> files= new ArrayList<>();
        for (OSSObjectSummary file : ossObjectSummaries) {
            String fname=file.getKey();
            // 找到最后一个斜杠的位置
            int lastSlashIndex = fname.lastIndexOf('/');

// 使用substring截取从最后一个斜杠位置开始到字符串末尾的部分
            String result = fname.substring(lastSlashIndex + 1);
            File f = new File();
            f.setFileSize(String.valueOf(file.getSize()));
            f.setUploadTime(file.getLastModified().toString());
            f.setFileName(result);
            files.add(f);
        }
        FileListVo fileListVo = new FileListVo();
        fileListVo.setFiles(files);
        fileListVo.setUserId(String.valueOf(userId));
//        fileListVo.setUserName(user.getUserName());

        return fileListVo;
    }

    /**
     * 删除指定路径下的文件/文件夹（包含子文件/文件夹）
     *
     * @param fileDelDTO 包含要删除的路径信息的DTO对象
     * @return 删除结果
     */
    @Override
    public boolean deleteByPath(FileDelDTO fileDelDTO) {
        String path = fileDelDTO.getUserId() + "/" + fileDelDTO.getPath();
        if (fileDelDTO.getFileName() != null && !fileDelDTO.getFileName().isEmpty()) {
            // 删除单个文件
            return initOss().deleteFile(path + fileDelDTO.getFileName());
        } else {
            // 删除整个目录
            return initOss().deleteDirectory(path);
        }
    }
    /**
     * 下载指定路径下的文件
     * userId/file/xxx.zip
     *
     * @param fileDownloadDTO
     * @return 根据流返回
     */
    @Override
    public byte[] exportOssFile(FileDownloadDTO fileDownloadDTO) {
        String Path= fileDownloadDTO.getUserId()+"/file/"+ fileDownloadDTO.getFileName();
        return null;
    }

    @Override
    public Map<String, Object> exportOssFileInputStream(FileDownloadDTO fileDownloadDTO) {
//        String Path= fileDownloadDTO.getUserId()+"/file/"+ fileDownloadDTO.getFileName();
        String Path= "1/file/控件平台1.7版本安装包及说明.zip";
        return initOss().exportOssFile(Path);
    }



}