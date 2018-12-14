package io.jonah;

import java.io.File;
import java.io.InputStream;

import org.junit.Test;

import cn.hutool.core.io.FileUtil;

public class TestEasyClient {

	
	
	@Test
	public void testUpload(){
		DefaultEasydfsClientImpl client = DefaultEasydfsClientImpl.create("http://127.0.0.1:8001/easydfs/");
		System.out.println(client.upload("test", new File("D:/face.jpg")));
	}
	
	
	@Test
	public void testDownload(){
		DefaultEasydfsClientImpl client = DefaultEasydfsClientImpl.create("http://127.0.0.1:8001/easydfs/");
		InputStream in = client.download("test", "5c136295a3b247ef8132e820.jpg");
		FileUtil.writeFromStream(in, new File("D:/test2.jpg"));
	}
	
	
	
	
}
