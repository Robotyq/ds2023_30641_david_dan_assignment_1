import React from 'react';
import APIResponseErrorMessage from "../../commons/errorhandling/api-response-error-message";
import {Button, Card, CardHeader, Col, Modal, ModalBody, ModalHeader, Row} from 'reactstrap';
import PersonForm from "./components/person-form";
// import 'bootstrap/dist/css/bootstrap.css';
import * as API_USERS from "./api/person-api"
import PersonTable from "./components/person-table";


class UsersCrud extends React.Component {

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
            selectedPerson: null
        };
    }

    componentDidMount() {
        this.fetchPersons();
    }

    fetchPersons() {
        return API_USERS.getPersons((result, status, err) => {

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

    deletePerson = (userId) => {
        // Implement your delete logic here using the userId
        alert('Delete user with ID:' + userId);
        return API_USERS.deletePerson(userId, (result, status, error) => {
            if (result !== null && (status === 200 || status === 201)) {
                console.log("Successfully delete user with id: " + userId);
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

    openEditForm(selectedPerson) {
        this.setState({selected: true, selectedPerson: selectedPerson});
    }

    closeEditForm() {
        this.setState({selected: false, selectedPerson: null});
    }

    reload() {
        this.setState({
            isLoaded: false
        });
        // this.toggleForm();
        this.closeEditForm();
        this.fetchPersons();
    }

    render() {
        return (
            <div>
                <CardHeader>
                    <strong> Users Management </strong>
                </CardHeader>
                <Card>
                    <br/>
                    <Row>
                        <Col sm={{size: '8', offset: 1}}>
                            <Button color="primary" onClick={() => {
                                this.openEditForm(null)
                            }}>Add User </Button>
                        </Col>
                    </Row>
                    <br/>
                    <Row>
                        <Col sm={{size: '8', offset: 1}}>
                            {this.state.isLoaded &&
                                <PersonTable tableData={this.state.tableData} update={(selectedPerson) => {
                                    this.openEditForm(selectedPerson)
                                }}
                                             delete={(id) => {
                                                 this.deletePerson(id)
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
                    <ModalHeader toggle={this.toggleForm}> Add User: </ModalHeader>
                    <ModalBody>
                        <PersonForm reloadHandler={this.reload} selectedPerson={this.state.selectedPerson}/>
                    </ModalBody>
                </Modal>
            </div>
        )

    }
}

export default UsersCrud;
