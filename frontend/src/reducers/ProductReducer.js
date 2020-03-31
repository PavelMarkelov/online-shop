import * as actionType from '../actions/ActionsType';

const images = new Map([
    [28, 'https://img4.okidoker.com/c/3/9/6/296760/5782249/12257100_2.jpg'],
    [23, 'https://cdn-1.dar.kz/darbiz/catalogs/93e/a75b3fc6-93e2-11e9-91a3-0a580a0205da.jpg'],
    [31, 'https://i.ebayimg.com/00/s/OTEwWDY0Ng==/z/ylIAAOSwXwtdIjr3/$_57.JPG?set_id=8800005007'],
    [27, 'https://fotocccp.ru/image/cache/import_files/03/0325ec89574511e7b63e00155d1fae00_23-900x600.jpg'],
    [22, 'https://63.img.avito.st/640x480/5557976463.jpg'],
    [30, 'https://www.gfxtra31.com/uploads/posts/2017-10/1507902280_2.jpg'],
    [24, 'https://img.moyo.ua/img/gallery/1653/80/303729_main.jpg?1498828241'],
    [26, 'https://stylus.ua/thumbs/568x568/7c/d6/683779.jpeg'],
    [29, 'https://toloka.to/photos/141007202218493983_f0_0.jpg']
]);

const initialState = {
    productsList: []
};

const productReducer = (state = initialState, action) => {
    switch (action.type) {

        case actionType.PRODUCTS_LIST:
            return {
                productsList: action.payload
                    .map(item => Object.assign(item, { image: images.get(item.id) }))
            };

        case actionType.PRODUCT_CATEGORY:
            return {
                productsList: action.payload
                    .map(item => Object.assign(item, { image: images.get(item.id) }))
            };

        default:
            return state
    }
};

export default productReducer;