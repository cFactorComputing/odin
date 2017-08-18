package in.cfcomputing.odin.core.services.web.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
@ConfigurationProperties("web")
public class WebConfiguration extends WebMvcConfigurerAdapter {

    private String[] resourceDirectories;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        if (resourceDirectories != null) {
            registry.addResourceHandler("/**").addResourceLocations(resourceDirectories);
        }
    }

    public String[] getResourceDirectories() {
        return resourceDirectories;
    }

    public void setResourceDirectories(String[] resourceDirectories) {
        this.resourceDirectories = resourceDirectories;
    }
}
