package com.green.greengramver1.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.time.chrono.MinguoDate;

@Getter
@Setter
public class UserSignInRes {
    private long userId;
    private String nickName;
    private String pic;
    @JsonIgnore
    private String upw; //얘는 비교용이라서 안줄꺼라
    @JsonIgnore
    private String Message;
}
