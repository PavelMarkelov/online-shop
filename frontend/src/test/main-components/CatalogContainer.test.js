import CatalogContainer from "../../components/catalog/CatalogContainer";
import {getWrapper} from "../utils";


describe('testing <CatalogContainer/>', () => {

    it('render categories of products', () =>{
        const categoriesState = {
            categoriesList: [
                {
                    id: 18,
                    name: "Books"
                },
                {
                    id: 17,
                    name: "Photo equipment"
                },
                {
                    id: 19,
                    name: "Cameras",
                    parentId: 17,
                    parentName: "Photo equipment"
                },
                {
                    id: 20,
                    name: "Lens",
                    parentId: 17,
                    parentName: "Photo equipment"
                }
            ]
        };
        window.HTMLElement.prototype.scrollIntoView = function() {};
        const wrapper = getWrapper(CatalogContainer, { categoriesState });
        const parentCategories = wrapper.find('[data-test="parent"]');
        expect(parentCategories).toHaveLength(2);
        const books = parentCategories.first();
        expect(books.find('[data-test="children"]')).toEqual({});
        expect(books.children()).toHaveLength(1);
        const photoEquipment = parentCategories.last();
        expect(photoEquipment.find('[data-test="children"]')).toHaveLength(2);
        expect(photoEquipment.children()).toHaveLength(2);
        console.log(books.find('[data-test="parent-item"]').debug())
        expect(books.find('[data-test="parent-item"]').text())
            .toEqual(categoriesState.categoriesList[0].name);
        expect(photoEquipment.find('[data-test="parent-item"]').text())
            .toEqual(categoriesState.categoriesList[1].name);
        expect(photoEquipment.find('[data-test="child-item"]').first().text())
            .toEqual(categoriesState.categoriesList[2].name);
        expect(photoEquipment.find('[data-test="child-item"]').last().text())
            .toEqual(categoriesState.categoriesList[3].name);
    })
})