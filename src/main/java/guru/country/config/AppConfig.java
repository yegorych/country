package guru.country.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.country.service.CountryErrorAttributes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

import java.text.SimpleDateFormat;

@Configuration
@EnableWs
public class AppConfig {
    public static final String SOAP_NAMESPACE = "countryNS";

    @Value("${api.version}")
    private String apiVersion;

    @Value("${country.base-uri}")
    private String countryUserdataBaseUri;

    @Bean
    public ObjectMapper objectMapper(){
        ObjectMapper om  = new ObjectMapper();
        om.setDateFormat(new SimpleDateFormat("dd-MM-yyyy"));
        return om;
    }

    @Bean
    public ErrorAttributes errorAttributes() {
        return new CountryErrorAttributes(apiVersion);
    }


    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext applicationContext) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean<>(servlet, "/ws/*");
    }

    @Bean(name = "country")
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema userdataSchema) {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("CountryPort");
        wsdl11Definition.setLocationUri(countryUserdataBaseUri + "/ws");
        wsdl11Definition.setTargetNamespace(SOAP_NAMESPACE);
        wsdl11Definition.setSchema(userdataSchema);
        return wsdl11Definition;
    }

    @Bean
    public XsdSchema photocatalogSchema() {
        return new SimpleXsdSchema(new ClassPathResource("country.xsd"));
    }
}
