package com.barablah.web.response;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

@Data
public class GetPointLogsResponse {

    private int points;

    private List<PointLogDto> logs = Lists.newArrayList();

}
