package com.balabala.web.request;

import com.balabala.web.response.HomeworkItemDto;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class SubmitHomeworkRequest {

    @NotNull
    private List<HomeworkItemDto> items;

}
