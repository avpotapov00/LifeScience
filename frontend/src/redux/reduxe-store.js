import {applyMiddleware, combineReducers, createStore} from "redux";
import thunkMiddleware from 'redux-thunk';
import categoryReducer from "./category-reducer";
import authReducer from "./auth-reducer";
import methodReducer from "./method-reducer";
import sectionReducer from "./section-reducer";
import usersReducer from "./users-reducer";
import initReducer from "./init-reducer";

let rootReducer = combineReducers({
    categoryPage: categoryReducer,
    usersPage: usersReducer,
    auth: authReducer,
    method: methodReducer,
    section: sectionReducer,
    init: initReducer
});

//Вносим свои reducers в rootReducer

let store = createStore(rootReducer, applyMiddleware(thunkMiddleware));

export default store;