package com.todo.ui;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;

@SpringBootApplication
@Theme(value = "todos")
public class TodoUiApplication implements AppShellConfigurator {

	public static void main(String[] args) {
		SpringApplication.run(TodoUiApplication.class, args);
	}

}
