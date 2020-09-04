package kr.co.bizframe.bert.manager.utils;

import java.io.File;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import kr.co.bizframe.bert.manager.type.Constants;

@Component
public class WebUtils {

	private static Logger logger = LoggerFactory.getLogger(WebUtils.class);

	@Value("${file.save.dir}")
	private String dir;

	public String getDir() {
		return dir;
	}

	public static void login(HttpSession session, String userId, String passwd) {
		session.setMaxInactiveInterval(99999999);
		setSessionValue(session, userId, passwd, "true");
	}

	public static void logout(HttpSession session) {
		setSessionValue(session, null, null, null);
		session.invalidate();
	}

	private static void setSessionValue(HttpSession session, String userId, String passwd, String status) {
		session.setAttribute(Constants.SESSION_USERID, userId);
		session.setAttribute(Constants.SESSION_PASSWD, passwd);
		session.setAttribute(Constants.SESSION_STATUS, status);
	}

	public String getMessageFilePath() throws Throwable {
		String currentString = TimeUtil.getCurrentDateTime("yyyyMMdd");

		StringBuffer sb = new StringBuffer();
		sb.append(currentString.substring(0, 4));
		sb.append("/");
		sb.append(currentString.substring(4, 6));
		sb.append("/");
		sb.append(currentString.substring(6, 8));

		return getMessageFilePath0(sb.toString());
	}

	public String getMessageFilePath0(String reqRes) throws Throwable {
		File saveDir = getFileSaveDirectory(dir, "message/" + reqRes);
		return saveDir.getAbsolutePath();
	}

	private static File getFileSaveDirectory(String savePath, String folderName) throws Throwable {
		folderName = Strings.trim(folderName);
		if (folderName == null) {
			throw new Exception("Id does not allow Null");
		}
		if (savePath == null) {
			throw new Exception("SavePath does not allow Null");
		}

		File saveDir = new File(savePath, folderName);
		if (!saveDir.exists()) {
			boolean result = saveDir.mkdirs();
			logger.debug(saveDir.getCanonicalPath() + " make directory result = " + result);
		}
		return saveDir;
	}
}
