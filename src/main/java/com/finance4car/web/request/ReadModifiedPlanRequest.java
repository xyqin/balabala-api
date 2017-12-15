package com.finance4car.web.request;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class ReadModifiedPlanRequest {

    private Boolean processModified;

    private Boolean actionModified;

}
