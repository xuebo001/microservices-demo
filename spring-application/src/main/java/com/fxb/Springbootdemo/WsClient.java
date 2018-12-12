package com.fxb.Springbootdemo;

import com.google.gson.JsonObject;
import org.codehaus.xfire.client.Client;
import util.HttpProvider;

import java.io.UnsupportedEncodingException;
import java.net.URL;

/**
 * 华夏出行
 * 
 * @author Pansy
 *
 */
public class WsClient {
	private static String url = "http://14.29.16.125:5000/dcmsWebservice/webservice/QueryRecordFromMongo?wsdl";
	private static String testurl = "http://101.201.58.168/dcmsWebservice/webservice/QueryRecordFromMongo?wsdl";

	public static void main(String[] args) throws Exception {
		//System.out.println(queryRecordAddr(url,"N00000001241","3001","1111@aaaa","64b5f078-a758-4492-9b0a-866dd9a40338:N00000001241:8000"));
		String geturlw="http://127.0.0.1:4000/api/getStatus";
		String geturl="https://scbgw.nonobank.com/sms-app/callback/qmVoiceCallBack";
		//String data="actionid=741852963&Message=4";
		//String Messageres = "";
		//Messageres = HttpProvider.sendPost(geturl, data.getBytes("UTF-8"),1000);
		//System.out.println(Messageres);

		try {
			boolean isHttps = false;
			String res = "";
			String params = getWebcallParams("123", "123");
			System.out.println("begin aync webll send url=" + url + "......params=" + params);
			res = HttpProvider.sendPost(geturlw, params.getBytes("UTF-8"),1000);
			System.out.println("end send url= " + url+"......res=" + res);



		} catch (Exception e) {

		}
	}
	public static String queryRecordAddr(String url, String entId, String userName, String password, String sessionId) throws Exception {
		Client client = new Client(new URL(url));
		Object[] results = client.invoke("queryRecordAddr", new Object[] {entId, userName, password, sessionId});
		String recordUrl = (String) results[0];
		return recordUrl;
	}
	public static String getWebcallParams(String actionID, String message)
			throws UnsupportedEncodingException {
		JsonObject json = new JsonObject();
		json.addProperty("actionid", actionID);
		json.addProperty("Message", message);

		return json.toString();
	}

}
