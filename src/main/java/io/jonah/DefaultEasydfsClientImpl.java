package io.jonah;

import java.io.File;
import java.io.InputStream;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpException;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;

public class DefaultEasydfsClientImpl{
	
	
	private String baseUrl;

	private int timeout = 5000;
	
	
	public static DefaultEasydfsClientImpl create(String baseUrl){
		return new DefaultEasydfsClientImpl(baseUrl);
	}
	
	
	public static DefaultEasydfsClientImpl create(String baseUrl, int timeout){
		return new DefaultEasydfsClientImpl(baseUrl, timeout);
	}
	
	
	public DefaultEasydfsClientImpl(String baseUrl){
		this.baseUrl = baseUrl;
	}
	
	
	public DefaultEasydfsClientImpl(String baseUrl, int timeout){
		this.baseUrl = baseUrl;
		this.timeout = timeout;
	}

	
	
	/**
	 * 上传文件
	 * @param bucket 文件夹名称
	 * @param uploadFile
	 * @return json字符串   {code: 200, msg: "OK", data: "123453355663434655.jpg"}
	 */
	public String upload(String bucket, File uploadFile){
		Assert.notBlank(bucket, "bucket不能为空");
		Assert.notNull(uploadFile, "uploadFile不能为空");
		if(!uploadFile.exists())
			Assert.notNull(uploadFile, "uploadFile不存在");
		
		String url = buildUrl("/file/upload");
		HttpRequest request = HttpUtil.createPost(url);
		request.timeout(timeout);
		request.form("file", uploadFile);
		request.form("bucket", bucket);
		
		HttpResponse response;
		try{
			response = request.execute(true);
			int code = response.getStatus();
			if(code == 200)
				return response.body();
		}catch(HttpException e){
			return "{\"msg\":\"发起HTTP请求超时！\",\"code\":\"500\"}";
		}
		return "{\"code\": 500, \"msg\": \"请求失败\"}";
	}
	


	
	/**
	 * 下载文件
	 * @param bucket 文件夹名称
	 * @param fileName 文件名
	 * @return 文件的二进制流
	 */
	public InputStream download(String bucket, String fileName){
		Assert.notBlank(bucket, "bucket不能为空");
		Assert.notBlank(fileName, "文件名不能为空");
		
		String url = buildUrl(StrUtil.format("/file/download/{}/{}", bucket, fileName));
		HttpRequest request = HttpUtil.createGet(url);
		request.timeout(timeout);
		HttpResponse response;
		try{
			response = request.execute(true);
			int code = response.getStatus();
			if(code == 200)
				return response.bodyStream();
		}catch(HttpException e){
			return null;
		}
		return null;
	}
	
	
	
	
	/**
	 * 构建请求URL
	 * @param url
	 * @return 组装好的url
	 */
	private String buildUrl(String url){
		//该URL后面有 "/" 
		if(baseUrl.lastIndexOf("/") == (baseUrl.length() -1)){
			url = url.substring(1);
			return baseUrl.concat(url);
		}else
			return baseUrl.concat(url);
	}
	
	
}
