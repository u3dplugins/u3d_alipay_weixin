package com.sdkplugin.extend;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.sdkplugin.alipay.PayResult;
import com.sdkplugin.alipay.SignUtils;
import com.sdkplugin.bridge.AbsU3DListener;

public class PluginAlipay extends AbsU3DListener {
	// 商户PID
	public static final String PARTNER = "2088312996627832";

	// 商户收款账号 密码:jinyan8825
	public static final String SELLER = "jinyan@jinyancc.com";

	// 商户私钥，pkcs8格式
	public static final String RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBANvBU8o1oFiqVVg9"
			+ "2xnA72kBNC+yooY6qnMbYPQ5FWDmeleRuKSm43ZP9sGPP4Pmjpsb9hk+SgQpvLaz"
			+ "DdsxEU24Hh7UO3IAqqQGM1q4NQzwGQrhAM8XNl7KsyluqcU6WoQ52zp9Xh6weCW0"
			+ "q63zEQ26pODmLvHkUb494GHTlYWbAgMBAAECgYBHTf6zIR1rR7GzlTaPr7O/PZ3C"
			+ "UFM9/LEr8llORWtzarWNzoG8I06xKBcoDSQN13S0TalX3YdEvIo4OfcoBU7Yb8ql"
			+ "x0t6Em2uzGeVDdf5l/G4GT1bcZ7I/Cn4Xrppl3eM3zGqF0fLfv5HPAT3uY6N02+P"
			+ "ABVg61Argnc1u4bCqQJBAPxUNkmp4sMZaYGmNF4U2Wq+OOksP4cC3d5b/ws7/PHQ"
			+ "wrvia1WZP0ked/lPoq0ouWbISNH4v04vVhuYn1esCv0CQQDe88uylOnzwweFeUfZ"
			+ "qXWsfQVu9VuOldPJBRefuHc6qwCTxG0NxGG8WGNFEPNYnLLUldy9OoHjX2ua+3gN"
			+ "gzJ3AkAYS2tmsEq7CilUaFJGdK9yTZphKPd84lPGYwktZbs29SiqumQnO7gSLDLU"
			+ "+EvrbC5drQ8F2HNi7b/WrF07Lb9pAkEAow0XBraDVqNmR8YHtvaIuaoBJEHKQL/w"
			+ "UA4qVcP5Zm2TEX5Q5wWdGLpndyZLYZ3P50rmUXmNbnTlZEXOactuRwJAEGUxgBmg"
			+ "+4bKjdUWY39EYgxLTcy//2PW0xr4Wr+gSQkvLdc8BoEtUvHJLdOP/pkedBfNbMlE"
			+ "Jm/wMfE3WSC8Bw==";

	// 支付宝公钥
	public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDbwVPKNaBYqlVYPdsZwO9pATQvsqKGOqpzG2D0ORVg5npXkbikpuN2T/bBjz+D5o6bG/YZPkoEKby2sw3bMRFNuB4e1DtyAKqkBjNauDUM8BkK4QDPFzZeyrMpbqnFOlqEOds6fV4esHgltKut8xENuqTg5i7x5FG+PeBh05WFmwIDAQAB";

	// 异步回调
	public static final String Notify_Url = "http://112.23.2.188:29002/mobileInterface/callbackforalipay";

	private static final int SDK_PAY_FLAG = 1;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler(Looper.getMainLooper()) {
		@SuppressWarnings("unused")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				PayResult payResult = new PayResult((String) msg.obj);
				/**
				 * 同步返回的结果必须放置到服务端进行验证 <br/>
				 * （验证的规则请看 <br/>
				 * https://doc.open.alipay.com/doc2/detail.htm?spm=0.0.0.0.
				 * xdvAU6&treeId=59&articleId=103665&docType=1 <br/>
				 * ) <br/>
				 * 建议商户依赖异步通知
				 */
				String resultInfo = payResult.getResult();// 同步返回需要验证的信息

				String resultStatus = payResult.getResultStatus();

				Map<String, String> map = Tools.buildMapByQuery(payResult
						.getResult());

				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					Toast.makeText(getCurActivity(), "支付成功", Toast.LENGTH_SHORT)
							.show();
					Tools.msg2U3D("success", "支付成功", map.get("out_trade_no"));
				} else {
					// 判断resultStatus 为非"9000"则代表可能支付失败
					// "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(getCurActivity(), "支付结果确认中",
								Toast.LENGTH_SHORT).show();
						Tools.msg2U3D("wait", "支付结果确认中", "");
					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						Toast.makeText(getCurActivity(), "支付失败",
								Toast.LENGTH_SHORT).show();
						Tools.msg2U3D("fails", "支付失败", "");
					}
				}
				break;
			}
			default:
				break;
			}
		};
	};

	/**
	 * call alipay sdk pay. 调用SDK支付
	 * 
	 */
	public void pay(String tradeNo, String subject, String body, String price) {
		if (TextUtils.isEmpty(PARTNER) || TextUtils.isEmpty(RSA_PRIVATE)
				|| TextUtils.isEmpty(SELLER)) {
			System.out.println("需要配置PARTNER | RSA_PRIVATE| SELLER");
			return;
		}
		String orderInfo = getOrderInfo(tradeNo, subject, body, price);

		/**
		 * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
		 */
		String sign = sign(orderInfo);
		try {
			/**
			 * 仅需对sign 做URL编码
			 */
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		/**
		 * 完整的符合支付宝参数规范的订单信息
		 */
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
				+ getSignType();

		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask(getCurActivity());
				// 调用支付接口，获取支付结果
				String result = alipay.pay(payInfo, true);

				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}

	/**
	 * create the order info. 创建订单信息
	 * 
	 */
	private String getOrderInfo(String tradeNo, String subject, String body,
			String price) {

		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + PARTNER + "\"";

		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + tradeNo + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";

		// 商品金额(单位为RMB-Yuan)
		orderInfo += "&total_fee=" + "\"" + price + "\"";

		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" + Notify_Url + "\"";

		// 服务接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";

		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		// orderInfo += "&return_url=\"m.alipay.com\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
		// orderInfo += "&paymethod=\"expressGateway\"";

		return orderInfo;
	}

	/**
	 * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
	 * 
	 */
	String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
				Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);

		Random r = new Random();
		key = key + r.nextInt();
		key = key.substring(0, 15);
		return key;
	}

	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param content
	 *            待签名订单信息
	 */
	private String sign(String content) {
		return SignUtils.sign(content, RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 */
	private String getSignType() {
		return "sign_type=\"RSA\"";
	}

	@Override
	public void receiveFromUnity(String json) throws Exception {
		JSONObject jsonObj = new JSONObject(json);
		String tradeNo = jsonObj.getString("tradeNo");
		// 商品名称
		String subject = jsonObj.getString("subject");
		String body = jsonObj.getString("body");
		String price = jsonObj.getString("price");

		boolean isDebug = jsonObj.getBoolean("isDebug");
		if (isDebug) {
			System.out.println("== alipay ==");
			System.out.println(json);
		}
		pay(tradeNo, subject, body, price);
	}
}
