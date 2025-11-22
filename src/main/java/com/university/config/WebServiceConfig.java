package com.university.config;

import com.university.soap.impl.*;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.xml.ws.Endpoint;

@Configuration
public class WebServiceConfig {

    @Autowired
    private Bus bus;

    @Bean
    public Endpoint etudiantEndpoint() {
        EndpointImpl endpoint = new EndpointImpl(bus, new EtudiantServiceImpl());
        endpoint.publish("/EtudiantService");
        return endpoint;
    }

    @Bean
    public Endpoint enseignantEndpoint() {
        EndpointImpl endpoint = new EndpointImpl(bus, new EnseignantServiceImpl());
        endpoint.publish("/EnseignantService");
        return endpoint;
    }

    @Bean
    public Endpoint coursEndpoint() {
        EndpointImpl endpoint = new EndpointImpl(bus, new CoursServiceImpl());
        endpoint.publish("/CoursService");
        return endpoint;
    }
}