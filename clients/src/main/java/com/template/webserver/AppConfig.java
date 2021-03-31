package com.template.webserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.corda.client.jackson.JacksonSupport;
import net.corda.client.rpc.CordaRPCClient;
import net.corda.core.messaging.CordaRPCOps;
import net.corda.core.utilities.NetworkHostAndPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Not really sure how this works just yet. It is needed to connect with a @RestController to pass data and stuff
 * - Charlie Nguyen
 */
@Configuration
public class AppConfig implements WebMvcConfigurer {

//    @Value("${patient1.host}")
    private String patient1HostAndPort;

//    @Value("${doctor1.host}")
    private String doctor1HostAndPort;

//    @Value("${employer1.host}")
    private String employer1HostAndPort;

//    @Value("${clinicAdmin1.host}")
    private String clinicAdmin1HostAndPort;

    @Bean(destroyMethod = "")  // Avoids node shutdown on rpc disconnect
    public CordaRPCOps patient1Proxy(){
        //CordaRPCClient patient1Client = new CordaRPCClient(NetworkHostAndPort.parse(patient1HostAndPort));
        CordaRPCClient patient1Client = new CordaRPCClient(NetworkHostAndPort.parse("localhost:10006"));
        return patient1Client.start("user1", "test").getProxy();
    }

    @Bean(destroyMethod = "")
    public CordaRPCOps doctor1Proxy(){
        //CordaRPCClient doctor1Client = new CordaRPCClient(NetworkHostAndPort.parse(doctor1HostAndPort));
        CordaRPCClient doctor1Client = new CordaRPCClient(NetworkHostAndPort.parse("localhost:10009"));
        return doctor1Client.start("user1", "test").getProxy();
    }

    @Bean(destroyMethod = "")
    public CordaRPCOps employer1Proxy(){
//        CordaRPCClient employer1Client = new CordaRPCClient(NetworkHostAndPort.parse(employer1HostAndPort));
        CordaRPCClient employer1Client = new CordaRPCClient(NetworkHostAndPort.parse("localhost:10007"));

        return employer1Client.start("user1", "test").getProxy();
    }

    @Bean(destroyMethod = "")
    public CordaRPCOps clinicAdmin1Proxy(){
        //CordaRPCClient employer1Client = new CordaRPCClient(NetworkHostAndPort.parse(clinicAdmin1HostAndPort));
        CordaRPCClient clinic1Client = new CordaRPCClient(NetworkHostAndPort.parse("localhost:10010"));
        return clinic1Client.start("ClinicAdmin1", "test").getProxy();
    }

    /**
     * Corda Jackson Support, to convert corda objects to json
     */
    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(){
        ObjectMapper mapper =  JacksonSupport.createDefaultMapper(patient1Proxy());
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(mapper);
        return converter;
    }
}
