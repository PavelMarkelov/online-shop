package net.thumbtack.onlineshop.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "purchase_history")
public class PurchaseHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    @JoinTable(
            name = "purchase_category",
            joinColumns = {@JoinColumn(name = "purchase_history_id")},
            inverseJoinColumns = {@JoinColumn(name = "category_id")}
    )
    @JsonView(View.Name.class)
    private List<Category> categories = new ArrayList<>();

    @JoinColumn(name = "product_id")
    @ManyToOne
    @JsonView(View.Id.class)
    private Product product;

    @JoinColumn(name = "person_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonView(View.Id.class)
    private Person person;

    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Europe/Samara")
    @JsonView(View.Data.class)
    private Date purchaseDate;

    @JsonView(View.Data.class)
    private String name;
    @JsonView(View.Data.class)
    private int price;
    @JsonView(View.Data.class)
    private int count;

    @Transient
    @JsonView(View.Data.class)
    private long total;

    public PurchaseHistory() {
    }

    public PurchaseHistory(List<Category> categories, Product product, Person person,
                           Date purchaseDate, String name, int price, int count) {
        this.categories = categories;
        this.product = product;
        this.person = person;
        this.purchaseDate = purchaseDate;
        this.name = name;
        this.price = price;
        this.count = count;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PurchaseHistory)) return false;
        PurchaseHistory history = (PurchaseHistory) o;
        return Objects.equals(getId(), history.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
