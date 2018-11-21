package com.fxb.Springbootdemo;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import modle.Student;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import util.JSONWriter;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@SpringBootApplication
public class SpringBootDemoApplication {

	private static final String account = "N00000001104";//替换为您的账户
	private static final String secret = "ae40e076-08ce-434e-a9c5-3d1350caaa58";//替换为您的api密码
	private static final String host = "http://apis.7moor.com";

	public static void main(String[] args) {
		//SpringApplication.run(SpringBootDemoApplication.class, args);
		/*String uploadUrl="http://14.29.16.121:4000/paas/zongji/uploadVoice/N00000001104";
		String time = getDateTime();
		String sig = md5(account + secret + time);
		uploadUrl += "?sig=" + sig;
		String auth = base64(account + ":" + time);
		String result="";
		System.out.println("sig=" + sig);
		System.out.println("auth=" + auth);*/
//		Student s1 = new Student("123","123");
//		Student s2 = new Student("123","123");
//		ArrayList<String> list = new ArrayList<String>();
//		list.add("123");
//		list.add("456");
//		//System.out.println(list);
//		JSONWriter jsonWriter = new JSONWriter();
//		String a = jsonWriter.write(list);
//		//System.out.println(a);
//
//		ObjectMapper mapper = new ObjectMapper();
//        try {
//            String json = mapper.writeValueAsString(s1);
//            System.out.println(json);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
        List<String> midNumList = new ArrayList<String>();
        for (int i = 0; i < 20; i++){
           midNumList.add(String.valueOf(i));
        }
        for (int i = 0; i < 50; i++){
            System.out.println("*****" + 0.26072517929158634 * 20);
            System.out.println("*****" + Math.random());
            System.out.println("-----" + Math.random() * ( 10));
            int num = (int) Math.round(Math.random()*19);
            System.out.println(midNumList.get(num));
        }


        try {
			//String sTestsetFile=System.getProperty("user.dir")+File.separator+"testdata"+File.separator+"solr_etl_agent35.json";

			/*CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpPost uploadFile = new HttpPost(uploadUrl);
			uploadFile.addHeader("Authorization",auth);
			uploadFile.addHeader("Charset", "UTF-8");

			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			builder.addTextBody("PBX", "gd.zj.16.0", ContentType.TEXT_PLAIN);

			// 把文件加到HTTP的post请求中
			File f = new File("/Users/fxb/Desktop/测试语音.wav");
			builder.addBinaryBody(
					"File",
					new FileInputStream(f),
					ContentType.APPLICATION_OCTET_STREAM,
					f.getName()
			);

			HttpEntity multipart = builder.build();
			uploadFile.setEntity(multipart);
			CloseableHttpResponse response = httpClient.execute(uploadFile);
			HttpEntity responseEntity = response.getEntity();
			String sResponse=EntityUtils.toString(responseEntity, "UTF-8");
			System.out.println("Post 返回结果"+sResponse);*/

			/*MultipartFile file = new MockMultipartFile("测试语音.wav", new FileInputStream(new File("/Users/fxb/Desktop/测试语音.wav")));
			// 换行符
			final String newLine = "\r\n";
			final String boundaryPrefix = "--";
			// 定义数据分隔线
			String BOUNDARY = "------WebKitFormBoundaryFsag3EBHB3PbWbwF";
			// 服务器的域名
			URL url = new URL(uploadUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			// 设置为POST情
			conn.setRequestMethod("POST");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			// 设置请求头参数
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("Charset", "UTF-8");
			conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
			conn.setRequestProperty("Authorization",auth);

			OutputStream out = new DataOutputStream(conn.getOutputStream());

			// 上传文件
			StringBuilder sb = new StringBuilder();
			sb.append(boundaryPrefix);
			sb.append(BOUNDARY);
			sb.append(newLine);
			// 文件参数,file参数名可以随意修改
			sb.append("Content-Disposition: form-data;name=\"File\";filename=\"" + file.getName()
					+ "\"" + newLine);
			sb.append("Content-Type:audio/wave");
			// 参数头设置完以后需要两个换行，然后才是参数内容
			sb.append(newLine);
			sb.append(newLine);
			// 将参数头的数据写入到输出流中
			out.write(sb.toString().getBytes());

			// 数据输入流,用于读取文件数据
			DataInputStream in = new DataInputStream(file.getInputStream());
			byte[] bufferOut = new byte[1024*8];
			int bytes = 0;
			// 每次读8KB数据,并且将文件数据写入到输出流中
			while ((bytes = in.read(bufferOut)) != -1) {
				out.write(bufferOut, 0, bytes);
			}
			// 最后添加换行
			out.write(newLine.getBytes());
			in.close();

			//其他参数
			StringBuilder params = new StringBuilder();
			params.append(BOUNDARY);
			params.append(newLine);
			params.append("Content-Disposition: form-data; name=\"PBX\""+ newLine);
			params.append(newLine);
			params.append("gd.zj.16.0");
			out.write(params.toString().getBytes());

			// 定义最后数据分隔线，即--加上BOUNDARY再加上--。
			byte[] end_data = (newLine + boundaryPrefix + BOUNDARY + boundaryPrefix + newLine)
					.getBytes();
			// 写上结尾标识
			out.write(end_data);
			out.flush();
			out.close();

			// 定义BufferedReader输入流来读取URL的响应
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				result += line; //这里读取的是上边url对应的上传文件接口的返回值，读取出来后，然后接着返回到前端，实现接口中调用接口的方式
			}*/
		} catch (Exception e) {
			System.out.println("发送POST请求出现异常！" + e);
			e.printStackTrace();
		}
		//System.out.println(result);
	}


	public static String md5 (String text) {
		return DigestUtils.md5Hex(text).toUpperCase();
	}
	public static String base64 (String text) {
		byte[] b = text.getBytes();
		Base64 base64 = new Base64();
		b = base64.encode(b);
		String s = new String(b);
		return s;
	}
	public static String getDateTime(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(new Date());
	}
}
