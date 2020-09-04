package kr.co.bizframe.bert.manager.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import kr.co.bizframe.bert.manager.utils.Strings;

@Configuration
@EnableWebMvc
@ComponentScan("kr.co.bizframe.bert.manager.controller")
public class WebMvcConfig extends WebMvcConfigurerAdapter {

	private static Logger logger = LoggerFactory.getLogger(WebMvcConfig.class);

	@Autowired
	private Environment env;
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		String location = Strings.trim(env.getProperty("resource.location"), "resources");
		registry.addResourceHandler("/**").addResourceLocations("/" + location + "/");
	}

	@Bean
	public ViewResolver jstlViewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setViewClass(JstlView.class);
		viewResolver.setPrefix("");
		viewResolver.setSuffix(".jsp");
		return viewResolver;
	}

	@Bean("messageSource")
	public MessageSource messageSource() {		

		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename(Strings.trim(env.getProperty("spring.messages.basename"), "/WEB-INF/i18n/message"));
		messageSource.setDefaultEncoding(Strings.trim(env.getProperty("spring.messages.encoding"), "UTF-8"));
		messageSource.setCacheSeconds(4);
		// 메시지 로딩에 문제가 생기면 키 값 자체를 표시하는 옵션
		//messageSource.setUseCodeAsDefaultMessage(true);
		return messageSource;
	}
	
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOrigins("*")
				.allowCredentials(false)
				.maxAge(3600);
	}

}