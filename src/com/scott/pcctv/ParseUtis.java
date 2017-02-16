package com.scott.pcctv;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ParseUtis {
	private static String prefix = "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2015/";
	private static String index = "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2015/index.html";

	public static ArrayList getNameByMap(HashMap provLinkMap) {
		ArrayList provNameList = new ArrayList();
		for (Object provName : provLinkMap.keySet()) {
			provNameList.add((String) provName);
		}
		return provNameList;
	}

	public static Document getDocument(String url) {
		try {
			// return Jsoup.connect(url).get();
			// return Jsoup.connect(url).timeout(10000).get();
			return Jsoup.connect(url).userAgent("Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)")
					.referrer(index).timeout(10000).get();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static HashMap getProvLinkMap(String url) {
		HashMap provLinkMap = new HashMap();
		Document doc = getDocument(url);

		// 获取目标HTML代码
		Elements mainTaget = doc.select("table[class=provincetable]");
		Elements hrefs = mainTaget.select("a[href]");
		for (Element elem : hrefs) {
			String h = elem.attr("abs:href");
			String postfix = h.substring(h.length() - 7, h.length());
			// if (!h.startsWith(prefix)) {
			// continue;
			// }
			// System.out.println(postfix);
			// System.out.println(h);
			Elements n = doc.select("a[href=" + postfix + "]");
			String name = n.text();
			// System.out.println(name);
			provLinkMap.put(name, h);
		}
		return provLinkMap;
	}

	public static HashMap getCityLinkMap(String urlProv) {
		HashMap cityLinkMap = new HashMap();
		Document doc = getDocument(urlProv);

		// 获取目标HTML代码
		Elements mainTaget = doc.select("table[class=citytable]");
		Elements hrefs = mainTaget.select("a[href]");
		int i = 0;
		for (Element elem : hrefs) {
			if ((i++) % 2 == 0) {
				continue;
			}
			String h = elem.attr("abs:href");
			String postfix = h.substring(h.length() - 12, h.length());
			// if (!h.startsWith(prefix)) {
			// continue;
			// }
			// System.out.println(postfix);
			// System.out.println(h);
			Elements n = doc.select("a[href=" + postfix + "]");
			String name = n.get(1).text();
			// System.out.println(name);
			cityLinkMap.put(name, h);
		}
		return cityLinkMap;
	}

	public static HashMap getCountyLinkMap(String urlCity) {
		HashMap countyLinkMap = new HashMap();
		Document doc = getDocument(urlCity);

		// 获取目标HTML代码
		Elements mainTaget = doc.select("table[class=countytable]");
		Elements hrefs = mainTaget.select("a[href]");
		int i = 0;
		for (Element elem : hrefs) {
			if ((i++) % 2 == 0) {
				continue;
			}
			String h = elem.attr("abs:href");
			String postfix = h.substring(h.length() - 14, h.length());
			// if (!h.startsWith(prefix)) {
			// continue;
			// }
			// System.out.println(postfix);
			// System.out.println(h);
			Elements n = doc.select("a[href=" + postfix + "]");
			String name = n.get(1).text();
			System.out.println(name);
			countyLinkMap.put(name, h);
		}
		return countyLinkMap;
	}

	public static HashMap getTownLinkMap(String urlCounty) {
		HashMap townLinkMap = new HashMap();
		Document doc = getDocument(urlCounty);

		Elements mainTaget = doc.select("table[class=towntable]");
		Elements hrefs = mainTaget.select("a[href]");
		int i = 0;
		for (Element elem : hrefs) {
			if ((i++) % 2 == 0) {
				continue;
			}
			String h = elem.attr("abs:href");
			String postfix = h.substring(h.length() - 17, h.length());
			// if (!h.startsWith(prefix)) {
			// continue;
			// }
			// System.out.println(postfix);
			Elements n = doc.select("a[href=" + postfix + "]");
			String name = n.get(1).text();
			// System.out.println(name);
			townLinkMap.put(name, h);
		}
		return townLinkMap;
	}

	public static ArrayList getVillageNameList(String urlTown) {
		ArrayList villageLinkMap = new ArrayList();
		Document doc = getDocument(urlTown);

		// 获取目标HTML代码
		Elements mainTaget = doc.select("table[class=villagetable]");
		Elements hrefs = mainTaget.select("tr[class=villagetr]");
		for (Element elem : hrefs) {
			Elements n = elem.select("td");
			String name = n.get(2).text();
			// System.out.println(name);
			villageLinkMap.add(name);
		}
		return villageLinkMap;
	}

}
