package net.thumbtack.onlineshop.dto.Request;

import org.springframework.util.StringUtils;

import javax.validation.constraints.AssertTrue;

public class CategoryDtoRequest {

    private String name;
    private Long parentId;

    public CategoryDtoRequest(String name, long parentId) {
        this.name = name;
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    @AssertTrue(message = "Category name can't be empty")
    public boolean isName() {
        return !StringUtils.isEmpty(name);
    }
}
