package com.balabala.web;

import com.balabala.domain.BalabalaTextbook;
import com.balabala.domain.BalabalaTextbookCategory;
import com.balabala.domain.TextbookType;
import com.balabala.repository.BalabalaTextbookCategoryMapper;
import com.balabala.repository.BalabalaTextbookMapper;
import com.balabala.repository.example.BalabalaTextbookCategoryExample;
import com.balabala.repository.example.BalabalaTextbookExample;
import com.balabala.web.response.GetTextbooksResponse;
import com.balabala.web.response.TextbookCategoryDto;
import com.balabala.web.response.TextbookDto;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Api(tags = "教材", description = "教材")
@RestController
public class TextbookController {

    @Autowired
    private BalabalaTextbookCategoryMapper textbookCategoryMapper;

    @Autowired
    private BalabalaTextbookMapper textbookMapper;

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
    public ApiEntity<GetTextbooksResponse> getTextBooks(@RequestParam Long categoryId) {
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

}
