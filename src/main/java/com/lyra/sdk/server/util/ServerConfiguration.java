package com.lyra.sdk.server.util;

import com.lyra.rest.client.ClientConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

/**
 * Configuration component that reads parameters from environment variables if exist
 *
 * @author Lyra Network
 */
@Configuration
public class ServerConfiguration {
    private final static String ENV_USERNAME = "SDK_SERVER_USERNAME";
    private final static String ENV_PASSWORD = "SDK_SERVER_PASSWORD";
    private final static String ENV_REST_API_SERVER_NAME = "SDK_REST_API_SERVER_NAME";
    private final static String ENV_HASH_KEY = "SDK_HASH_KEY";

    private final Environment env;

    @Autowired
    public ServerConfiguration(Environment env) {
        this.env = env;
    }

    /*
     * Return a client configuration depending of chosen scenario
     */
    public ClientConfiguration getConfiguration() {
        ClientConfiguration.ClientConfigurationBuilder builder = ClientConfiguration.builder();

        if (!StringUtils.isEmpty(env.getProperty(ENV_USERNAME))) {
            builder.username(env.getProperty(ENV_USERNAME));
        }
        if (!StringUtils.isEmpty(env.getProperty(ENV_PASSWORD))) {
            builder.password(env.getProperty(ENV_PASSWORD));
        }
        if (!StringUtils.isEmpty(env.getProperty(ENV_REST_API_SERVER_NAME))) {
            builder.restApiServerName(env.getProperty(ENV_REST_API_SERVER_NAME));
        }
        if (!StringUtils.isEmpty(env.getProperty(ENV_HASH_KEY))) {
            builder.hashKey(env.getProperty(ENV_HASH_KEY));
        }

        return builder.build();
    }
}
