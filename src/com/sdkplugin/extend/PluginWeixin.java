package com.sdkplugin.extend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.bowlong.security.MD5;
import com.coolape.lbs79.wxapi.WXPayEntryActivity;
import com.sdkplugin.bridge.AbsU3DListener;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class PluginWeixin extends AbsU3DListener {

	static private IWXAPI iwxApi;

	static public IWXAPI getSingleIWxApi(String appId, Activity activity) {
		if (iwxApi == null) {
			iwxApi = WXAPIFactory.createWXAPI(activity, appId);
			iwxApi.registerApp(appId);
		}
		return iwxApi;
	}

	Map<String, String> toMap(String appId, String partnerId, String prepayId,
			String nonceStr, String timeStamp) {
		Map<String, String> ret = new HashMap<String, String>();
		ret.put("appid", appId);
		ret.put("partnerid", partnerId);
		ret.put("prepayid", prepayId);
		ret.put("package", "Sign=WXPay");
		ret.put("noncestr", nonceStr);
		ret.put("timestamp", timeStamp);
		return ret;
	}
	
	String getStr4Sign(Map<String, String> map) {
		List<String> list = new ArrayList<String>();
		list.addAll(map.keySet());
		Collections.sort(list);
		StringBuffer buff = new StringBuffer();
		for (String key : list) {
			buff.append(key).append("=").append(map.get(key)).append("&");
		}
		buff.append("key").append("=").append(WXPayEntryActivity.Key);
		String v = buff.toString();
		buff.setLength(0);
		buff = null;
		return v;
	}
	
	String getSign(Map<String, String> map){
		String str = getStr4Sign(map);
		String sign = MD5.toMD5(str);
		return sign.toUpperCase();
	}

	@Override
	public void receiveFromUnity(String parsJson) throws Exception {
		final String json = parsJson;
		final JSONObject jsonObj = new JSONObject(json);
		final String appId = jsonObj.getString("appId");
		final String partnerId = jsonObj.getString("partnerId");
		final String prepayId = jsonObj.getString("prepayId");
		final String nonceStr = jsonObj.getString("nonceStr");
		final String timeStamp = jsonObj.getString("timeStamp");
		final String sign = jsonObj.getString("sign");
		final boolean isDebug = jsonObj.getBoolean("isDebug");
		
		Map<String, String> map = toMap(appId, partnerId, prepayId, nonceStr, timeStamp);
		final String signLoc = getSign(map);

		if (isDebug) {
			System.out.println("== jsonObj weixin ==");
			System.out.println("appId=" + appId);
			System.out.println("partnerId=" + partnerId);
			System.out.println("prepayId=" + prepayId);
			System.out.println("nonceStr=" + nonceStr);
			System.out.println("timeStamp=" + timeStamp);
			System.out.println("sign=" + sign);
			System.out.println("signLoc=" + signLoc);
		}

		Handler hd = new Handler(Looper.getMainLooper());
		hd.post(new Runnable() {
			public void run() {
				// 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
				IWXAPI api = getSingleIWxApi(appId, getCurActivity());
				if (!api.isWXAppInstalled()) {
					Toast.makeText(getCurActivity(), "没有安装微信",
							Toast.LENGTH_SHORT).show();
					Tools.msg2U3D("fails", "没有安装微信", "");
					return;
				}

				if (!api.isWXAppSupportAPI()) {
					Toast.makeText(getCurActivity(), "微信当前版本不支持支付功能",
							Toast.LENGTH_SHORT).show();
					Tools.msg2U3D("fails", "微信当前版本不支持支付功能", "");
					return;
				}

				PayReq req = new PayReq();
				req.appId = appId;
				req.partnerId = partnerId;
				req.prepayId = prepayId;
				req.nonceStr = nonceStr;
				req.timeStamp = timeStamp;
				req.packageValue = "Sign=WXPay";
				// req.sign = sign;
				req.sign = signLoc;
				boolean isOkey = api.sendReq(req);

				if (isDebug) {
					System.out.println("== weixin == isOkey = " + isOkey);
				}
			}
		});

	}

}
