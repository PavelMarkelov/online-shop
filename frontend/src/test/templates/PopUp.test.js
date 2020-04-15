import PopUp from "../../components/templates/pop-up/PopUp";
import {getWrapper} from '../utils';


describe('testing PopUp component', () => {

    it('testing toggle visibility', () => {
        const wrapper = getWrapper(PopUp)
        expect(wrapper.find('.overlay').hasClass('pop-up-visible')).toBeFalsy()
        wrapper.find('button').simulate('click')
        expect(wrapper.find('.overlay').hasClass('pop-up-visible')).toBeTruthy()
    })

    it('rendering message for error', () => {
        const popUpState = {
            isVisible: true,
            messageForFail: 'Oops... Don\'t worry, be happy!',
            messageForSuccess: ''
        }
        const wrapper = getWrapper(PopUp, {popUpState});
        expect(wrapper.find('.overlay').hasClass('pop-up-visible')).toBeTruthy()
        expect(wrapper.find('.alert').hasClass('alert-danger')).toBeTruthy()
        expect(wrapper.find('.alert').hasClass('alert-success')).toBeFalsy()
    })

    it('rendering message for success', () =>{
        const popUpState = {
            isVisible: true,
            messageForFail: '',
            messageForSuccess: 'It\'s OK, it\'s working!'
        }
        const wrapper = getWrapper(PopUp, {popUpState});
        console.log(wrapper.debug());
        expect(wrapper.find('.overlay').hasClass('pop-up-visible')).toBeTruthy()
        expect(wrapper.find('.alert').hasClass('alert-danger')).toBeFalsy()
        expect(wrapper.find('.alert').hasClass('alert-success')).toBeTruthy()
    })
})