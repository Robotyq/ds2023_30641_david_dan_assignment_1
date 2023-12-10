import React, {useEffect, useState} from 'react';
import axios from 'axios';
import {useNavigate} from "react-router-dom";

const LoginPage = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        const user = JSON.parse(sessionStorage.getItem('loggedUser'));
        const role = user ? user.role : ' ';
        handleRedirect(role)
    }, []);
    const handleRedirect = (role) => {
        if (role === "CLIENT")
            navigate("/client");
        if (role === "ADMIN")
            navigate("/admin");
        // if(role===" ")
        //     navigate("/");
    }


    const handleLogin = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post('http://localhost:8081/user/login', {
                email: username,
                pass: password
            });
            let jwt = response.data.token;
            let user = response.data.user;
            console.log(jwt);
            localStorage.setItem('jwt', jwt);
            const role = user ? user.role : ' ';
            if (role === 'CLIENT' || role === 'ADMIN') {
                sessionStorage.setItem('loggedUser', JSON.stringify(user));
                handleRedirect(role)
            }
        } catch (error) {
            if (error.response && error.response.status === 404) {
                alert('Invalid username or password');
            } else {
                console.error('Error:', error);
                alert('An error occurred while logging in');
            }
        }
    }


    const styles = `
        .login-page {
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }

        .login-form {
            border: 1px solid #ccc;
            padding: 20px;
            border-radius: 5px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            max-width: 300px;
        }

        .login-form h2 {
            text-align: center;
        }

        .login-form label {
            margin-bottom: 6px;
            display: block;
        }

        .login-form input {
            width: 100%;
            padding: 8px;
            margin-bottom: 15px;
            border: 1px solid #ccc;
            border-radius: 4px;
        }

        .login-form button {
            width: 100%;
            padding: 10px;
            background-color: #3498db;
            color: #fff;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            transition: background-color 0.3s;
        }

        .login-form button:hover {
            background-color: #2980b9;
        }
    `;

    return (
        <div className="login-page">
            <style>{styles}</style>
            {(
                <form onSubmit={handleLogin} className="login-form">
                    <h2>Login</h2>
                    <div>
                        <label>Username:</label>
                        <input
                            type="text"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                        />
                    </div>
                    <div>
                        <label>Password:</label>
                        <input
                            type="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                        />
                    </div>
                    <button type="submit">Login</button>
                </form>
            )}
        </div>
    );
};

export default LoginPage;
