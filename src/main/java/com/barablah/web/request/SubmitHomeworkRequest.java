package com.barablah.web.request;

import com.barablah.web.response.HomeworkItemDto;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class SubmitHomeworkRequest {

    @NotNull
    private List<HomeworkItemDto> items;

}
