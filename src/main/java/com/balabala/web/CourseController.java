package com.balabala.web;

import com.balabala.domain.*;
import com.balabala.repository.BalabalaCourseCategoryMapper;
import com.balabala.repository.BalabalaCourseMapper;
import com.balabala.repository.BalabalaTextbookCategoryMapper;
import com.balabala.repository.BalabalaTextbookMapper;
import com.balabala.repository.example.BalabalaCourseCategoryExample;
import com.balabala.repository.example.BalabalaCourseExample;
import com.balabala.repository.example.BalabalaTextbookCategoryExample;
import com.balabala.repository.example.BalabalaTextbookExample;
import com.balabala.web.response.*;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@Slf4j
@Api(tags = "课程", description = "课程相关接口")
@RestController
public class CourseController {

    @Autowired
    private BalabalaCourseMapper courseMapper;

    @Autowired
    private BalabalaCourseCategoryMapper courseCategoryMapper;

    @Autowired
    private BalabalaTextbookCategoryMapper textbookCategoryMapper;

    @Autowired
    private BalabalaTextbookMapper textbookMapper;

    @ApiOperation(value = "获取课程分类列表")
    @GetMapping(value = "/courses/categories")
    public ApiEntity<List<CourseCategoryDto>> getCourseCategories() {
        BalabalaCourseCategoryExample example = new BalabalaCourseCategoryExample();
        example.createCriteria().andDeletedEqualTo(Boolean.FALSE);
        List<BalabalaCourseCategory> categories = courseCategoryMapper.selectByExample(example);
        List<CourseCategoryDto> response = Lists.newArrayList();

        for (BalabalaCourseCategory category : categories) {
            CourseCategoryDto dto = new CourseCategoryDto();
            dto.setId(category.getId());
            dto.setName(category.getCategoryName());
            response.add(dto);
        }

        return new ApiEntity<>(response);
    }


    @ApiOperation(value = "获取课程列表")
    @GetMapping(value = "/courses")
    public ApiEntity<List<CourseDto>> getCourses(@RequestParam Long categoryId) {
        BalabalaCourseExample example = new BalabalaCourseExample();
        example.createCriteria()
                .andCategoryIdEqualTo(categoryId)
                .andDeletedEqualTo(Boolean.FALSE);
        List<BalabalaCourse> courses = courseMapper.selectByExample(example);
        List<CourseDto> response = Lists.newArrayList();

        for (BalabalaCourse course : courses) {
            CourseDto dto = new CourseDto();
            dto.setId(course.getId());
            dto.setName(course.getCourseName());
            response.add(dto);
        }

        return new ApiEntity<>(response);
    }

    @ApiOperation(value = "根据父id获取教材分类列表")
    @GetMapping(value = "/textbooks/categories")
    public ApiEntity<List<TextbookCategoryDto>> getTextbookCategory(@RequestParam(defaultValue = "0") Long parentId) {
        BalabalaTextbookCategoryExample example = new BalabalaTextbookCategoryExample();
        example.createCriteria()
                .andParentIdEqualTo(parentId)
                .andDeletedEqualTo(Boolean.FALSE);
        List<BalabalaTextbookCategory> categories = textbookCategoryMapper.selectByExample(example);
        List<TextbookCategoryDto> response = Lists.newArrayList();

        for (BalabalaTextbookCategory category : categories) {
            TextbookCategoryDto dto = new TextbookCategoryDto();
            dto.setId(category.getId());
            dto.setName(category.getCategoryName());
            response.add(dto);
        }

        return new ApiEntity(response);
    }


    @ApiOperation(value = "根据分类id获取教材题目列表")
    @GetMapping(value = "/textbooks")
    public ApiEntity<GetTextbooksResponse> getTextbooks(@RequestParam Long categoryId) {
        BalabalaTextbookExample example = new BalabalaTextbookExample();
        example.createCriteria()
                .andCategoryIdEqualTo(categoryId)
                .andDeletedEqualTo(Boolean.FALSE);
        List<BalabalaTextbook> textbooks = textbookMapper.selectByExample(example);
        GetTextbooksResponse response = new GetTextbooksResponse();

        for (BalabalaTextbook textbook : textbooks) {
            TextbookDto dto = new TextbookDto();
            dto.setId(textbook.getId());
            dto.setName(textbook.getTextbookName());

            if (TextbookType.CHOICE.equals(textbook.getType())) {
                response.getChoices().add(dto);
            } else if (TextbookType.FILLIN.equals(textbook.getType())) {
                response.getFillins().add(dto);
            } else if (TextbookType.LISTEN.equals(textbook.getType())) {
                response.getListens().add(dto);
            } else if (TextbookType.SENTENCE.equals(textbook.getType())) {
                response.getSentences().add(dto);
            } else if (TextbookType.CONNECT.equals(textbook.getType())) {
                response.getConnects().add(dto);
            } else if (TextbookType.WORD.equals(textbook.getType())) {
                response.getWords().add(dto);
            } else if (TextbookType.PICTURE.equals(textbook.getType())) {
                response.getPictures().add(dto);
            } else if (TextbookType.ARTICLE.equals(textbook.getType())) {
                response.getArticle().add(dto);
            }
        }

        return new ApiEntity<>(response);
    }

    @ApiOperation(value = "获取教材题目详情")
    @GetMapping(value = "/textbooks/{id}")
    public ApiEntity<GetTextbookResponse> getTextbook(@PathVariable Long id) {
        BalabalaTextbook textbook = textbookMapper.selectByPrimaryKey(id);

        if (Objects.isNull(textbook)) {
            return new ApiEntity<>(ApiStatus.STATUS_404);
        }

        GetTextbookResponse response = new GetTextbookResponse();
        response.setId(textbook.getId());
        response.setName(textbook.getTextbookName());
        response.setType(textbook.getType().name());
        response.setQuestion(textbook.getQuestion());
        response.setOption(textbook.getOption());
        response.setCorrect(textbook.getCorrect());
        response.setImage(textbook.getImage());
        response.setVideo(textbook.getVideo());
        return new ApiEntity<>(response);
    }

}
