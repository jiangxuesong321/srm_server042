package com.cmoc.modules.oss.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cmoc.common.util.CommonUtils;
import com.cmoc.common.util.oss.OssBootUtil;
import com.cmoc.modules.oss.entity.OSSFile;
import com.cmoc.modules.oss.mapper.OSSFileMapper;
import com.cmoc.modules.oss.service.IOSSFileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @Description: OSS云存储实现类
 * @author: jeecg-boot
 */
@Service("ossFileService")
public class OSSFileServiceImpl extends ServiceImpl<OSSFileMapper, OSSFile> implements IOSSFileService {

	@Override
	public void upload(MultipartFile multipartFile) throws IOException {
		String fileName = multipartFile.getOriginalFilename();
		fileName = CommonUtils.getFileName(fileName);
		OSSFile ossFile = new OSSFile();
		ossFile.setFileName(fileName);
		String url = OssBootUtil.upload(multipartFile,"upload/test");
		//update-begin--Author:scott  Date:20201227 for：JT-361【文件预览】阿里云原生域名可以文件预览，自己映射域名kkfileview提示文件下载失败-------------------
		// 返回阿里云原生域名前缀URL
		ossFile.setUrl(OssBootUtil.getOriginalUrl(url));
		//update-end--Author:scott  Date:20201227 for：JT-361【文件预览】阿里云原生域名可以文件预览，自己映射域名kkfileview提示文件下载失败-------------------
		this.save(ossFile);
	}

	@Override
	public boolean delete(OSSFile ossFile) {
		try {
			this.removeById(ossFile.getId());
			OssBootUtil.deleteUrl(ossFile.getUrl());
		}
		catch (Exception ex) {
			return false;
		}
		return true;
	}

}