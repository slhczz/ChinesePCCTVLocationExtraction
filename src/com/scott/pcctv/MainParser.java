package com.scott.pcctv;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class MainParser {
	private static Map provMapper;

	static {
		provMapper = new HashMap();
	}

	public static void writeIn(String outputDir, String content, String fileName) {
		// fileName = fileName.substring(54, fileName.length()).replace("/",
		// "");
		// System.out.println(fileName);
		String dir = outputDir + "\\" + fileName;
		File file = new File(dir);

		FileOutputStream o = null;
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			o = new FileOutputStream(file);
			o.write(content.getBytes("UTF8"));
			o.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void storeInProperty(String outputDir, String fileName, HashMap<String, String> argMap) {
		String dir = outputDir + "\\" + fileName;
		Properties prop = new Properties();
		try {
			/// 保存属性到b.properties文件
			FileOutputStream oFile = new FileOutputStream(dir, false);// true表示追加打开
			for (Object keyItem : argMap.keySet()) {
				String key = (String) keyItem;
				String prope = argMap.get(key);
				prop.setProperty(key, prope);
			}
			prop.store(oFile, fileName);
			oFile.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public static void main(String[] args) {
		getAllMaps();
		// readAllProv();
	}

	private static String output = "XXX\\properties";//your properties path

	private static void readAllProv() {
		String urlIndex = "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2015/index.html";

		HashMap provLinkMap = ParseUtis.getProvLinkMap(urlIndex);
		ArrayList provNameList = ParseUtis.getNameByMap(provLinkMap);// provList2Map

		ArrayList cityNameList = null;
		for (Object prov : provLinkMap.keySet()) {
			String provName = (String) prov;
			String urlProv = (String) provLinkMap.get(provName);
			System.out.println(provName + "	" + urlProv);
			HashMap<String, String> argMap = new HashMap<String, String>();
			argMap.put("downloarded", "0");
			argMap.put("urlProv", urlProv);
			storeInProperty(output, provName + ".properties", argMap);
		}
	}

	public static HashMap<String, String> readFromProperty(String outputDir, String fileName) {
		HashMap<String, String> maps = new HashMap<String, String>();
		String dir = outputDir + "\\" + fileName;
		Properties prop = new Properties();
		try {
			// 读取属性文件a.properties
			InputStream in = new BufferedInputStream(new FileInputStream(dir));
			prop.load(in); /// 加载属性列表
			Iterator<String> it = prop.stringPropertyNames().iterator();
			while (it.hasNext()) {
				String key = it.next();
				maps.put(key, prop.getProperty(key));
			}
			in.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		return maps;
	}

	private static List iterateAllPropertyFiles(String outputDir, String fileName) {
		HashMap<String, String> maps = readFromProperty(outputDir, fileName);
		String downloarded = maps.get("downloarded");
		String urlProv = maps.get("urlProv");
		ArrayList args = new ArrayList();
		args.add(Integer.parseInt(downloarded));
		args.add(urlProv);
		return args;
	}

	private static void updateProvProperty(String provName, String urlProv, String downloarded) {
		HashMap<String, String> argMap = new HashMap<String, String>();
		argMap.put("downloarded", "1");
		argMap.put("urlProv", urlProv);
		storeInProperty(output, provName + ".properties", argMap);
	}

	private static void getAllMaps() {
		String urlIndex = "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2015/index.html";

		// HashMap provLinkMap = ParseUtis.getProvLinkMap(urlIndex);

		// ArrayList provNames = new ArrayList();
		File file = new File(output);
		String[] files = file.list();
		// for (int i = 0; i < files.length; i++) {
		// provNames.add(files[i]);
		// }

		for (int i = 0; i < files.length; i++) {
			List args = iterateAllPropertyFiles(output, files[i]);
			int downloarded = (Integer) args.get(0);
			if (downloarded == 0) {
				String provName = files[i].substring(0, files[i].length() - 11);
				String urlProv = (String) args.get(1);

				HashMap cityLinkMap = ParseUtis.getCityLinkMap(urlProv);

				HashMap cityMap = new HashMap();
				for (Object city : cityLinkMap.keySet()) {
					String cityName = (String) city;
					String urlCity = (String) cityLinkMap.get(cityName);
					HashMap countyLinkMap = ParseUtis.getCountyLinkMap(urlCity);

					HashMap countyMap = new HashMap();
					for (Object county : countyLinkMap.keySet()) {
						String countyName = (String) county;
						String urlCounty = (String) countyLinkMap.get(countyName);
						HashMap townLinkMap = ParseUtis.getTownLinkMap(urlCounty);

						HashMap townMap = new HashMap();
						ArrayList villageNameList = null;
						for (Object town : townLinkMap.keySet()) {
							String townName = (String) town;
							String urlTown = (String) townLinkMap.get(townName);
							villageNameList = ParseUtis.getVillageNameList(urlTown);// villageList2Map
							townMap.put(townName, villageNameList);
						}
//						try {
//							Thread.currentThread().sleep(1000);
//						} catch (InterruptedException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
						countyMap.put(countyName, townMap);
					}
					cityMap.put(cityName, countyMap);
				}
				provMapper.put(provName, cityMap);
				writeInFile(provMapper);
				updateProvProperty(provName, urlProv, "1");
			}
			provMapper.clear();
		}
	}

	private static void writeInFile(Map provMapper) {
		String outputDir = "XXX\\province_city_county_town_village.txt";//your path

		StringBuilder stringBuilder = new StringBuilder();

		for (Object province : provMapper.keySet()) {
			String provName = (String) province;
			stringBuilder.append(provName + "\n");
			HashMap cityMap = (HashMap) provMapper.get(provName);
			for (Object city : cityMap.keySet()) {
				String cityName = (String) city;
				stringBuilder.append("		" + cityName + "\n");
				HashMap countyMap = (HashMap) cityMap.get(cityName);
				for (Object county : countyMap.keySet()) {
					String countyName = (String) county;
					// System.out.println(countyName);
					stringBuilder.append("			" + countyName + "\n");
					HashMap townMap = (HashMap) countyMap.get(countyName);
					for (Object town : townMap.keySet()) {
						String townName = (String) town;
						// System.out.println(townName);
						stringBuilder.append("				" + townName + "\n");
						ArrayList villageNameList = (ArrayList) townMap.get(townName);
						for (Object village : villageNameList) {
							// System.out.println(village);
							stringBuilder.append("					" + (String) village + "\n");
						}
					}
				}
			}
		}

		File file = new File(outputDir);

		FileOutputStream o = null;
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			o = new FileOutputStream(file, true);
			o.write(stringBuilder.toString().getBytes("UTF8"));
			o.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
