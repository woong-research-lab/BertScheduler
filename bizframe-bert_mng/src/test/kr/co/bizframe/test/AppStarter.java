package kr.co.bizframe.test;


import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AppStarter {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.scan("kr.co.bizframe.bert");
		ctx.refresh();
		
		/*ctx.registerShutdownHook();
		ctx.close();*/
	}
}
