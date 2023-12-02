import {BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import LoginPage from "./components/LoginPage";
import AdminPage from "./components/Admin Page/AdminPage";
import ClientPage from "./components/User Page/ClientPage";
import AdminChat from "./components/Admin Page/AdminChat";

function App() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<LoginPage/>}/>
                <Route path="/admin" element={<AdminPage/>}/>
                {/*<Route path="/register" exact component={RegisterPage} />*/}
                <Route path="/client" element={<ClientPage/>}/>
                <Route path="/Chat" element={<AdminChat/>}/>
            </Routes>
        </Router>
    );
}

export default App;
