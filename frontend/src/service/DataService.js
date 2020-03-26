const REST_API_URL = 'http://localhost:8080/api';

class DataService {

    static loginRequest(credentials) {
        const params = {
            method: 'POST',
            mode: "cors",
            headers: {
                'Content-Type': 'application/json;charset=utf-8'
            },
            body: JSON.stringify(credentials)
        };
        return fetch(REST_API_URL + '/sessions', params);
    }

    static logoutRequest() {
        const params = {
            method: 'DELETE',
            mode: "cors",
            headers: {
                'Content-Type': 'application/json;charset=utf-8'
            }
        };
        return fetch(REST_API_URL + '/sessions', params);
    }
}

export default DataService;