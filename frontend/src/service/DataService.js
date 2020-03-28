const REST_API_URL = 'http://localhost:8080/api';

class DataService {

    static loginRequest(credentials) {
        const params = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json;charset=utf-8'
            },
            body: JSON.stringify(credentials),
            credentials: 'include'
        };
        return fetch(REST_API_URL + '/sessions', params);
    }

    static logoutRequest() {
        const params = {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json;charset=utf-8'
            },
            credentials: 'include'
        };
        return fetch(REST_API_URL + '/sessions', params);
    }

    static userProfileRequest() {
        const params = {
            credentials: 'include'
        };
        return fetch(REST_API_URL + '/accounts', params);
    }

    static editAccountDataRequest(newAccountData) {
        const params = {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json;charset=utf-8'
            },
            body: JSON.stringify(newAccountData),
            credentials: 'include'
        };
        return fetch(REST_API_URL + '/clients', params)
    }
}

export default DataService;