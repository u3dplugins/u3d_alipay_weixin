package com.weixin.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bowlong.security.MD5;
import com.weixin.WeiXinXMLHelper;

/**
 * 统一下单--生成预付单
 * 
 * @author Canyon
 * 
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class Advance implements Serializable {

	private static final long serialVersionUID = -2741969764904805653L;

	/** ################# 常用的参数固定值 beg ################# **/
	// key设置路径：微信商户平台(pay.weixin.qq.com)-->账户设置-->API安全-->密钥设置
	static public String key = "";
	static public String APPID = ""; // 应用APPID
	static public String MCHID = ""; // 商户号
	static public String HOST_IP = ""; // 用户端实际ip
	static public String NOTIFY_URL = ""; // 异步通知回调地址
	/** ################# 常用的参数固定值 end ################# **/

	// 微信开放平台审核通过的应用APPID (必填:是)
	private String appid;

	// 微信支付分配的商户号 (必填:是)
	private String mch_id;

	// 终端设备号(门店号或收银设备ID)，默认请传"WEB" (必填:否)
	private String device_info;

	// 随机字符串，不长于32位。推荐随机数生成算法 (必填:是)
	private String nonce_str;

	// 签名 (必填:是)
	private String sign;

	// 签名类型 默认为MD5 (必填:否)
	private String sign_type;

	// 商品或支付单简要描述 (必填:是)
	private String body;

	// 商品名称明细列表-商品详情 (必填:否)
	private String detail;

	// 附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据 (必填:否)
	private String attach;

	// 商户系统内部的订单号,32个字符内、可包含字母 (必填:是)
	private String out_trade_no;

	// 符合ISO 4217标准的三位字母代码，默认人民币：CNY (必填:否)
	private String fee_type;

	// 订单总金额，单位为分 (必填:是)
	private String total_fee;

	// 用户端实际ip (必填:是)
	private String spbill_create_ip;

	// 订单生成时间，格式为yyyyMMddHHmmss (必填:否)
	private String time_start;

	// 订单失效时间，格式为yyyyMMddHHmmss (必填:否)
	// 最短失效时间间隔必须大于5分钟
	private String time_expire;

	// 商品标记，代金券或立减优惠功能的参数 (必填:否)
	private String goods_tag;

	// 异步通知回调地址 (必填:是)
	private String notify_url;

	// 支付类型 : APP (必填:是)
	private String trade_type;

	// 指定支付方式:no_credit--指定不能使用信用卡支付 (必填:否)
	private String limit_pay;

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getMch_id() {
		return mch_id;
	}

	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}

	public String getDevice_info() {
		return device_info;
	}

	public void setDevice_info(String device_info) {
		this.device_info = device_info;
	}

	public String getNonce_str() {
		return nonce_str;
	}

	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getSign_type() {
		return sign_type;
	}

	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getFee_type() {
		return fee_type;
	}

	public void setFee_type(String fee_type) {
		this.fee_type = fee_type;
	}

	public String getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(String total_fee) {
		this.total_fee = total_fee;
	}

	public String getSpbill_create_ip() {
		return spbill_create_ip;
	}

	public void setSpbill_create_ip(String spbill_create_ip) {
		this.spbill_create_ip = spbill_create_ip;
	}

	public String getTime_start() {
		return time_start;
	}

	public void setTime_start(String time_start) {
		this.time_start = time_start;
	}

	public String getTime_expire() {
		return time_expire;
	}

	public void setTime_expire(String time_expire) {
		this.time_expire = time_expire;
	}

	public String getGoods_tag() {
		return goods_tag;
	}

	public void setGoods_tag(String goods_tag) {
		this.goods_tag = goods_tag;
	}

	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public String getTrade_type() {
		return trade_type;
	}

	public void setTrade_type(String trade_type) {
		this.trade_type = trade_type;
	}

	public String getLimit_pay() {
		return limit_pay;
	}

	public void setLimit_pay(String limit_pay) {
		this.limit_pay = limit_pay;
	}

	public Advance() {
		super();
	}

	public Advance(String appid, String mch_id, String device_info,
			String nonce_str, String sign, String sign_type, String body,
			String detail, String attach, String out_trade_no, String fee_type,
			String total_fee, String spbill_create_ip, String time_start,
			String time_expire, String goods_tag, String notify_url,
			String trade_type, String limit_pay) {
		super();
		this.appid = appid;
		this.mch_id = mch_id;
		this.device_info = device_info;
		this.nonce_str = nonce_str;
		this.sign = sign;
		this.sign_type = sign_type;
		this.body = body;
		this.detail = detail;
		this.attach = attach;
		this.out_trade_no = out_trade_no;
		this.fee_type = fee_type;
		this.total_fee = total_fee;
		this.spbill_create_ip = spbill_create_ip;
		this.time_start = time_start;
		this.time_expire = time_expire;
		this.goods_tag = goods_tag;
		this.notify_url = notify_url;
		this.trade_type = trade_type;
		this.limit_pay = limit_pay;
	}

	public Advance(String appid, String mch_id, String body,
			String out_trade_no, String total_fee, String spbill_create_ip,
			String notify_url, String trade_type) {
		super();
		this.appid = appid;
		this.mch_id = mch_id;
		this.nonce_str = MD5.MD5UUIDStime();
		// this.sign = sign;(MD5签名后在赋值)
		this.body = body; // 商品标题
		this.out_trade_no = out_trade_no;
		this.total_fee = total_fee;
		this.spbill_create_ip = spbill_create_ip;
		this.notify_url = notify_url;
		this.trade_type = trade_type;
	}

	public Advance(String body, String out_trade_no, String total_fee) {
		this(APPID, MCHID, body, out_trade_no, total_fee, HOST_IP, NOTIFY_URL,
				"APP");
	}

	public Map toBasicMap() {
		Map result = new HashMap();
		result.put("appid", appid);
		result.put("mch_id", mch_id);
		result.put("device_info", device_info);
		result.put("nonce_str", nonce_str);
		result.put("sign", sign);
		result.put("sign_type", sign_type);
		result.put("body", body);
		result.put("detail", detail);
		result.put("attach", attach);
		result.put("out_trade_no", out_trade_no);
		result.put("fee_type", fee_type);
		result.put("total_fee", total_fee);
		result.put("spbill_create_ip", spbill_create_ip);
		result.put("time_start", time_start);
		result.put("time_expire", time_expire);
		result.put("goods_tag", goods_tag);
		result.put("notify_url", notify_url);
		result.put("trade_type", trade_type);
		result.put("limit_pay", limit_pay);
		return result;
	}

	Map toMapClear() {
		Map map = toBasicMap();
		List listKeys = new ArrayList();
		listKeys.addAll(map.keySet());
		int lens = listKeys.size();
		for (int i = 0; i < lens; i++) {
			Object key = listKeys.get(i);
			Object val = map.get(key);
			if (val == null) {
				map.remove(key);
				continue;
			}
			String valStr = val.toString().trim();
			if (valStr.isEmpty()) {
				map.remove(key);
			} else {
				if (val instanceof Double) {
					double v = (double) val;
					if (v <= 0) {
						map.remove(key);
					}
				} else if (val instanceof Integer) {
					int v = (int) val;
					if (v <= 0) {
						map.remove(key);
					}
				}
			}
		}
		return map;
	}

	public Map<String, String> toMapKV() {
		Map map = toMapClear();
		Map<String, String> result = new HashMap<String, String>();
		for (Object key : map.keySet()) {
			Object val = map.get(key);
			result.put(key.toString(), val.toString());
		}
		return result;
	}

	/**
	 * 使用URL键值对的格式（即key1=value1&key2=value2…）拼接成字符串stringA <br/>
	 * 参数名ASCII码从小到大排序（字典序）
	 * 
	 * @param params
	 * @return
	 */
	public String toStringA() {
		return createLinkString(toMapKV());
	}

	public String toStr4Sign() {
		return toStringA() + "&key=" + key;
	}

	public void MakeSign(){
		this.sign = null;
		this.sign = MD5.MD5Encode(toStr4Sign()).toUpperCase();
	}

	public String getXml(boolean isCDATA) {
		MakeSign();
		Map<Object, Object> ret = toMapClear();
//		ret = new HashMap<>();
//		Map<String, String> vMap = toMapKV();
//		for (Entry<String, String> entry : vMap.entrySet()) {
//			ret.put(entry.getKey(), entry.getValue());
//		}
		return WeiXinXMLHelper.getXmlByMap(ret,isCDATA);
	}

	/**
	 * 使用URL键值对的格式（即key1=value1&key2=value2…）拼接成字符串stringA <br/>
	 * 参数名ASCII码从小到大排序（字典序）
	 * 
	 * @param params
	 * @return
	 */
	public static String createLinkString(Map<String, String> params) {
		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);
		String prestr = "";
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = params.get(key);
			if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
				prestr = prestr + key + "=" + value;
			} else {
				prestr = prestr + key + "=" + value + "&";
			}
		}
		return prestr;
	}
}
