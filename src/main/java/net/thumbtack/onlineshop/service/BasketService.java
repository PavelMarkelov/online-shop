package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.dto.Request.BuyProductDtoRequest;
import net.thumbtack.onlineshop.dto.Response.BuyBasketResponseDto;
import net.thumbtack.onlineshop.dto.Response.BuyProductDtoResponse;
import net.thumbtack.onlineshop.entities.Basket;
import net.thumbtack.onlineshop.entities.Person;
import net.thumbtack.onlineshop.entities.Product;
import net.thumbtack.onlineshop.entities.ProductInBasket;
import net.thumbtack.onlineshop.exception.GlobalExceptionErrorCode;
import net.thumbtack.onlineshop.exception.NoMoneyException;
import net.thumbtack.onlineshop.exception.ProductNotFoundException;
import net.thumbtack.onlineshop.repos.BasketRepository;
import net.thumbtack.onlineshop.repos.PersonRepository;
import net.thumbtack.onlineshop.repos.ProductInBasketRepository;
import net.thumbtack.onlineshop.repos.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class BasketService {

    private final BasketRepository basketRepository;
    private final PersonRepository personRepository;
    private final ProductRepository productRepository;
    private final ProductInBasketRepository productInBasketRepository;

    public BasketService(BasketRepository basketRepository,
                         PersonRepository personRepository,
                         ProductRepository productRepository,
                         ProductInBasketRepository productInBasketRepository) {
        this.basketRepository = basketRepository;
        this.personRepository = personRepository;
        this.productRepository = productRepository;
        this.productInBasketRepository = productInBasketRepository;
    }

    public List<BuyProductDtoResponse> addToBasket(
            String login,
            BuyProductDtoRequest request
    ) {
        Product product = getProductWithCheck(request);
        Person person = personRepository.findByLogin(login);
        Basket basket = person.getBasket();
        Optional<ProductInBasket> productExist = basket.getProductInBaskets()
                .stream()
                .filter(item -> item.getProductIdInfo() == (request.getId()))
                .findFirst();
        if (!productExist.isPresent()) {
            ProductInBasket productInBasket = new ProductInBasket(basket, product,
                    product.getId(), product.getName(), product.getPrice(), request.getCount());
            basket.addProduct(productInBasket);
            productInBasketRepository.save(productInBasket);
        }
        return basket.getProductInBaskets()
                .stream()
                .map(item -> new BuyProductDtoResponse(item.getProductIdInfo(),
                        item.getName(), item.getPrice(), item.getCount()))
                .collect(Collectors.toList());
    }

    public List<BuyProductDtoResponse> changingQuantity(
            String login,
            BuyProductDtoRequest request
    ) {
        getProductWithCheck(request);
        Person person = personRepository.findByLogin(login);
        Basket basket = person.getBasket();
        Optional<ProductInBasket> productExist = basket.getProductInBaskets()
                .stream()
                .filter(item -> item.getProductIdInfo() == request.getId())
                .findFirst();
        if (productExist.isPresent() && request.getCount() != productExist.get().getCount()) {
            productExist.get().setCount(request.getCount());
            productInBasketRepository.save(productExist.get());
        }
        return basket.getProductInBaskets()
                .stream()
                .map(item -> new BuyProductDtoResponse(item.getProductIdInfo(),
                        item.getName(), item.getPrice(), item.getCount()))
                .collect(Collectors.toList());
    }

    private Product getProductWithCheck(BuyProductDtoRequest request) {
        Optional<Product> productOpt = productRepository.findById(request.getId());
        if (!productOpt.isPresent())
            throw new ProductNotFoundException(GlobalExceptionErrorCode.PRODUCT_NOT_FOUND);
        Product product = productOpt.get();
        if (!request.getName().equals(product.getName()) || request.getPrice() != product.getPrice())
            throw new ProductNotFoundException(GlobalExceptionErrorCode.BUY_ERROR);
        if (request.getCount() <= 0)
            request.setCount(1);
        return product;
    }

    public void deleteProductFromBasket(String login, Long productId) {
        Person person = personRepository.findByLogin(login);
        Basket basket = person.getBasket();
        Optional<ProductInBasket> productOpt = basket.getProductInBaskets()
                .stream()
                .filter(item -> item.getProductIdInfo() == productId)
                .findFirst();
        basketRepository.save(basket);
        productOpt.ifPresent(product -> {
            basket.delProduct(product);
            productInBasketRepository.deleteById(product.getId());
        });
    }

    @Transactional(readOnly = true)
    public List<BuyProductDtoResponse> findBasketByPersonLogin(String login) {
        Person person = personRepository.findByLogin(login);
        return person.getBasket().getProductInBaskets()
                .stream()
                .map(item -> new BuyProductDtoResponse(item.getProductIdInfo(),
                        item.getName(), item.getPrice(), item.getCount()))
                .collect(Collectors.toList());
    }

    public BuyBasketResponseDto buyingFromBasket(String login,
                                                 List<BuyProductDtoRequest> request) {
        Person person = personRepository.findByLogin(login);
        Basket personBasket = person.getBasket();
        List<BuyProductDtoResponse> bought = new ArrayList<>();
        List<BuyProductDtoResponse> remaining = new ArrayList<>();
        request.forEach(item -> {
            Optional<ProductInBasket> productInBasketOpt = personBasket.getProductInBaskets()
                    .stream()
                    .filter(productInBasket1 -> productInBasket1.getProduct() != null &&
                            productInBasket1.getProduct().getName().equals(item.getName())
                            && productInBasket1.getProduct().getPrice() == item.getPrice())
                    .findFirst();
            if (productInBasketOpt.isPresent()) {
                if (item.getCount() <= 0)
                    item.setCount(1);
                ProductInBasket productInBasket = productInBasketOpt.get();
                Product product = productInBasket.getProduct();
                if (product.getCount() >= item.getCount()) {
                    if (item.getCount() > productInBasket.getCount())
                        item.setCount(productInBasket.getCount());
                    int countToBuy = item.getCount();
                    product.setCount(product.getCount() - countToBuy);
                    productInBasket.setCount(productInBasket.getCount() - countToBuy);
                    if (productInBasket.getCount() == 0)
                        personBasket.delProduct(productInBasket);
                    productInBasketRepository.deleteById(productInBasket.getId());
                    bought.add(new BuyProductDtoResponse(item.getId(), item.getName(),
                            item.getPrice(), item.getCount()));
                }
            }
        });
        personBasket.getProductInBaskets().forEach(product ->
                remaining.add(new BuyProductDtoResponse(product.getProductIdInfo(),
                        product.getName(), product.getPrice(), product.getCount())));
        Optional<Integer> totalPriseOpt = bought
                .stream()
                .map(buyProduct -> buyProduct.getCount() * buyProduct.getPrice())
                .reduce(Integer::sum);
        totalPriseOpt.ifPresent(totalPrise -> {
            if (totalPrise > person.getDeposit())
                throw new NoMoneyException(GlobalExceptionErrorCode.ERROR_BASKET);
            person.setDeposit(person.getDeposit() - totalPrise);
        });
        personRepository.save(person);
        return new BuyBasketResponseDto(bought, remaining);
    }
}
