package net.thumbtack.onlineshop.controller;

import net.thumbtack.onlineshop.entities.Category;
import net.thumbtack.onlineshop.entities.Person;
import net.thumbtack.onlineshop.entities.Product;
import net.thumbtack.onlineshop.entities.PurchaseHistory;
import net.thumbtack.onlineshop.repos.CategoryRepository;
import net.thumbtack.onlineshop.repos.PersonRepository;
import net.thumbtack.onlineshop.repos.ProductRepository;
import net.thumbtack.onlineshop.repos.PurchaseHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

class InitSummaryList {

    @Autowired
    private PersonRepository personRepo;
    @Autowired
    private CategoryRepository categoryRepo;
    @Autowired
    private ProductRepository productRepo;
    @Autowired
    private PurchaseHistoryRepository purchaseRepo;

    Person customer;
    Person customer1;
    Category category;
    Category category1;
    Product product;
    Product product1;
    Product product2;
    Product product3;
    Product product4;
    Product product5;
    private Date date;
    private Date date1;

    private PurchaseHistory purchase;
    private PurchaseHistory purchase1;
    private PurchaseHistory purchase2;
    private PurchaseHistory purchase3;
    private PurchaseHistory purchase4;
    private PurchaseHistory purchase5;
    private PurchaseHistory purchase6;
    private PurchaseHistory purchase7;
    private PurchaseHistory purchase8;
    private PurchaseHistory purchase9;
    private PurchaseHistory purchase10;
    private PurchaseHistory purchase11;
    private PurchaseHistory purchase12;
    private PurchaseHistory purchase13;
    private PurchaseHistory purchase14;
    private PurchaseHistory purchase15;
    private PurchaseHistory purchase16;

    long totalSum;

    {
        customer = new Person("Федор", "Иванов", "qwert", "sddsvwe34s");
        customer1 = new Person("Федор", "Иванов", "qwer", "sddsvwe34s");
        category = new Category("Фотоаппараты");
        category1 = new Category("Продукты");
        product = new Product("Nikon D700", 80000, 5);
        product1 = new Product("Canon Mark 2", 120000, 5);
        product2 = new Product("Sony A-7S", 90000, 5);
        product3 = new Product("Сыр", 800, 500);
        product4 = new Product("Апельсин", 10, 500);
        product5 = new Product("Хлеб", 25, 500);
        date = new Date();
        Calendar time = Calendar.getInstance();
        time.setTime(new Date());
        time.add(Calendar.HOUR_OF_DAY, -(3 * 24));
        date1 = time.getTime();
    }

    void initDatabase() {
        customer = personRepo.save(customer);
        customer1 = personRepo.save(customer1);

        category = categoryRepo.save(category);
        category1 = categoryRepo.save(category1);
        List<Category> categories = new ArrayList<>();
        categories.add(category);

        product.setCategories(categories);
        product1.setCategories(categories);
        product2.setCategories(categories);
        product = productRepo.save(product);
        product1 = productRepo.save(product1);
        product2 = productRepo.save(product2);

        categories = new ArrayList<>();
        categories.add(category);
        categories.add(category1);
        product3.setCategories(categories);
        product3 = productRepo.save(product3);
        categories = new ArrayList<>();
        categories.add(category1);
        product4.setCategories(categories);
        product5.setCategories(categories);
        product4 = productRepo.save(product4);
        product5 = productRepo.save(product5);

        categories = new ArrayList<>(product.getCategories());
        purchase = new PurchaseHistory(categories, product,
                customer, date, product.getName(), product.getPrice(), 1);
        purchase.setTotal(purchase.getPrice() * purchase.getCount());
        purchaseRepo.save(purchase);
        purchase1 = new PurchaseHistory(categories, product,
                customer, date1, product.getName(), product.getPrice(), 1);
        purchase1.setTotal(purchase1.getPrice() * purchase1.getCount());
        purchaseRepo.save(purchase1);
        categories = new ArrayList<>(product1.getCategories());
        purchase2 = new PurchaseHistory(categories, product1,
                customer, date, product1.getName(), product1.getPrice(), 1);
        purchase2.setTotal(purchase2.getPrice() * purchase2.getCount());
        purchaseRepo.save(purchase2);
        categories = new ArrayList<>(product2.getCategories());
        purchase3 = new PurchaseHistory(categories, product2,
                customer, date, product2.getName(), product2.getPrice(), 3);
        purchase3.setTotal(purchase3.getPrice() * purchase3.getCount());
        purchaseRepo.save(purchase3);
        categories = new ArrayList<>(product3.getCategories());
        purchase4 = new PurchaseHistory(categories, product3,
                customer, date1, product3.getName(), product3.getPrice(), 6);
        purchase4.setTotal(purchase4.getPrice() * purchase4.getCount());
        purchaseRepo.save(purchase4);
        categories = new ArrayList<>(product4.getCategories());
        purchase5 = new PurchaseHistory(categories, product4,
                customer, date, product4.getName(), product4.getPrice(), 6);
        purchase5.setTotal(purchase5.getPrice() * purchase5.getCount());
        purchaseRepo.save(purchase5);
        purchase6 = new PurchaseHistory(categories, product4,
                customer, date, product4.getName(), product4.getPrice(), 12);
        purchase6.setTotal(purchase6.getPrice() * purchase6.getCount());
        purchaseRepo.save(purchase6);
        categories = new ArrayList<>(product5.getCategories());
        purchase7 = new PurchaseHistory(categories, product5,
                customer, date, product5.getName(), product5.getPrice(), 10);
        purchase7.setTotal(purchase7.getPrice() * purchase7.getCount());
        purchaseRepo.save(purchase7);
        purchase8 = new PurchaseHistory(categories, product5,
                customer, date1, product5.getName(), product5.getPrice(), 10);
        purchase8.setTotal(purchase8.getPrice() * purchase8.getCount());
        purchaseRepo.save(purchase8);

        categories = new ArrayList<>(product.getCategories());
        purchase9 = new PurchaseHistory(categories, product,
                customer1, date, product.getName(), product.getPrice(), 3);
        purchase9.setTotal(purchase9.getPrice() * purchase9.getCount());
        purchaseRepo.save(purchase9);
        categories = new ArrayList<>(product.getCategories());
        purchase10 = new PurchaseHistory(categories, product,
                customer1, date1, product.getName(), product.getPrice(), 1);
        purchase10.setTotal(purchase10.getPrice() * purchase10.getCount());
        purchaseRepo.save(purchase10);
        categories = new ArrayList<>(product1.getCategories());
        purchase11 = new PurchaseHistory(categories, product1,
                customer1, date, product1.getName(), product1.getPrice(), 2);
        purchase11.setTotal(purchase11.getPrice() * purchase11.getCount());
        purchaseRepo.save(purchase11);
        categories = new ArrayList<>(product2.getCategories());
        purchase12 = new PurchaseHistory(categories, product2,
                customer1, date, product2.getName(), product2.getPrice(), 4);
        purchase12.setTotal(purchase12.getPrice() * purchase12.getCount());
        purchaseRepo.save(purchase12);
        categories = new ArrayList<>(product3.getCategories());
        purchase13 = new PurchaseHistory(categories, product3,
                customer1, date, product3.getName(), product3.getPrice(), 12);
        purchase13.setTotal(purchase13.getPrice() * purchase13.getCount());
        purchaseRepo.save(purchase13);
        categories = new ArrayList<>(product4.getCategories());
        purchase14 = new PurchaseHistory(categories, product4,
                customer1, date1, product4.getName(), product4.getPrice(), 12);
        purchase14.setTotal(purchase14.getPrice() * purchase14.getCount());
        purchaseRepo.save(purchase14);
        purchase15 = new PurchaseHistory(categories, product4,
                customer1, date, product4.getName(), product4.getPrice(), 20);
        purchase15.setTotal(purchase15.getPrice() * purchase15.getCount());
        purchaseRepo.save(purchase15);
        categories = new ArrayList<>(product5.getCategories());
        purchase16 = new PurchaseHistory(categories, product5,
                customer1, date, product5.getName(), product5.getPrice(), 4);
        purchase16.setTotal(purchase16.getPrice() * purchase16.getCount());
        purchaseRepo.save(purchase16);
        totalSum = purchase.getTotal() + purchase1.getTotal() + purchase2.getTotal() +
                purchase3.getTotal() + purchase4.getTotal() + purchase5.getTotal() +
                purchase6.getTotal() +  purchase7.getTotal() +  purchase8.getTotal() +
                purchase9.getTotal() +  purchase10.getTotal() +  purchase11.getTotal() +
                purchase12.getTotal() + purchase13.getTotal() + purchase14.getTotal() +
                purchase15.getTotal() + purchase16.getTotal();
    }
}