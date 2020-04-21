import CartContainer from "../../components/cart/CartContainer";
import {getWrapper} from "../utils";


describe('testing <CartContainer/>', () => {

    it('render cart items', () => {
        const cartState = {
            editingCart: [
                {
                    "id": 27,
                    "name": "Nikon 24-70mm f/2.8G",
                    "price": 1400,
                    "count": 2
                },
                {
                    "id": 29,
                    "name": "The C Programming Language",
                    "price": 40,
                    "count": 3
                }
            ]
        };
        const wrapper = getWrapper(CartContainer, { cartState });
        const totalSum = cartState.editingCart
            .map(value => value.count * value.price)
            .reduce((previousValue, currentValue) => previousValue + currentValue, 0);
        const totalSumFromString = +wrapper.find('[data-test="total-sum"]').text().replace(/\W/g, "")
        expect(totalSumFromString).toEqual(totalSum);
        const cartItems = wrapper.find('[data-test="cart-item"]')
        expect(cartItems).toHaveLength(2);
        cartItems.forEach((node, index) => {
            const product = cartState.editingCart[index]
            expect(node.find('[data-test="name"]').text()).toEqual(product.name);
            const total = +node.find('[data-test="total"]').text().replace(/\W/g, "");
            expect(total).toEqual(product.count * product.price);
            expect(+node.find('[name="count"]').instance().value).toEqual(product.count)
        })
    });
    
    it('render empty cart message', () => {
        const wrapper = getWrapper(CartContainer);
        expect(wrapper.find('[data-test="empty-cart"]').text()).toBeTruthy()
    })
})