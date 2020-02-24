package net.thumbtack.onlineshop.entities;

import com.fasterxml.jackson.annotation.JsonView;
import net.thumbtack.onlineshop.utils.Views;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_id")
    private Category parentCategory;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "parentCategory")
    private List<Category> childrenCategories = new ArrayList<>();

    @JsonView(Views.Name.class)
    private String name;

    @ManyToMany
    @JoinTable(
            name = "product_category",
            joinColumns = {@JoinColumn(name = "category_id")},
            inverseJoinColumns = {@JoinColumn(name = "product_id")}
    )
    private List<Product> products = new ArrayList<>();

    public Category() {
    }

    public Category(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Category getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(Category parentCategory) {
        this.parentCategory = parentCategory;
    }

    public List<Category> getChildrenCategories() {
        return childrenCategories;
    }

    public void setChildrenCategories(List<Category> childrenCategories) {
        this.childrenCategories = childrenCategories;
    }

    public void addChildrenCategory(Category childrenCategory) {
        childrenCategories.add(childrenCategory);
    }

    public Category getChildrenCategory(String name) {
        for (Category item : childrenCategories) {
            if (item.getName().equals(name))
                return item;
        }
        return null;
    }

    public void delChildrenCategory(Category childrenCategory) {
        childrenCategories.remove(childrenCategory);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category)) return false;
        Category category = (Category) o;
        return Objects.equals(getId(), category.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return String.format("Category: [id=%s mame=%s]", id,
                name);
    }
}
