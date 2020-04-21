import {getWrapper} from "../utils";
import React from 'react';


describe('testing <PrivateRoute/>', () => {

    const TestComponent = () => <div>test PrivateRoute</div>;

    it('should render component if user has been authenticated', () => {
        const userState = {
            isAuthenticated: true
        };
        const route = '/test';
        const wrapper = getWrapper(TestComponent, { userState }, { route }, true);
        expect(wrapper.exists(TestComponent)).toBeTruthy();
    });

    it('should redirect if user is not authenticated', () => {
        const userState = {
            isAuthenticated: false
        };
        const route = '/test';
        const wrapper = getWrapper(TestComponent, { userState }, { route }, true);
        expect(wrapper.exists(TestComponent)).toBeFalsy();
        const history = wrapper.find('Router').prop('history');
        expect(history.location.pathname).toBe('/');
    });
})