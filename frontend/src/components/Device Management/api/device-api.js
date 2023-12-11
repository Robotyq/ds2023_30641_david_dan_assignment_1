import {HOST} from '../../../commons/hosts';
import RestApiClient from "../../../commons/api/rest-client";


const endpoint = {
    user: '/user',
    device: '/device'
};
const token = localStorage.getItem("jwt");
function getDevices(callback) {
    const headers = {
        'Authorization': `Bearer ${token}`,
    };
    let request = new Request(HOST.device_backend_api + endpoint.device, {
        method: 'GET',
        headers: new Headers(headers),
    });
    // console.log(request.url);
    // console.log(token);
    // console.log(request.headers.get("Authorization"));
    RestApiClient.performRequest(request, callback);
}

function getUsersDevices(params, callback) {
    let request = new Request(HOST.device_backend_api + endpoint.device + "/getByUser" + params.id, {
        method: 'GET',
    });
    console.log(request.url);
    RestApiClient.performRequest(request, callback);
}

function getDeviceById(params, callback) {
    let request = new Request(HOST.device_backend_api + endpoint.device + "/" + params.id, {
        method: 'GET'
    });

    console.log(request.url);
    RestApiClient.performRequest(request, callback);
}

function postDevice(device, callback) {
    let request = new Request(HOST.device_backend_api + endpoint.device + "/insert", {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`,
        },
        body: JSON.stringify(device)
    });

    console.log("URL: " + request.url);

    RestApiClient.performRequest(request, callback);
}

function putDevice(device, callback) {
    let request = new Request(HOST.device_backend_api + endpoint.device + "/update", {
        method: 'PUT',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`,
        },
        body: JSON.stringify(device)
    });

    console.log("URL: " + request.url);

    RestApiClient.performRequest(request, callback);
}

function setUser(deviceId, userId, callback) {
    let request = new Request(HOST.device_backend_api + endpoint.device + "/" + deviceId + "/setUser?id=" + userId, {
        method: 'PUT',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`,
        },
    });

    console.log("URL: " + request.url);

    RestApiClient.performRequest(request, callback);
}

function deleteDevice(id, callback) {

    let request = new Request(HOST.device_backend_api + endpoint.device + "/delete/" + id, {
        method: 'DELETE',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            "Authorization": "Bearer " + token,
        },
    });

    console.log("URL: " + request.url);

    RestApiClient.performRequest(request, callback);
}

export {
    getDevices,
    getDeviceById,
    postDevice,
    putDevice,
    deleteDevice,
    setUser,
    getUsersDevices
};
