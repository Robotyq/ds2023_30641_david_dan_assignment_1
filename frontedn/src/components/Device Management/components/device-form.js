import React from 'react';
import validate from "./validators/device-validators";
import Button from "react-bootstrap/Button";
import * as API_DEVICES from "../api/device-api";
import APIResponseErrorMessage from "../../../commons/errorhandling/api-response-error-message";
import {Col, FormGroup, Input, Label, Row} from "reactstrap";


class DeviceForm extends React.Component {

    constructor(props) {
        super(props);
        this.toggleForm = this.toggleForm.bind(this);
        this.reloadHandler = this.props.reloadHandler;
        this.selectedDevice = this.props.selectedDevice;
        this.selected = this.selectedDevice != null;

        this.state = {

            errorStatus: 0,
            error: null,

            formIsValid: false,

            formControls: {
                id: {
                    value: this.selected ? this.selectedDevice.id : "",
                    placeholder: 's00m3-r4nd0m-1d-w1ll-b3-4ss1gn3d-4ut0m4t1c4ll1',
                    valid: true,
                    touched: false,
                },
                description: {
                    value: this.selected ? this.selectedDevice.description : "",
                    placeholder: 'Name or description',
                    valid: this.selected,
                    touched: this.selected,
                    validationRules: {
                        minLength: 3,
                        isRequired: true
                    }
                },
                address: {
                    value: this.selected ? this.selectedDevice.address : "",
                    placeholder: 'Bathroom at home',
                    valid: this.selected,
                    touched: this.selected
                },
                owner: {
                    value: this.selected ? this.selectedDevice.userId : "",
                    placeholder: 'Use the mapping page to assign a user to this device',
                    valid: true,
                    touched: false,
                },
                maxC: {
                    value: this.selected ? this.selectedDevice.maxC : "",
                    placeholder: '10',
                    valid: this.selected,
                    touched: this.selected,
                }
            }
        };

        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    toggleForm() {
        this.setState({collapseForm: !this.state.collapseForm});
    }


    handleChange = event => {

        const name = event.target.name;
        const value = event.target.value;

        const updatedControls = this.state.formControls;

        const updatedFormElement = updatedControls[name];

        updatedFormElement.value = value;
        updatedFormElement.touched = true;
        updatedFormElement.valid = validate(value, updatedFormElement.validationRules);
        updatedControls[name] = updatedFormElement;

        let formIsValid = true;
        for (let updatedFormElementName in updatedControls) {
            formIsValid = updatedControls[updatedFormElementName].valid && formIsValid;
        }

        this.setState({
            formControls: updatedControls,
            formIsValid: formIsValid
        });

    };

    registerDevice(device) {
        console.log(" inserting device with id: ")
        return API_DEVICES.postDevice(device, (result, status, error) => {
            if (result !== null && (status === 200 || status === 201)) {
                console.log("Successfully inserted device with id: " + result);
                this.reloadHandler();
            } else {
                this.setState(({
                    errorStatus: status,
                    error: error
                }));
            }
        });
    }

    updateDevice(device) {
        return API_DEVICES.putDevice(device, (result, status, error) => {
            if (result !== null && (status === 200 || status === 201)) {
                console.log("Successfully updated device with id: " + result);
                this.reloadHandler();
            } else {
                this.setState(({
                    errorStatus: status,
                    error: error
                }));
            }
        });
    }


    handleSubmit() {
        console.log("Device form submitted")
        console.log(this.state.formControls.id);
        let device = {
            id: this.state.formControls.id.value,
            description: this.state.formControls.description.value,
            address: this.state.formControls.address.value,
            maxConsumption: this.state.formControls.maxC.value,
            userId: this.state.formControls.owner.value
        };

        console.log(device);
        if (device.id == null || device.id === "")
            this.registerDevice(device);
        else
            this.updateDevice(device);
    }

    render() {
        return (
            <div>

                <FormGroup id='id'>
                    <Label for='idField'> ID: </Label>
                    <Input name='id' id='idField' placeholder={this.state.formControls.id.placeholder}
                           onChange={this.handleChange}
                           defaultValue={this.state.formControls.id.value}
                           touched={this.state.formControls.id.touched ? 1 : 0}
                           valid={this.state.formControls.id.valid}
                           disabled={true}
                    />
                </FormGroup>

                <FormGroup id='description'>
                    <Label for='descriptionField'> Description: </Label>
                    <Input name='description' id='descriptionField'
                           placeholder={this.state.formControls.description.placeholder}
                           onChange={this.handleChange}
                           defaultValue={this.state.formControls.description.value}
                           touched={this.state.formControls.description.touched ? 1 : 0}
                           valid={this.state.formControls.description.valid}
                           required
                    /> {this.state.formControls.description.touched && !this.state.formControls.description.valid &&
                    <div className={"error-message row"}> * Description must have at least 3 characters </div>}
                </FormGroup>

                <FormGroup id='address'>
                    <Label for='addressField'> Address/Location: </Label>
                    <Input name='address' id='addressField' placeholder={this.state.formControls.address.placeholder}
                           onChange={this.handleChange}
                           defaultValue={this.state.formControls.address.value}
                           touched={this.state.formControls.address.touched ? 1 : 0}
                           valid={this.state.formControls.address.valid}
                    />
                </FormGroup>

                <FormGroup id='maxC'>
                    <Label for='maxCField'> Maximum Consumption: </Label>
                    <Input type={"number"}
                           name='maxC' id='maxCField' placeholder={this.state.formControls.maxC.placeholder}
                           onChange={this.handleChange}
                           defaultValue={this.state.formControls.maxC.value}
                           touched={this.state.formControls.maxC.touched ? 1 : 0}
                           valid={this.state.formControls.maxC.valid}
                    />
                </FormGroup>

                <FormGroup id='owner'>
                    <Label for='ownerField'> Owner: </Label>
                    <Input name='owner' id='ownerField' placeholder={this.state.formControls.owner.placeholder}
                           onChange={this.handleChange}
                           defaultValue={this.state.formControls.owner.value}
                           touched={this.state.formControls.owner.touched ? 1 : 0}
                           valid={this.state.formControls.owner.valid}
                           disabled={true}
                    />
                </FormGroup>

                <Row>
                    <Col sm={{size: '4', offset: 8}}>
                        <Button type={"submit"} disabled={!this.state.formIsValid}
                                onClick={this.handleSubmit}> Submit </Button>
                    </Col>
                </Row>

                {
                    this.state.errorStatus > 0 &&
                    <APIResponseErrorMessage errorStatus={this.state.errorStatus} error={this.state.error}/>
                }
            </div>
        );
    }
}

export default DeviceForm;
