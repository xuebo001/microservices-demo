package util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.*;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.logging.Logger;

/**
 * 
 * @author Cai Zhibin
 *
 */
public class HttpProvider {
	private static Logger log = Logger.getLogger(HttpProvider.class.getName());

	//  静态方法，类名可直接调用
	public static String doPost(String url, Map<String, Object> paramsMap, String auth) {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);

		// 设置请求的header
		httpPost.addHeader("Accept", "application/json");
		httpPost.addHeader("Content-Type", "application/json;charset=utf-8");
		//httpPost.addHeader("Authorization", auth);
		JSONObject json = (JSONObject) JSON.toJSON(paramsMap);
		StringEntity entity = new StringEntity(json.toString(), "utf-8");
		entity.setContentEncoding("UTF-8");
		entity.setContentType("application/json");
		httpPost.setEntity(entity);

		// 执行请求
		HttpResponse response = null;
		try {
			response = httpClient.execute(httpPost);
			String json2 = EntityUtils.toString(response.getEntity(), "utf-8");
			return json2;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String sendPost(String url, byte[] data, int i) throws Exception{
		HttpURLConnection con = null;
		BufferedReader in = null;
		try {
			String result = "";
			URL httpUrl = new URL(url);
			con = (HttpURLConnection) httpUrl.openConnection();
			con.setRequestMethod("POST");
			con.setDoOutput(true);
			con.setConnectTimeout(10000);
			con.setReadTimeout(10000);
//			con.setRequestProperty("Content-Type", "application/xml;charset=utf-8"); 
			OutputStream out = con.getOutputStream();
			out.write(data);
			out.flush();
			out.close();
			int responseCode = con.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				in = new BufferedReader(new InputStreamReader(con
						.getInputStream()));
				String line;
				while ((line = in.readLine()) != null) {
					result += line;
				}
				return result;
			} else {

			}

		} catch (IOException e) {
			
			throw e;
		} finally {
			if (in != null) {
				try {
					if (in != null)
						in.close();
				} catch (IOException e) {
				}
			}
			
			if (con != null) {
				try{
					con.getInputStream().close();
				} catch (Throwable e){
					
				}
				try{
					con.getOutputStream().close();
				} catch (Throwable e){
					
				}
				con.disconnect();
			}
		}
		return null;
	}
	
	public static String sendGet(String url) throws Exception {
		String result = "";
		System.out.println("Call url: "+url);
		HttpURLConnection con = null;
		BufferedReader in = null;
		try {
			URL httpUrl = new URL(url);
			con = (HttpURLConnection) httpUrl.openConnection();
			con.setRequestMethod("GET");
			con.setReadTimeout(10000);
			int responseCode = con.getResponseCode();
			log.info("Get return ["+responseCode+"] from "+url);
			if (responseCode == HttpURLConnection.HTTP_OK) {
				in = new BufferedReader(new InputStreamReader(con
						.getInputStream()));
				String line;
				while ((line = in.readLine()) != null) {
					result += line;
				}
				return result;
			} else {
				
			}
		} catch (IOException e) {
			throw e;
		} finally {
			if(in != null)
				in.close();
			try{
				con.getInputStream().close();
			} catch (Throwable e){
				
			}
			try{
				con.getOutputStream().close();
			} catch (Throwable e){
				
			}
			con.disconnect();
		}
		return result;
	}
	
	public static String sendGet(String url, int time) throws Exception {
		String result = "";
		System.out.println("Call url: "+url);
		HttpURLConnection con = null;
		BufferedReader in = null;
		try {
			URL httpUrl = new URL(url);
			con = (HttpURLConnection) httpUrl.openConnection();
			con.setRequestMethod("GET");
			con.setReadTimeout(time * 1000);
			int responseCode = con.getResponseCode();
			log.info("Get return ["+responseCode+"] from "+url);
			if (responseCode == HttpURLConnection.HTTP_OK) {
				in = new BufferedReader(new InputStreamReader(con
						.getInputStream()));
				String line;
				while ((line = in.readLine()) != null) {
					result += line;
				}
				return result;
			} else {
				
			}
		} catch (IOException e) {
			throw e;
		} finally {
			if(in != null)
				in.close();
			try{
				con.getInputStream().close();
			} catch (Throwable e){
				
			}
			try{
				con.getOutputStream().close();
			} catch (Throwable e){
				
			}
			con.disconnect();
		}
		return result;
	}
	
	public static String sendFile(String url, String filename) throws Exception{
		File file = new File(filename);
		if(!file.exists())
			throw new Exception("File ["+filename+"] not found");
		HttpURLConnection con = null;
		BufferedReader in = null;
		BufferedOutputStream out = null;
		InputStream fileIn = null;
		String result = null;
		try{
			
			URL httpUrl = new URL(url);
			con = (HttpURLConnection) httpUrl.openConnection();
			con.setRequestMethod("POST");
			con.setDoOutput(true);
			con.setReadTimeout(3600000);
			out = new BufferedOutputStream(con.getOutputStream());
			
			fileIn = new BufferedInputStream(new FileInputStream(file));
			byte[] data = new byte[1024];
			int count = -1;
			while((count = fileIn.read(data)) != -1){
				out.write(data, 0, count);
			}
			out.flush();
			out.close();
			fileIn.close();
			int responseCode = con.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				in = new BufferedReader(new InputStreamReader(con
						.getInputStream()));
				String line;
				result = "";
				while ((line = in.readLine()) != null) {
					result += line;
				}
				return result;
			} else {

			}
		} catch (Exception e){
			throw e;
		} finally {
			try{
				if(in != null)
					in.close();
			}catch (Exception e){}
			try{
				if(in != null)
					out.close();
			}catch (Exception e){}
			try{
				con.getInputStream().close();
			} catch (Throwable e){
				
			}
			try{
				con.getOutputStream().close();
			} catch (Throwable e){
				
			}
			con.disconnect();
			
		}
		
		return result;
	}
	
	public static void main(String[] args) {
//		String url = "http://127.0.0.1:3000/openService";
//		String data = "action=interfaceSms&account=N00000000143&password=123456&num=18600030392&sign=七陌科技&content=测试短信";
//		String url = "http://127.0.0.1:8888/GDSmsService";
//		String data = "args=2%2C18600030392%2C0%2CF671695150414161113%2C20150414161119%3B";
		try {
//			String url = "http://127.0.0.1:7000";
//			String url = "http://116.255.153.10:7788/statusApi.aspx";
//			String newData ="action=query&userid=88&account=qimuzy&password=123456";
//			String data = "action=login&data={\"userName\":\"abc\",\"password\":\"abc\",\"device\":\"ios\"}";
//			System.out.println(HttpProvider.sendPost(url, newData.getBytes("UTF-8")));
			
//			String url = "http://120.55.72.213:7780/app";
//			String data = "?Action=Dialout&ActionID=1234567890&Account=N00000000143&Exten=18600030392&FromExten=7000&PBX=1.1.1.101";
//			System.out.println(HttpProvider.sendPost(url, data.getBytes("UTF-8")));
			
//			String data = "{\"Command\":\"Action\",\"Action\":\"NewWebchat\",\"ActionID\":\"NewWebchat1234\",\"PBX\":\"1.1.1.101\","+
//							"\"Account\":\"N00000000143\",\"Sid\":\"sidsid\",\"MessageId\":\"123abc\",\"CreateTime\":\"123456\"," +
//							"\"MsgType\":\"newMsg\",\"AccessId\":\"79fa56a0-40d1-11e5-8bcb-f3d7d2578a5f\",\"Content\":\"hahahaha\"}";
//			System.out.println(HttpProvider.sendPost(url, data.getBytes("UTF-8")));			
			//jian zhou sms report test
//			String url = "http://115.29.10.194:8888/JZSmsService";
//			String data = "args=323456781%2C18717830363%2CDELIVRD%2C2013-12-12+12%3A12%3A12";
//			System.out.println(HttpProvider.sendPost(url, data.getBytes("UTF-8")));
			//yimi new sms report test
//			String url1 = "http://127.0.0.1:8888/YMNewSmsService?receiver=&pswd=&msgid=2021110150147099500&reportTime=1511101502&mobile=18600030392&status=DELIVRD";
//			System.out.println(HttpProvider.sendGet(url1));
			//ytx flow test
//			String url = "http://115.29.10.194:3000/ytxVoiceVerify";
////			
//			String data = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Request><appId>aaf98f8950e15fc10150ef88f9c32147</appId>"+
//			"<rechargeId>fe395ca79b86424fa9b48336351dd90c</rechargeId><phoneNum>18515056916</phoneNum>"+
//			"<status>3</status><msg>充值成功</msg><customId>cx</customId></Request>";
//			System.out.println(HttpProvider.sendPost(url, data.getBytes("UTF-8")));
			//
//			String url = "http://127.0.0.1:4000/qiniu/mps/notfiy";
//			String data = "{\"id\":\"z0.564894907823de48a830705e\",\"pipeline\":\"1380449361.IvrMusic\","+
//"\"code\":0,\"desc\":\"The fop was completed successfully\",\"reqid\":\"0SAAAMOIcPb75RYU\","+
//"\"inputBucket\":\"m7-ivr-music/\",\"inputKey\":\"c99a4a2ccfff827f1949667c00871819\",\"items\":[{"+
//"\"cmd\":\"avthumb/wav/ab/128k/ar/8000\",\"code\":0,\"desc\":\"The fop was completed successfully\",\"hash\":\"FiEzG-xtkqQoU6SU6L77c0yPGFoh\","+
//"\"key\":\"OOuPmA8_Tkoz3I8ZZudLM9orU1I=/FvvfFYDVMp_OTCqG7LnRjkQxGdFC\",\"returnOld\":0}]}";
//			System.out.println(HttpProvider.sendPost(url, data.getBytes("UTF-8")));	
			
//			String url1 = "http://120.27.133.149:7789";
//			String data2 = "{\"Account\":\"N00000004347\",\"PBX\":\"bj.ali.3.3\",\"Action\":\"AccountRemove\",\"Command\":\"Action\",\"ActionID\":\"AccountRemove0.731477020541206\"}\r\n";
////			String data1 = "{\"_id\":\"dda71670-9577-11e5-8e89-8d875f2a4fcf\",\"Exten\":\"10036\",\"Account\":\"N00000000103\",\"PBX\":\"dh.pbx.3.0\",\"Action\":\"PeerRemove\",\"Peer\":\"dynamic-route\",\"Command\":\"Action\",\"ActionID\":\"PeerRemove0.2560591937508434\"}\r\n";
//			System.out.println(HttpProvider.sendPost(url1, data2.getBytes("UTF-8")));	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
