package com.balabala.web.response;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

@Data
public class GetMemberHomeResponse {

    private List<PositionContentDto> contents = Lists.newArrayList();

    private List<LessonDto> lessons = Lists.newArrayList();

}
