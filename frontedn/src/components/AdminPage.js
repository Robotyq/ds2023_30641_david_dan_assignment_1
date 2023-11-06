import React, {useEffect} from 'react';
import UsersCrud from "./User Management/users-crud";
import DevicesCrud from "./Device Management/devices-crud";
import DeviceMapping from "./DeviceMapping";
import {useNavigate} from "react-router-dom";

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
            </div>
        </div>
    );
};

export default AdminPage;
