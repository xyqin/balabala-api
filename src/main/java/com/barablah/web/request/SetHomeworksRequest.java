package com.barablah.web.request;

import com.google.common.collect.Lists;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
public class SetHomeworksRequest {

    @NotNull
    private Long classId;

    @NotNull
    private String name;

    @NotNull
    private Date closingAt;

    private List<Long> textbookIds = Lists.newArrayList();

}
