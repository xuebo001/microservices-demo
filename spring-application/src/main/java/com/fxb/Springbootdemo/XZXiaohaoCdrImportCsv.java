package com.fxb.Springbootdemo;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import util.HttpProvider;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * 讯众小号话单导入
 * 
 * @author fxb
 *
 */
public class XZXiaohaoCdrImportCsv {
	private static Logger log = Logger.getLogger(XZXiaohaoCdrImportCsv.class.getName());

	private static String filepath = "/Users/fxb/Desktop/qm.csv";
	private static String account = "N00000023852";
	private static String secret = "e4827fb0-eae8-11e8-80f9-adf0551f60fe";
	private static String url = "http://apis.7moor.com/v20160818/rlxh/inserCdrByXZ";
	//private static String url = "http://127.0.0.1:4000/v20160818/rlxh/inserCdrByXZ";
	private static String title = "appkey,telA,telB,telX,calltime,ringingtime,starttime,releasetime,calltype,releasecause,callid,subid,recordUrl,platform";

	public static void main(String[] args) throws Exception {
		try {
			readCsv(filepath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	// 读取csv文件的内容
	public static void readCsv(String filepath) {
		File csv = new File(filepath); // CSV文件路径
		csv.setReadable(true);//设置可读
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(csv));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String line = "";
		int index = 0;
		String[] titles = title.split(",");
		try {
			while ((line = br.readLine()) != null) // 读取到的内容给line变量
			{
				//跳过第一行
				if (index == 0){
					index ++;
					continue;
				}
				HashMap<String,Object> params = lineTransToMap(line,titles);
				System.out.println("csv表格数据读取行数：" + index);
				//调用restapi接口
				System.out.println("params:" + params);
				log.info("params:" + params);
				String time = getDateTime();
				String sig = md5(account + secret + time);
				//url = url + "?sig=" + sig;
				String auth = base64(account + ":" + time);
				System.out.println(url);
				System.out.println(auth);
				String res = HttpProvider.doPost(url,params,auth);
				log.info("res is :" + res);
				System.out.println("res is :" + res);
				index ++;
			}
			System.out.println("csv表格数据读取结束！！！！！！！！！！！！");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	private static HashMap<String,Object> lineTransToMap(String line, String[] titles) throws ParseException {
		HashMap<String,Object> linemap = new HashMap<String,Object>();
		String[] lineStrs = line.split(",");
		for (int i = 1; i< 14; i++){
			linemap.put(titles[i],lineStrs[i]);
		}
		return linemap;
	}

	public static String md5(String text) {
		return DigestUtils.md5Hex(text).toUpperCase();
	}

	public static String base64(String text) {
		byte[] b = text.getBytes();
		Base64 base64 = new Base64();
		b = base64.encode(b);
		String s = new String(b);
		return s;
	}

	public static String getDateTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(new Date());
	}
}
