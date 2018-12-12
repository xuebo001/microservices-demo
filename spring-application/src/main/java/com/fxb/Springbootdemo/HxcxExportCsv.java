package com.fxb.Springbootdemo;

import util.HttpProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 华夏出行
 * 
 * @author Pansy
 *
 */
public class HxcxExportCsv {

	public static void main(String[] args) throws Exception {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startDate = dateFormat.parse("2018-10-01 00:00:00");
		Date endDate = dateFormat.parse("2018-11-01 00:00:00");
		long stepSeconds = 1800;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDate);
		String status = "!leak";
		FileOutputStream out = new FileOutputStream(new File("/Users/fxb/Desktop/gwcx/gwcx-20181001-1031-" + status + ".csv"));
		while (calendar.getTime().before(endDate)) {
			if (calendar.getTimeInMillis() != startDate.getTime()) {
				calendar.setTimeInMillis(calendar.getTimeInMillis() + 1);
			}
			String beginTime = dateFormat.format(calendar.getTime());
			calendar.setTimeInMillis(calendar.getTimeInMillis() + stepSeconds * 1000 - 1);
			String endTime = dateFormat.format(calendar.getTime());
			if (calendar.getTime().before(endDate)) {
				String url = "http://121.40.250.6:4100/dingzhi/hxcx/gsCdrChannel/N00000022966/" + beginTime.replace(" ", "%20") + "/"
						+ endTime.replace(" ", "%20") + "/" + status;

				System.out.println(url);
				String result = HttpProvider.sendPost(url, "".getBytes(), 20000);
				System.out.println(result);
				result = result.substring(39);
				result = result.substring(0, result.length() - 2);
				result = result.replaceAll("\\\\t", "\n");
				out.write(result.getBytes("UTF-8"));
			}

		}
	}
}
