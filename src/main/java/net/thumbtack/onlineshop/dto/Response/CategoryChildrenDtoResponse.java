package net.thumbtack.onlineshop.dto.Response;

public class CategoryChildrenDtoResponse extends CategoryParentDtoResponse {

    private long id;
    private String name;
    private long parentId;
    private String parentName;

    public CategoryChildrenDtoResponse(long id, String name, long parentId, String parentName) {
        super(id, name);
        this.parentId = parentId;
        this.parentName = parentName;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }
}
