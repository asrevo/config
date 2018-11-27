package org.revo.Domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;
import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

/**
 * Created by ashraf on 17/04/17.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseUser {
    @NotBlank
    private String email;
    @JsonProperty(access = WRITE_ONLY)
    @NotBlank
    private String password;

    @Override
    @JsonProperty(access = READ_ONLY)
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }


}