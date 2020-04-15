import React from 'react'
import {Router} from 'react-router-dom'
import {createMemoryHistory} from 'history'
import {render} from '@testing-library/react'
import {Provider} from 'react-redux';
import {createStore} from 'redux';
import reducers from '../reducers/index';
import Enzyme, {mount} from 'enzyme';
import Adapter from 'enzyme-adapter-react-16';
// React 16 Enzyme adapter
Enzyme.configure({ adapter: new Adapter() });

export function renderWithRouter(
    ui,
    {
        route = '/',
        history = createMemoryHistory({ initialEntries: [route] }),
    } = {}
) {
    const Wrapper = ({ children }) => (
        <Router history={history}>{children}</Router>
    )
    return {
        ...render(ui, { wrapper: Wrapper }),
        history,
    }
}

export const getWrapper = (Component, preloadedState) => {
    const mockStore = createStore(reducers, preloadedState)
    if (preloadedState)
        mockStore.dispatch = jest.fn();
    return mount(
        <Provider store={mockStore}>
            <Component/>
        </Provider>
    )
}