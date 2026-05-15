package br.com.senai.autoescola.n116.lessons;

import com.icegreen.greenmail.spring.GreenMailBean;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class LessonTestGreenMailConfig {
	@Bean
	public GreenMailBean greenMailBean() {
		var gmb = new GreenMailBean();
		IO.println(gmb.getUsers());
		return gmb;
	}
}
