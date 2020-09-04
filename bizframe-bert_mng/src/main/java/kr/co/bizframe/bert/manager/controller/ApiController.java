package kr.co.bizframe.bert.manager.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.bizframe.bert.manager.handler.ElasticsearchHandler;
import kr.co.bizframe.bert.manager.model.api.KorquadInput;

@Controller
public class ApiController {
	
	private static Logger logger = LoggerFactory.getLogger(ApiController.class);

	@Autowired
	private ElasticsearchHandler elasticsearchHandler;
	
	// TODO input json 모델이 바뀌면..requestbody 수정필요
	
	@ResponseBody
	@RequestMapping(value = "/api/put-koquad/{action:now|day|month|quarter|year}", method = RequestMethod.POST)
	public Map<String, Object> putTrainingDatas(@PathVariable String action, @RequestBody KorquadInput input, HttpServletRequest request)
			throws Exception {

		Map<String, Object> mapResponse = new HashMap<String, Object>();

		try {
			switch (action) {
			case "now" : 
				break;
				
			}
			
			// 1. 작업 요청 내역 저장
			
			
			// 2. 엘라스틱에 컨텍스트 저장
			elasticsearchHandler.insertContexts(input.getData());
			
			mapResponse.put("result", 1);
		} catch (Throwable e) {
			mapResponse.put("result", 0);
			mapResponse.put("errorMsg", e.getMessage());
			logger.error("putTrainingDatas error " + e.getMessage(), e);
		}

		return mapResponse;
	}

	@ResponseBody
	@RequestMapping(value = "/api/put/{action:now|day|month|quarter|year}", method = RequestMethod.POST)
	public Map<String, Object> putDatas(@PathVariable String action, @RequestBody Map<String, List<String>> input , HttpServletRequest request)
			throws Exception {

		Map<String, Object> mapResponse = new HashMap<String, Object>();

		try {
			switch (action) {
			case "now" : 
				break;
				
			}
			
			// 1. 작업 요청 내역 저장
			
			
			// 2. 엘라스틱에 컨텍스트 저장
			elasticsearchHandler.insertContexts(input);
			
			mapResponse.put("result", 1);
		} catch (Throwable e) {
			mapResponse.put("result", 0);
			mapResponse.put("errorMsg", e.getMessage());
			logger.error("putTrainingDatas error " + e.getMessage(), e);
		}

		return mapResponse;
	}
	
	@ResponseBody
	@RequestMapping(value = "/api/predict", method = RequestMethod.POST)
	public Map<String, Object> putTrainingDatas(@RequestBody String question, HttpServletRequest request)
			throws Exception {

		Map<String, Object> mapResponse = new HashMap<String, Object>();

		try {
			// 1. 지문 찾기
			
			// 2. api호출
			/*http://192.168.10.2:5000/query_data

			{"context": "이것은 쿼리입니다","question": "이것은 무었입니까?"}*/
					
			// 3. json
			
			mapResponse.put("result", 1);

		} catch (Throwable e) {
			mapResponse.put("result", 0);
			mapResponse.put("errorMsg", e.getMessage());
			logger.error("putTrainingDatas error " + e.getMessage(), e);
		}

		return mapResponse;
	}	
}
