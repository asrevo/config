package org.revo.Config.Domain;

import lombok.Getter;
import lombok.Setter;

import java.net.URI;
import java.net.URISyntaxException;

@Getter
@Setter
public class Credentials {
    private String uri;
    private String sslUri;
    private String http_api_uri;
    private String accessKey;
    private String secretKey;
    private String username;
    private String password;
    private String encoded_password;
    private String hostname;
    private String path;
    private int port;


    URI init() {
        try {
            if (this.uri != null) return new URI(this.uri);
        } catch (URISyntaxException e) {
        }
        return null;
    }


    public String getUsername() {
        URI init = init();
        if (init != null) {
            return init.getUserInfo().split(":")[0];
        }
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        URI init = init();
        if (init != null) {
            return init.getUserInfo().split(":")[1];
        }
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHostname() {
        URI init = init();
        if (init != null) {
            return init.getHost();
        }
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getPath() {
        URI init = init();
        if (init != null && init.getPath().length() > 0) {
            return init.getPath().substring(1);
        }
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getPort() {
        URI init = init();
        if (init != null) return init.getPort();
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
