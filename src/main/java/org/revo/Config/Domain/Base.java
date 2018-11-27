package org.revo.Config.Domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Base {
    private List<String> tags;

    private String plan;

    private String name;

    private List<String> volume_mounts;

    private Credentials credentials;

    private String provider;

    private String label;

    private String instance_name;

    private String syslog_drain_url;

    private String binding_name;
}
