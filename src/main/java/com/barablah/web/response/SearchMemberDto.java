package com.barablah.web.response;

import lombok.Data;

@Data
public class SearchMemberDto {

    private Long id;

    private String phoneNumber;

    private String nickname;

    private String avatar;

}
