import React from 'react';
import validate from "./validators/person-validators";
import Button from "react-bootstrap/Button";
import * as API_USERS from "../api/person-api";
import APIResponseErrorMessage from "../../../commons/errorhandling/api-response-error-message";
import {Col, FormGroup, Input, Label, Row} from "reactstrap";


class PersonForm extends React.Component {

    constructor(props) {
        super(props);
        this.toggleForm = this.toggleForm.bind(this);
        this.reloadHandler = this.props.reloadHandler;
        this.selectedPerson = this.props.selectedPerson;
        this.selected = this.selectedPerson != null;

        this.state = {

            errorStatus: 0,
            error: null,

            formIsValid: false,

            formControls: {
                id: {
                    value: this.selected ? this.selectedPerson.id : "",
                    placeholder: 's00m3-r4nd0m-1d-w1ll-b3-4ss1gn3d-4ut0m4t1c4ll1',
                    valid: true,
                    touched: false,
                },
                name: {
                    value: this.selected ? this.selectedPerson.name : "",
                    placeholder: 'What is his name?...',
                    valid: this.selected,
                    touched: this.selected,
                    validationRules: {
                        minLength: 3,
                        isRequired: true
                    }
                },
                email: {
                    value: this.selected ? this.selectedPerson.email : "",
                    placeholder: 'Email...',
                    valid: this.selected,
                    touched: this.selected,
                    validationRules: {
                        emailValidator: true
                    }
                },
                role: {
                    value: this.selected ? this.selectedPerson.role : "",
                    placeholder: 'ADMIN or CLIENT',
                    valid: this.selected,
                    touched: this.selected,
                },
                password: {
                    value: this.selected ? this.selectedPerson.password : "",
                    placeholder: '***',
                    valid: this.selected,
                    touched: this.selected,
                    validationRules: {
                        minLength: 3,
                        isRequired: true
                    }
                },
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

    registerPerson(person) {
        return API_USERS.postPerson(person, (result, status, error) => {
            if (result !== null && (status === 200 || status === 201)) {
                console.log("Successfully inserted user with id: " + result);
                this.reloadHandler();
            } else {
                this.setState(({
                    errorStatus: status,
                    error: error
                }));
            }
        });
    }

    updatePerson(person) {
        return API_USERS.putPerson(person, (result, status, error) => {
            if (result !== null && (status === 200 || status === 201)) {
                console.log("Successfully updated user with id: " + result);
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
        let person = {
            id: this.state.formControls.id.value,
            name: this.state.formControls.name.value,
            email: this.state.formControls.email.value,
            role: this.state.formControls.role.value,
            password: this.state.formControls.password.value
        };

        console.log(person);
        if (person.id == null || person.id === "")
            this.registerPerson(person);
        else
            this.updatePerson(person);
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
                    {this.state.formControls.name.touched && !this.state.formControls.name.valid &&
                        <div className={"error-message row"}> * Name must have at least 3 characters </div>}
                </FormGroup>

                <FormGroup id='name'>
                    <Label for='nameField'> Name: </Label>
                    <Input name='name' id='nameField' placeholder={this.state.formControls.name.placeholder}
                           onChange={this.handleChange}
                           defaultValue={this.state.formControls.name.value}
                           touched={this.state.formControls.name.touched ? 1 : 0}
                           valid={this.state.formControls.name.valid}
                           required
                    />
                </FormGroup>

                <FormGroup id='email'>
                    <Label for='emailField'> Email: </Label>
                    <Input name='email' id='emailField' placeholder={this.state.formControls.email.placeholder}
                           onChange={this.handleChange}
                           defaultValue={this.state.formControls.email.value}
                           touched={this.state.formControls.email.touched ? 1 : 0}
                           valid={this.state.formControls.email.valid}
                           required
                    />
                    {this.state.formControls.email.touched && !this.state.formControls.email.valid &&
                        <div className={"error-message"}> * Email must have a valid format</div>}
                </FormGroup>

                <FormGroup id='role'>
                    <Label for='roleField'> Role: </Label>
                    <Input name='role' id='roleField' placeholder={this.state.formControls.role.placeholder}
                           onChange={this.handleChange}
                           defaultValue={this.state.formControls.role.value}
                           touched={this.state.formControls.role.touched ? 1 : 0}
                           valid={this.state.formControls.role.valid}
                           required
                    />
                </FormGroup>

                <FormGroup id='password'>
                    <Label for='passwordField'> Password: </Label>
                    <Input name='password' id='passwordField' placeholder={this.state.formControls.password.placeholder}
                        // min={0} max={1} type="number"
                           onChange={this.handleChange}
                           defaultValue={this.state.formControls.password.value}
                           touched={this.state.formControls.password.touched ? 1 : 0}
                           valid={this.state.formControls.password.valid}
                           required
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

export default PersonForm;
