package com.balabala.web.request;

import com.balabala.web.response.HomeworkItemDto;
import lombok.Data;

import java.util.List;

@Data
public class SubmitHomeworkRequest {

    private List<HomeworkItemDto> items;

}
