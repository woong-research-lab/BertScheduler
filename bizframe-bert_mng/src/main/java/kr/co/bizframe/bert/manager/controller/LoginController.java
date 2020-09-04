package kr.co.bizframe.bert.manager.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.bizframe.bert.manager.model.api.Login;
import kr.co.bizframe.bert.manager.type.Constants;
import kr.co.bizframe.bert.manager.utils.HashEncryption;
import kr.co.bizframe.bert.manager.utils.WebUtils;

/**
 * Login
 * @author bumma
 *
 */
@Controller
public class LoginController {

	private static Logger logger = LoggerFactory.getLogger(LoginController.class);

	@ResponseBody
	@RequestMapping(value = "/api/login", method = RequestMethod.POST)
	public Map<String, Object> login(@RequestBody Login vo, HttpServletRequest request) throws Exception {
		logger.info(vo.toString());
		HttpSession session = request.getSession();
		String userid = vo.getLoginid();
		String pass = vo.getLoginpw();

		Map<String, Object> mapResponse = new HashMap<String, Object>();
		if (userid == null) {
			mapResponse.put("result", "101");
			WebUtils.logout(session);
			return mapResponse;

		}

		if (pass == null) {
			mapResponse.put("result", "102");
			WebUtils.logout(session);
			return mapResponse;
		}

		if (!Constants.ADMIN_USER_ID.equals(userid)) {
			mapResponse.put("result", "101");
			WebUtils.logout(session);
		}

		if (!HashEncryption.getInstance().encryptSHA1(pass).equals(Constants.ADMIN_USER_PASS)) {
			mapResponse.put("result", "103");
			return mapResponse;
		}

		mapResponse.put("userId", userid);
		WebUtils.login(session, userid, pass);
		mapResponse.put("result", "001");

		logger.debug("result = " + mapResponse);
		return mapResponse;
	}

	@ResponseBody
	@RequestMapping(value = "/api/logout", method = RequestMethod.GET)
	public Map<String, Object> logout(HttpServletRequest request, HttpServletResponse reponse) throws Exception {
		HttpSession session = request.getSession();
		WebUtils.logout(session);

		Map<String, Object> mapResponse = new HashMap<String, Object>();
		mapResponse.put("result", "001");
		return mapResponse;
	}

}
