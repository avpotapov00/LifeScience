import './App.css';
import React, {useState} from "react";
import Header from './components/Header/header';
import Register from "./components/Register/register";
import {BrowserRouter as Router, Route} from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';
import Navbar from "./components/Navbar/navbar";
import Method from "./components/Method/method";
import ProfilePage from "./components/ProfilePage/profilePage";
import Login from "./components/Login/login";
import CategoriesContainer from "./components/Main/Categories/categoriesContainer";
import Home from "./components/Main/HomePage/homePage";
import UserPage from "./components/Main/UserPage/userPage";

function App() {
    // Fetch auth-data from local storage in case user was already logged in
    const authData = JSON.parse(localStorage.getItem('auth-data'));
    const [loggedUser, setLoggedUser] = useState(authData ? authData : {});
    const subFolders = [{
        path: "/",
        name: "Home",
    }]

    return (
        <Router>
            <Header/>
            <Navbar/>
            <Route exact={true} path="/" component={Home}/>
            <Route path="/register" component={Register}/>
            <Route path="/login" render={() => <Login />}/>
            <Route path="/categories/:categoryId?" render={() => <CategoriesContainer />}/>
            <Route path="/bradford-assay">
                <Method link="/bradford-assay" articleId={1}/>
            </Route>
            <Route exact={true} path="/my-account" render={() => <ProfilePage user={loggedUser}/>}/>
        </Router>
    );
};

export default App;
