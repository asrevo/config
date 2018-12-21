package org.revo.Config.Domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Getter
@Setter
@Slf4j
public class VCAP {

    @JsonProperty("user-provided")
    private User_provided[] user_provided;
    private List<Mlab> mlab;

    private List<Searchly> searchly;

    private List<Rediscloud> rediscloud;

    private List<Cloudamqp> cloudamqp;


    public VCAP filterFor(String app) {
        VCAP vcap = new VCAP();
        Predicate<Base> predicate = it -> it.getInstance_name().toLowerCase().startsWith(app) || it.getInstance_name().toLowerCase().startsWith("all");
        vcap.setMlab(getMlab().stream().filter(predicate).collect(Collectors.toList()));


        vcap.setSearchly(getSearchly().stream().filter(predicate).collect(Collectors.toList()));
        log.info("filterFor " + app);
        log.info("filterFor searchly size" + getSearchly().size());
        log.info("filterFor searchly list" + getSearchly().stream().map(it -> it.getName()).collect(Collectors.toList()));
        log.info("filterFor searchly result" + getSearchly().stream().filter(predicate).collect(Collectors.toList()));

        vcap.setRediscloud(getRediscloud().stream().filter(predicate).collect(Collectors.toList()));
        vcap.setCloudamqp(getCloudamqp().stream().filter(predicate).collect(Collectors.toList()));
        return vcap;
    }
}
