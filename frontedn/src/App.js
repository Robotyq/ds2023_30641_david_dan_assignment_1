import {BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import LoginPage from "./components/LoginPage";
import AdminPage from "./components/AdminPage";
import ClientPage from "./components/User Page/ClientPage";

function App() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<LoginPage/>}/>
                <Route path="/admin" element={<AdminPage/>}/>
                {/*<Route path="/register" exact component={RegisterPage} />*/}
                <Route path="/client" element={<ClientPage/>}/>
            </Routes>
        </Router>
    );
}

export default App;
