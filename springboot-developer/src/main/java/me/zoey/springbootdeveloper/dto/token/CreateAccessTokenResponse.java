package me.zoey.springbootdeveloper.dto.token;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateAccessTokenResponse {

    private String accessToken;

}
