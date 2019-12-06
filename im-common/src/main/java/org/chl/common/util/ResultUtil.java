package org.chl.common.util;

import com.alibaba.fastjson.JSONObject;

public class ResultUtil {
	
	public static JSONObject success(JSONObject data, String msg) {
		JSONObject result = new JSONObject();
		result.put("status", Boolean.TRUE);
		result.put("data", data);
		result.put("msg", msg);
		return result;
	}

	public static JSONObject failure(String msg) {
		JSONObject result = new JSONObject();
		result.put("status", Boolean.FALSE);
		result.put("msg", msg);
		return result;
	}
}
