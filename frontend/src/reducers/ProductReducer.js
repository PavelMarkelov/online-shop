import * as actionType from '../actions/ActionsType';
import faker from 'faker';

const images = new Map([
    [28, 'https://img4.okidoker.com/c/3/9/6/296760/5782249/12257100_2.jpg'],
    [23, 'https://cdn-1.dar.kz/darbiz/catalogs/93e/a75b3fc6-93e2-11e9-91a3-0a580a0205da.jpg'],
    [31, 'https://i.ebayimg.com/00/s/OTEwWDY0Ng==/z/ylIAAOSwXwtdIjr3/$_57.JPG?set_id=8800005007'],
    [27, 'http://www.shx.ru/info/obektiv-Nikon-24-70-mm-f-2-8-E-VR-Nikkor-AF-S-4.jpg'],
    [22, 'https://main-cdn.goods.ru/big1/hlr-system/1748262/100000009691b0.jpg'],
    [30, 'https://www.gfxtra31.com/uploads/posts/2017-10/1507902280_2.jpg'],
    [24, 'https://img.moyo.ua/img/gallery/1653/80/303729_main.jpg?1498828241'],
    [26, 'https://skidka-ekb.ru/images/prodacts/sourse/61433/61433000_obyektiv-tamron-sp-af-70-300mm-f-4-0-5-6-di-vc-usd-dlya-canon-ef-a005e-a005-a005e.jpg'],
    [29, 'https://toloka.to/photos/141007202218493983_f0_0.jpg']
]);

const initialState = {
    productsList: [],
    productDescription: {},
    filter: null
};

const productReducer = (state = initialState, action) => {
    switch (action.type) {

        case actionType.PRODUCTS_LIST:
            return {
                ...state,
                productsList: action.payload
                    .map(item => Object.assign(item, {image: images.get(item.id)}))
            };

        case actionType.PRODUCT_CATEGORY:
            return {
                ...state,
                filter: null,
                productsList: action.payload
                    .map(item => Object.assign(item, { image: images.get(item.id) }))
            };

        case actionType.ENABLE_FILTER:
            return {
                ...state,
                filter: action.payload
            };

        case actionType.DISABLE_FILTER:
            return {
                ...state,
                filter: null,
                productsList: action.payload
                    .map(item => Object.assign(item, { image: images.get(item.id) }))
            };

        case actionType.PRODUCT_DESCRIPTION:
            return {
                ...state,
                productDescription: Object.assign(action.payload, {
                        image : images.get(action.payload.id),
                        description: faker.lorem.paragraphs()
                    }
                )
            };

        default:
            return state
    }
};

export default productReducer;