package com.coolape.lbs79.wxapi;

import android.app.Activity;
import android.os.Bundle;

import com.sdkplugin.extend.PluginWeixin;
import com.sdkplugin.extend.Tools;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler{
	
	public static final String AppId = "wx20652a715bba49fa";
	public static final String AppSecret = "4bf0f9a52d97eda2dbd533b7de353d49";
	public static final String Mch_id = "1355289002";
	// key并非AppID或AppSectet，而是在商户平台设置的
	public static final String Key = "1qaz2wsx3edc4rfv4rfv3edc2wsx1qaz";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.pay_result);
		
		IWXAPI api = PluginWeixin.getSingleIWxApi(AppId, this);
		api.handleIntent(getIntent(), this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		// resp.errCode
		// 0 成功 展示成功页面
		// -1 错误 可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等。
		// -2 用户取消 无需处理。发生场景：用户不支付了，点击取消，返回APP。
		boolean isDebug = true;
		if (isDebug) {
			// resp.errCode
			// 0 成功 展示成功页面
			// -1 错误 可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等。
			// -2 用户取消 无需处理。发生场景：用户不支付了，点击取消，返回APP。
			System.out.println("errCode = " + resp.errCode);
			System.out.println("errStr = " + resp.errStr);
			System.out.println("openId = " + resp.openId);
			System.out.println("transaction = " + resp.transaction);
		}
		
		switch (resp.errCode) {
		case 0:
			// 0 成功 展示成功页面
			if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
				Tools.msg2U3D("success","交易成功",resp.transaction);
			}else{
				Tools.msg2U3D("fails","交易失败，resp.getType() != COMMAND_PAY_BY_WX","");
			}
			break;
		case -1:
			Tools.msg2U3D("fails","签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等","");
			break;
		case -2:
			Tools.msg2U3D("fails","用户取消 无需处理","");
			break;
		default:
			Tools.msg2U3D("fails","其他情况","");
			break;
		}
		close();
	}
	
	void close() {
		finish();
	}
}
