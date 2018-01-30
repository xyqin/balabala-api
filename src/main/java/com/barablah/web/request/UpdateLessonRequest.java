package com.barablah.web.request;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class UpdateLessonRequest {

    @NotBlank
    private String video;

}
