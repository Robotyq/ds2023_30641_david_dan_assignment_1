import React from 'react';
import APIResponseErrorMessage from "../../commons/errorhandling/api-response-error-message";
import {Button, Card, CardHeader, Col, Modal, ModalBody, ModalHeader, Row} from 'reactstrap';
import DeviceForm from "./components/device-form";
import 'bootstrap/dist/css/bootstrap.css';
import * as API_DEVICES from "./api/device-api"
import DeviceTable from "./components/device-table";


class DevicesCrud extends React.Component {

    constructor(props) {
        super(props);
        this.toggleForm = this.toggleForm.bind(this);
        this.reload = this.reload.bind(this);
        this.state = {
            selected: false,
            collapseForm: false,
            tableData: [],
            isLoaded: false,
            errorStatus: 0,
            error: null,
            selectedDevice: null
        };
    }

    componentDidMount() {
        this.fetchDevices();
    }

    fetchDevices() {
        return API_DEVICES.getDevices((result, status, err) => {

            if (result !== null && status === 200) {
                this.setState({
                    tableData: result,
                    isLoaded: true
                });
            } else {
                this.setState(({
                    errorStatus: status,
                    error: err
                }));
            }
        });
    }

    deleteDevice = (deviceId) => {
        alert('Delete Device with ID:' + deviceId);
        return API_DEVICES.deleteDevice(deviceId, (result, status, error) => {
            if (result !== null && (status === 200 || status === 201)) {
                console.log("Successfully delete device with id: " + deviceId);
                this.reload()
            } else {
                this.setState(({
                    errorStatus: status,
                    error: error
                }));
            }
        });


    };

    toggleForm() {
        this.setState({selected: !this.state.selected});
    }

    openEditForm(selectedDevice) {
        this.setState({selected: true, selectedDevice: selectedDevice});
    }

    closeEditForm() {
        this.setState({selected: false, selectedDevice: null});
    }

    reload() {
        this.setState({
            isLoaded: false
        });
        this.closeEditForm();
        this.fetchDevices();
    }

    render() {
        return (
            <div>
                <CardHeader>

                    <strong> Device Management </strong>
                </CardHeader>
                <Card>
                    <br/>
                    <Row>
                        <Col sm={{size: '8', offset: 1}}>
                            <Button color="primary" onClick={() => {
                                this.openEditForm(null)
                            }}>Add Device </Button>
                        </Col>
                    </Row>
                    <br/>
                    <Row>
                        <Col sm={{size: '8', offset: 1}}>
                            {this.state.isLoaded &&
                                <DeviceTable tableData={this.state.tableData} update={(selectedDevice) => {
                                    this.openEditForm(selectedDevice)
                                }}
                                             delete={(id) => {
                                                 this.deleteDevice(id)
                                             }}
                                />}
                            {this.state.errorStatus > 0 && <APIResponseErrorMessage
                                errorStatus={this.state.errorStatus}
                                error={this.state.error}
                            />}
                        </Col>
                    </Row>
                </Card>

                <Modal isOpen={this.state.selected} toggle={this.toggleForm}
                       className={this.props.className} size="lg">
                    <ModalHeader toggle={this.toggleForm}> Add Device: </ModalHeader>
                    <ModalBody>
                        <DeviceForm reloadHandler={this.reload} selectedDevice={this.state.selectedDevice}/>
                    </ModalBody>
                </Modal>
            </div>
        )

    }
}

export default DevicesCrud;
