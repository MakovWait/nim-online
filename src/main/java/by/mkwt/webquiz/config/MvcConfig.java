package by.mkwt.webquiz.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");

        registry.addViewController("/").setViewName("lobby");
        registry.addViewController("/index").setViewName("index");
        registry.addViewController("/lobby").setViewName("lobby");
        registry.addViewController("/personal").setViewName("personal");

        registry.addViewController("/room").setViewName("room");
    }

}
