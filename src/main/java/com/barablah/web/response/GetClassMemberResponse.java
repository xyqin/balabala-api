package com.barablah.web.response;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

@Data
public class GetClassMemberResponse {

    private Long id;

    private String nickname;

    private String avatar;

    private String phoneNumber;

    private int points;

    private List<CommentDto> comments = Lists.newArrayList();

}
