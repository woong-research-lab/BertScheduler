package kr.co.bizframe.test;

import java.io.FileReader;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import kr.co.bizframe.bert.manager.model.api.KorquadInput;

public class JsonParse {

	public static void main(String[] args) throws Throwable {
	
		String fileName = "C:/monitoring/bizframe-bert_mng/misc/bert/simple.json";
		JsonParser p = new JsonParser();
		JsonReader file = new JsonReader(new FileReader(fileName));
		JsonObject obj = p.parse(file).getAsJsonObject();
		String jsonString = obj.toString();
		Gson gson = new Gson();
		KorquadInput input = gson.fromJson(jsonString, KorquadInput.class);
		System.out.println(input.getData());
		//System.out.println(jsonString);
		
	}
}
