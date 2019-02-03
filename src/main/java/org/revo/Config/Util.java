package org.revo.Config;

/*
import com.amazonaws.services.cloudformation.AmazonCloudFormation;
import com.amazonaws.services.cloudformation.model.Export;
import com.amazonaws.services.cloudformation.model.ListExportsRequest;
*/
import com.fasterxml.jackson.databind.ObjectMapper;
import org.revo.Config.Domain.Base;
import org.revo.Config.Domain.VCAP;
import org.revo.Service.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.config.environment.Environment;
import org.springframework.cloud.config.environment.PropertySource;
import org.springframework.cloud.config.server.environment.EnvironmentRepository;
import org.springframework.cloud.config.server.environment.NativeEnvironmentProperties;
import org.springframework.cloud.config.server.environment.NativeEnvironmentRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

@Configuration
public class Util {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Bean
    @Profile("cloudformation")
    EnvironmentRepository nativeEnvironmentRepository(/*@Qualifier("simpPropertySource") PropertySource propertySource,*/ ConfigurableEnvironment configurableEnvironment, NativeEnvironmentProperties nativeEnvironmentRepository) {
        return new NativeEnvironmentRepository(configurableEnvironment, nativeEnvironmentRepository) {
            @Override
            public Environment findOne(String config, String profile, String label) {
                Environment one = super.findOne(config, profile, label);
//                one.addFirst(propertySource);
                configurableEnvironment.getSystemEnvironment().entrySet().stream()
                        .filter(it -> it.getKey().equalsIgnoreCase("VCAP_SERVICES"))
                        .map(Entry::getValue)
                        .map(it -> toVcap(it))
                        .filter(Objects::nonNull)
                        .map(it -> it.filterFor(config))
                        .map(it -> getMap(it))
                        .map(it -> new PropertySource("vcap", it))
                        .forEach(one::addFirst);
                return one;
            }
        };
    }

    private VCAP toVcap(Object it) {
        try {
            return objectMapper.readValue(it.toString(), VCAP.class);
        } catch (IOException e) {
            return null;
        }
    }

    void put(Map<String, Object> source, String key, Object value) {
        if (value != null) source.put(key, value);
    }

    Map<String, Object> loop(Base loop) {
        Map<String, Object> source = new HashMap<>();
        put(source, "vcap.services." + loop.getInstance_name() + ".credentials.uri", loop.getCredentials().getUri());
        put(source, "vcap.services." + loop.getInstance_name() + ".credentials.sslUri", loop.getCredentials().getSslUri());
        put(source, "vcap.services." + loop.getInstance_name() + ".credentials.http_api_uri", loop.getCredentials().getHttp_api_uri());
        put(source, "vcap.services." + loop.getInstance_name() + ".credentials.accessKey", loop.getCredentials().getAccessKey());
        put(source, "vcap.services." + loop.getInstance_name() + ".credentials.secretKey", loop.getCredentials().getSecretKey());
        put(source, "vcap.services." + loop.getInstance_name() + ".credentials.username", loop.getCredentials().getUsername());
        put(source, "vcap.services." + loop.getInstance_name() + ".credentials.password", loop.getCredentials().getPassword());
        put(source, "vcap.services." + loop.getInstance_name() + ".credentials.hostname", loop.getCredentials().getHostname());
        put(source, "vcap.services." + loop.getInstance_name() + ".credentials.path", loop.getCredentials().getPath());
        if (loop.getCredentials().getPort() != -1)
            source.put("vcap.services." + loop.getInstance_name() + ".credentials.port", loop.getCredentials().getPort());
        return source;
    }

    private Map<String, Object> getMap(VCAP value) {
        HashMap<String, Object> source = new HashMap<>();
        for (Base l : value.getMlab()) {
            source.putAll(loop(l));
        }
        for (Base l : value.getCloudamqp()) {
            source.putAll(loop(l));
        }
        for (Base l : value.getRediscloud()) {
            source.putAll(loop(l));
        }
        for (Base l : value.getSearchly()) {
            source.putAll(loop(l));
        }
        return source;
    }

/*
    @Bean("simpPropertySource")
    @Profile("cloudformation")
    public PropertySource propertySource(AmazonCloudFormation amazonCloudFormation, org.springframework.core.env.Environment environment) {
        List<Export> exports = amazonCloudFormation.listExports(new ListExportsRequest()).getExports();
        Map<String, Object> source = loadYouProperties(environment.getProperty("cloud.aws.stack.name"), exports);
        for (String s : Arrays.asList("cloud.aws.credentials.accessKey", "cloud.aws.credentials.secretKey", "org.revo.env.eureka.externalurl")) {
            source.put(s, environment.getProperty(s, ""));
        }
        return new PropertySource("cloudformation", source);
    }


    private Map<String, Object> loadYouProperties(String stackPrefix, List<Export> exports) {

        HashMap<String, Object> stringStringHashMap = new HashMap<>();
        for (Export export : exports) {
            String stack = export.getName().replaceAll(":", ".").toLowerCase();
            if (!stack.startsWith(stackPrefix + ".")) {
                stack = stackPrefix + "." + stack;
            }
            stringStringHashMap.put("org.revo." + stack, export.getValue().toLowerCase());
        }
        return stringStringHashMap;
    }
*/


    @Bean
    public PasswordEncoder encoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserService userService) {
        return s -> userService.findByEmail(s).orElseThrow(() -> new UsernameNotFoundException(s));
    }

    @Bean
    public CommandLineRunner runner(Env env, UserService userService) {
        return args -> env.getUsers().forEach(userService::save);
    }
}
