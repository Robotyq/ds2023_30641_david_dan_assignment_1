import React from 'react';
import {Button, Form, FormGroup, Input, Label} from 'reactstrap';
import * as API_DEVICES from "../Device Management/api/device-api";
import * as API_USERS from "../User Management/api/person-api";

class DeviceMapping extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            users: [],
            devices: [],
            selectedUserId: '',
            selectedDeviceId: '',
            mappingSuccess: false
        };
    }

    componentDidMount() {
        API_DEVICES.getDevices((result, status, err) => {
            if (result !== null && status === 200) {
                this.setState({devices: result});
            }
        });

        API_USERS.getPersons((result, status, err) => {
            if (result !== null && status === 200) {
                this.setState({users: result});
            }
        });
    }

    handleUserChange = (event) => {
        this.setState({selectedUserId: event.target.value});
    };

    handleDeviceChange = (event) => {
        const selectedDeviceId = event.target.value;
        const selectedDevice = this.state.devices.find(device => device.id === selectedDeviceId);
        if (selectedDevice && selectedDevice.userId) {
            this.setState({selectedUserId: selectedDevice.userId});
            console.log(selectedDevice.userId);
        }
        this.setState({selectedDeviceId: event.target.value});
        this.setState({mappingSuccess: false});
    };

    handleMapDeviceToUser = () => {
        API_DEVICES.setUser(this.state.selectedDeviceId, this.state.selectedUserId, (result, status, err) => {
            if (result !== null && status === 200) {
                this.setState({mappingSuccess: true});
            } else
                this.setState({mappingSuccess: false});
        });
        this.setState({mappingSuccess: false});
    };

    render() {
        return (
            <div>
                <h2>Device to User Mapping</h2>
                <Form>
                    <FormGroup>
                        <Label for="selectDevice">Select a device:</Label>
                        <Input type="select" name="selectDevice" id="selectDevice" onChange={this.handleDeviceChange}>
                            <option value="">Select a device</option>
                            {this.state.devices.map((device) => (
                                <option key={device.id} value={device.id}>
                                    {device.description}
                                </option>
                            ))}
                        </Input>
                    </FormGroup>
                    <FormGroup>
                        <Label for="selectUser">Select a user:</Label>
                        <Input type="select" name="selectUser" id="selectUser" onChange={this.handleUserChange}
                               value={this.state.selectedUserId}>
                            <option value="">Select a user</option>
                            {this.state.users.map((user) => (
                                <option key={user.id} value={user.id}>
                                    {user.name}
                                </option>
                            ))}
                        </Input>
                    </FormGroup>
                    <Button onClick={this.handleMapDeviceToUser} color="primary">Map Device to User</Button>
                    {this.state.mappingSuccess && <p>Device mapped to the user successfully!</p>}
                </Form>
            </div>
        );
    }
}

export default DeviceMapping;
