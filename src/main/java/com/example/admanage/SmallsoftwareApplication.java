package com.example.admanage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.SessionCookieConfig;
import javax.servlet.SessionTrackingMode;
import java.util.Collections;

@SpringBootApplication
public class SmallsoftwareApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(SmallsoftwareApplication.class);
	}

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		super.onStartup(servletContext);

		// This will set to use COOKIE only
		servletContext.setSessionTrackingModes(
				Collections.singleton(SessionTrackingMode.COOKIE)
		);
		// This will prevent any JS on the page from accessing the
		// cookie - it will only be used/accessed by the HTTP transport
		// mechanism in use
		SessionCookieConfig sessionCookieConfig =
				servletContext.getSessionCookieConfig();
		sessionCookieConfig.setHttpOnly(true);
	}
	public static void main(String[] args) {
		SpringApplication.run(SmallsoftwareApplication.class, args);
	}
}
