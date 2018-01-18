package com.barablah.web.request;

import lombok.Data;

@Data
public class MakeCommentRequest {

    private Long memberId;

    private String content;

}
