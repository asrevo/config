package org.revo.Config.Domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Getter
@Setter
public class VCAP {

    @JsonProperty("user-provided")
    private User_provided[] user_provided;
    private List<Mlab> mlab;

    private List<Searchly> searchly;

    private List<Rediscloud> rediscloud;

    private List<Cloudamqp> cloudamqp;


    public VCAP filterFor(String app) {
        VCAP vcap = new VCAP();
        Predicate<Base> mlabPredicate = it -> it.getInstance_name().toLowerCase().startsWith(app) || it.getInstance_name().toLowerCase().startsWith("all");
        vcap.setMlab(getMlab().stream().filter(mlabPredicate).collect(Collectors.toList()));
        vcap.setSearchly(getSearchly().stream().filter(mlabPredicate).collect(Collectors.toList()));
        vcap.setRediscloud(getRediscloud().stream().filter(mlabPredicate).collect(Collectors.toList()));
        vcap.setCloudamqp(getCloudamqp().stream().filter(mlabPredicate).collect(Collectors.toList()));
        return vcap;
    }
}
