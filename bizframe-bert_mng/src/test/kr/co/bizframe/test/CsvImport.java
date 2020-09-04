package kr.co.bizframe.test;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.opencsv.CSVReader;

public class CsvImport {
	
	public static String rootDir = "C:/monitoring/bizframe-bert_mng/misc/0820도로공사 실무서 작업 데이터/";

	public static void main(String[] args) throws Throwable {
		CsvImport c = new CsvImport();
	    c.makeJsonFile();
	}
	
	
	public void makeJsonFile() throws Throwable {		
		// 1. 
		String path = "암판정 검측대장";
		Map<String, List<String>> datas = csvReader(path);
		Gson gson = new Gson();
		System.out.println(gson.toJson(datas));
	}

	public Map<String, List<String>> csvReader(String path) throws Throwable {
		//CSVReader reader = new CSVReader(new FileReader(rootDir + path), ',');
		FileInputStream fis = new FileInputStream(rootDir + path + ".csv");
		CSVReader reader = new CSVReader(new InputStreamReader(fis,"euc-kr"), ',');
		
		Map<Integer, String> categories = new HashMap<>();
		Map<Integer, List<String>> datas = new HashMap<>();
		List<String[]> records = reader.readAll();
		Iterator<String[]> iterator = records.iterator();
		int j = 0;
		while (iterator.hasNext()) {
			String[] record = iterator.next();
			if (j == 0) {
				// category
				for (int i = 0; i < record.length; i++) {
					String category = record[i];					
					categories.put(i, category);
					datas.put(i, new ArrayList<>());
				}
			} else {
				for (int i = 0; i < record.length; i++) {
					List<String> list = datas.get(i);					
					list.add(record[i]);							
				}	
			}
			j++;
		}
		reader.close();
		fis.close();
		
		Map<String, List<String>> result = new HashMap<>();
		for (int idx : datas.keySet()) { 
			result.put(categories.get(idx), datas.get(idx));
		}
		
		return result;
	}
}
