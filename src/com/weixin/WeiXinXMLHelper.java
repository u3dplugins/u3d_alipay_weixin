package com.weixin;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class WeiXinXMLHelper {
	/**
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static Map<String, String> parseXML(InputStream in) throws IOException {

		HashMap<String, String> map = new HashMap();
		SAXReader reader = new SAXReader();

		try {
			Document doc = reader.read(in);
			// doc = DocumentHelper.parseText(string);
			Element rootElt = doc.getRootElement();
			List<Element> iterator = rootElt.elements();

			for (Element element : iterator) {
				String name = element.getName();
				String value = element.getStringValue();
				map.put(name, value);
			}
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}

	public static Map<String, String> parseXML(String xml) {

		HashMap<String, String> map = new HashMap();

		try {
			Document doc = DocumentHelper.parseText(xml);
			Element rootElt = doc.getRootElement();
			List<Element> iterator = rootElt.elements();

			for (Element element : iterator) {
				String name = element.getName();
				String value = element.getStringValue();
				map.put(name, value);
			}
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 通过map生成xml
	 * 
	 * @param map
	 * @return
	 */
	public static String getXmlByMap(Map<Object, Object> map, boolean isCDATA) {
		StringBuffer sb = new StringBuffer("<xml>");
		for (Map.Entry<Object, Object> entry : map.entrySet()) {
			sb.append("<" + entry.getKey() + ">");
			if (isCDATA) {
				sb.append("<![CDATA[" + entry.getValue() + "]]>");
			} else {
				sb.append("" + entry.getValue() + "");
			}
			sb.append("</" + entry.getKey() + ">");
		}
		sb.append("</xml>");

		return sb.toString();
	}

}
