package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.dto.Request.BuyProductDtoRequest;
import net.thumbtack.onlineshop.dto.Response.BasketResponseDto;
import net.thumbtack.onlineshop.dto.Response.BuyProductDtoResponse;
import net.thumbtack.onlineshop.entities.Basket;
import net.thumbtack.onlineshop.entities.Person;
import net.thumbtack.onlineshop.entities.Product;
import net.thumbtack.onlineshop.entities.ProductInBasket;
import net.thumbtack.onlineshop.exception.GlobalExceptionErrorCode;
import net.thumbtack.onlineshop.exception.ProductNotFoundException;
import net.thumbtack.onlineshop.repos.BasketRepository;
import net.thumbtack.onlineshop.repos.PersonRepository;
import net.thumbtack.onlineshop.repos.ProductInBasketRepository;
import net.thumbtack.onlineshop.repos.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public List<BuyProductDtoResponse> addToBasketOrChangingQuantity(
            String login,
            BuyProductDtoRequest request
    ) {
        Optional<Product> productOpt = productRepository.findById(request.getId());
        if (!productOpt.isPresent())
            throw new ProductNotFoundException(GlobalExceptionErrorCode.PRODUCT_NOT_FOUND);
        Product product = productOpt.get();
        if (!request.getName().equals(product.getName()) || request.getPrice() != product.getPrice())
            throw new ProductNotFoundException(GlobalExceptionErrorCode.BUY_ERROR);
        int count = request.getCount();
        if (count <= 0)
            count = 1;
        Person person = personRepository.findByLogin(login);
        Basket basket = person.getBasket();
        Optional<ProductInBasket> productExist = basket.getProductInBaskets()
                .stream()
                .filter(item -> item.getProduct().getId().equals(request.getId()))
                .findFirst();
        if (!productExist.isPresent()) {
            ProductInBasket productInBasket = new ProductInBasket(basket, product,
                    product.getName(), product.getPrice(), count);
            basket.addProduct(productInBasket);
            productInBasketRepository.save(productInBasket);
        }
        else if (request.getCount() != productExist.get().getCount()){
            productExist.get().setCount(request.getCount());
            productInBasketRepository.save(productExist.get());
        }
        return basket.getProductInBaskets()
                .stream()
                .map(item -> new BuyProductDtoResponse(item.getProduct().getId(),
                        item.getName(), item.getPrice(), item.getCount()))
                .collect(Collectors.toList());
    }

    public void deleteProductFromBasket(String login, Long productId) {
        Person person = personRepository.findByLogin(login);
        Basket basket = person.getBasket();
        Optional<ProductInBasket> productOpt = basket.getProductInBaskets()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();
        basketRepository.save(basket);
        productOpt.ifPresent(product -> {
            basket.delProduct(product);
            productInBasketRepository.deleteById(product.getId());
        });
    }

    public List<BuyProductDtoResponse> findBasketByPersonLogin(String login) {
        Person person = personRepository.findByLogin(login);
        return person.getBasket().getProductInBaskets()
                .stream()
                .map(item -> new BuyProductDtoResponse(item.getProduct().getId(),
                        item.getName(), item.getPrice(), item.getCount()))
                .collect(Collectors.toList());
    }

    public BasketResponseDto buyingFromBasket(Basket basket) {
        return null;
    }
}
