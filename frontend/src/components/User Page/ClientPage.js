import React, {useEffect, useState} from 'react';
import {Button, Card, CardBody, CardHeader, ListGroup, ListGroupItem,} from 'reactstrap';
import 'bootstrap/dist/css/bootstrap.css';
import axios from 'axios';
import {HOST} from '../../commons/hosts';
import {useNavigate} from 'react-router-dom';
import BarChart from "./BarChart";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import SockJsClient from 'react-stomp';

const containerStyle = {
    padding: '20px',
    border: '1px solid #ccc',
    borderRadius: '5px',
    boxShadow: '0 0 10px rgba(0, 0, 0, 0.1)',
    backgroundColor: 'rgba(255, 255, 255, 0)',
    overflowY: 'auto', // Add vertical scrolling for the cards
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
    const [selectedDate, setSelectedDate] = useState(new Date());
    const navigate = useNavigate();


    useEffect(() => {
        let loggedUser = JSON.parse(sessionStorage.getItem('loggedUser'));
        const role = loggedUser ? loggedUser.role : ' ';
        handleRedirect(role);
        setCurrentUser(loggedUser);
    }, []);

    let onConnected = () => {
        console.log("Connected!!")
    }

    let onMessageReceived = (msg) => {
        console.log("New Message Received!!", msg)
        let deviceId = msg.deviceId;
        let consumption = msg.measure;
        let consNumber = parseFloat(consumption).toFixed(1);
        let device = userDevices.find(device => device.id === deviceId);
        if (device) {
            let deviceName = device.description;
            alert("Device " + deviceName + " consumption is too high!  " + consNumber + " W");
        }
    }
    useEffect(() => {
        if (selectedDevice === null) return
        let date = selectedDate.toISOString().split('T')[0]
        handleButtonClick(selectedDevice.id, date)
    }, [selectedDevice, selectedDate]);

    const handleRedirect = (role) => {
        if (role === 'ADMIN') navigate('/admin');
        if (role === ' ') navigate('/');
    };
    const handleDateChange = (date) => {
        setSelectedDate(date);
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

    const handleButtonClick = (deviceId, date) => {
        axios
            .get(HOST.monitoring_api + deviceId, {
                params: {
                    dateString: date, // Format as "yyyy-MM-DD
                },
            })
            .then((response) => {
                setApiResponse(response.data);
            })
            .catch((error) => {
                console.error('Error fetching data:', error);
                setApiResponse(null);
            });
    };

    if (!currentUser) {
        return <div>Invalid login. Redirecting to the login page...</div>;
    }

    return (
        <div style={{display: 'flex', height: '100vh'}}>
            <div>
                <SockJsClient
                    url={HOST.monitoring_socket}
                    topics={['/topic/message']}
                    onConnect={onConnected}
                    onDisconnect={console.log("Disconnected!")}
                    onMessage={msg => onMessageReceived(msg)}
                    debug={true}
                />
            </div>
            <div style={{flex: 0.25, overflowY: 'auto'}}>
                <Card style={containerStyle}>
                    <CardHeader style={headerStyle}>{currentUser.name}'s Devices</CardHeader>
                    {userDevices.length > 0 ? (
                        <ListGroup style={{listStyle: 'none', padding: '0'}}>
                            {userDevices.map((device) => (
                                <ListGroupItem key={device.id}>
                                    <h3
                                        style={{marginBottom: '10px', cursor: 'pointer'}}
                                        onClick={() => setSelectedDevice(device)}
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
                                        onClick={() => {
                                            handleButtonClick(device.id, selectedDate.toISOString().split('T')[0]);
                                            setSelectedDevice(device)
                                        }
                                        }
                                        style={buttonStyle}
                                    >
                                        Fetch History
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
            <div style={{flex: 0.75, marginLeft: '20px', textAlign: 'center'}}>
                <label style={{marginBottom: '10px', display: 'block'}}>Select Date:</label>
                <DatePicker
                    selected={selectedDate}
                    onChange={handleDateChange}
                    dateFormat="dd-MM-yyyy"
                    className="form-control"
                    style={{margin: '0 auto'}} // Center the date picker
                />
                <br></br>
                <br></br>
                {selectedDevice && apiResponse !== null && apiResponse.length > 0 ? (
                    <div>
                        <BarChart data={apiResponse}/>
                    </div>
                ) : (
                    <div>
                        {selectedDevice === null && <p>No device selected</p>}
                        {selectedDevice !== null && apiResponse === null && <p>Can't fetch data</p>}
                        {selectedDevice !== null && apiResponse !== null && apiResponse.length === 0 && (
                            <p>No registered values for selected device and date</p>
                        )}
                    </div>
                )}
            </div>

        </div>
    );
}
