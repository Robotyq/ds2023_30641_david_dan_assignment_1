import React, {useEffect} from 'react';
import UsersCrud from "../User Management/users-crud";
import DevicesCrud from "../Device Management/devices-crud";
import DeviceMapping from "./DeviceMapping";
import {Link, useNavigate} from "react-router-dom";
import {Button} from "reactstrap";

const AdminPage = () => {
    const navigate = useNavigate();

    useEffect(() => {
        let loggedUser = JSON.parse(sessionStorage.getItem('loggedUser'));
        const role = loggedUser ? loggedUser.role : ' ';
        handleRedirect(role)
    }, []);

    const handleRedirect = (role) => {
        if (role === "CLIENT")
            navigate("/client");
        if (role === " ")
            navigate("/");
    }

    return (
        <div style={{textAlign: 'center'}}>
            <h1>Welcome Admin!</h1>
            <div style={{display: 'flex', justifyContent: 'center'}}>
                <div style={{width: '78%', display: 'flex', flexDirection: 'column'}}>
                    <UsersCrud/>
                    <DevicesCrud/>
                </div>
                <div style={{width: '22%'}}>
                    <DeviceMapping/>
                </div>
                <div>
                    <Link to="/Chat">
                        <Button color="primary" style={{
                            position: 'fixed',
                            bottom: '20px',
                            right: '20px',
                        }}>Go to Chat</Button>
                    </Link>
                </div>
            </div>
        </div>
    );
};

export default AdminPage;
