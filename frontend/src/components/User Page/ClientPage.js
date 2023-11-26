import React, {useEffect, useState} from 'react';
import {Button, Card, CardBody, CardHeader, ListGroup, ListGroupItem,} from 'reactstrap';
import 'bootstrap/dist/css/bootstrap.css';
import axios from 'axios';
import {HOST} from '../../commons/hosts';
import {useNavigate} from 'react-router-dom';
import BarChart from "./BarChart";

const containerStyle = {
    padding: '20px',
    border: '1px solid #ccc',
    borderRadius: '5px',
    boxShadow: '0 0 10px rgba(0, 0, 0, 0.1)',
    backgroundColor: 'rgba(255, 255, 255, 0)',
};

const headerStyle = {
    textAlign: 'center',
    marginBottom: '20px',
};

const noDeviceStyle = {
    textAlign: 'center',
};

const hrStyle = {
    marginTop: '20px',
    borderTop: '1px solid #ddd',
};

const buttonStyle = {
    marginTop: '10px',
};

export default function ClientPage() {
    const [userDevices, setUserDevices] = useState([]);
    const [currentUser, setCurrentUser] = useState(null);
    const [selectedDevice, setSelectedDevice] = useState(null);
    const [apiResponse, setApiResponse] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        let loggedUser = JSON.parse(sessionStorage.getItem('loggedUser'));
        const role = loggedUser ? loggedUser.role : ' ';
        handleRedirect(role);
        setCurrentUser(loggedUser);
    }, []);

    const handleRedirect = (role) => {
        if (role === 'ADMIN') navigate('/admin');
        if (role === ' ') navigate('/');
    };

    useEffect(() => {
        if (currentUser) {
            axios
                .get(HOST.device_backend_api + '/device/getByUser/' + currentUser.id)
                .then((response) => {
                    setUserDevices(response.data);
                });
        }
    }, [currentUser]);

    const handleButtonClick = (deviceId) => {
        axios
            .get(HOST.monitoring_api + deviceId)
            .then((response) => {
                setApiResponse(response.data);
            })
            .catch((error) => {
                console.error('Error fetching data:', error);
            });
    };

    const handleDeviceClick = (device) => {
        setSelectedDevice(device);
        handleButtonClick(device.id)
    };

    if (!currentUser) {
        return <div>Invalid login. Redirecting to the login page...</div>;
    }

    return (
        <div style={{display: 'flex'}}>
            <div style={{flex: 0.25}}>
                <Card style={containerStyle}>
                    <CardHeader style={headerStyle}>{currentUser.name}'s Devices</CardHeader>
                    {userDevices.length > 0 ? (
                        <ListGroup style={{listStyle: 'none', padding: '0'}}>
                            {userDevices.map((device) => (
                                <ListGroupItem key={device.id}>
                                    <h3
                                        style={{marginBottom: '10px', cursor: 'pointer'}}
                                        onClick={() => handleDeviceClick(device)}
                                    >
                                        {device.description}
                                    </h3>
                                    <p>
                                        <strong>Address:</strong> {device.address}
                                    </p>
                                    <p>
                                        <strong>Max Consumption:</strong> {device.maxConsumption}
                                    </p>
                                    <Button
                                        color="primary"
                                        onClick={() => handleButtonClick(device.id)}
                                        style={buttonStyle}
                                    >
                                        Fetch Data
                                    </Button>
                                    <hr style={hrStyle}/>
                                </ListGroupItem>
                            ))}
                        </ListGroup>
                    ) : (
                        <CardBody style={noDeviceStyle}>No devices found for this client.</CardBody>
                    )}
                </Card>
            </div>
            <div style={{flex: 1, marginLeft: '20px'}}>
                {selectedDevice && apiResponse && (

                    <div>
                        <BarChart data={apiResponse}/>

                        <h2>Data for {selectedDevice.description}</h2>
                        dghjgdj
                        {/* Add code to render the graph using the apiResponse data */}
                        {/* For example, you can use a chart library like BarChart.js */}
                    </div>
                )}
            </div>
        </div>
    );
}
