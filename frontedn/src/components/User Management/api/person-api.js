import {HOST} from '../../../commons/hosts';
import RestApiClient from "../../../commons/api/rest-client";


const endpoint = {
    user: '/user'
};

function getPersons(callback) {
    let request = new Request(HOST.user_backend_api + endpoint.user, {
        method: 'GET',
    });
    console.log(request.url);
    RestApiClient.performRequest(request, callback);
}

function getPersonById(params, callback) {
    let request = new Request(HOST.user_backend_api + endpoint.user + "/" + params.id, {
        method: 'GET'
    });

    console.log(request.url);
    RestApiClient.performRequest(request, callback);
}

function postPerson(user, callback) {
    let request = new Request(HOST.user_backend_api + endpoint.user + "/insert", {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(user)
    });

    console.log("URL: " + request.url);

    RestApiClient.performRequest(request, callback);
}

function putPerson(user, callback) {
    let request = new Request(HOST.user_backend_api + endpoint.user, {
        method: 'PUT',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(user)
    });

    console.log("URL: " + request.url);

    RestApiClient.performRequest(request, callback);
}

function deletePerson(id, callback) {
    let request = new Request(HOST.user_backend_api + endpoint.user + "/delete/" + id, {
        method: 'DELETE',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
    });

    console.log("URL: " + request.url);

    RestApiClient.performRequest(request, callback);
}

export {
    getPersons,
    getPersonById,
    postPerson,
    putPerson,
    deletePerson
};
