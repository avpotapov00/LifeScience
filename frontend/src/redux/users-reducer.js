import {usersApi} from "../api/users-api";
import {getAuthorizedUserThunk} from "./auth-reducer";

const GET_USER = 'GET_USER';
const ERROR = 'USER_ERROR';
const GET_USER_FAVOURITES = 'GET_USER_FAVOURITES';

const initialState = {
    user: null,
    userFavourites: []
}

const usersReducer = (state = initialState, action) => {

    switch (action.type) {
        case ERROR:
            return {
                ...state,
                trouble: action.trouble
            };
        case GET_USER:
            return {
                ...state,
                user: action.user
            };
        case GET_USER_FAVOURITES:
            return {
                ...state,
                userFavourites: action.userFavourites
            }
        default:
            return {
                ...state
            };
    }
};

export const getError = (trouble, reason) => {
    return {type: reason, trouble: trouble}
};

export const getUser = (user) => {
    return {type: GET_USER, user: user}
};

export const getUserFavourites = (favourites) => {
    return {type: GET_USER_FAVOURITES, userFavourites: favourites}
};

export const getUsersThunk = (id) => async (dispatch) => {
    let response = await usersApi.getUser(id);
    if (response.status !== 200) {
        dispatch(getError(response, ERROR))
        return;
    }
    dispatch(getUser(response.data));
};

export const getUserFavouritesThunk = (id) => async (dispatch) => {
    let response = await usersApi.getUserFavorites(id);
    if (response.status !== 200) {
        dispatch(getError(response, ERROR))
        return;
    }
    dispatch(getUserFavourites(response.data));
};

export const patchUserDataThunk = (id, data) => async (dispatch) => {
    let response = await usersApi.patchToUserData(id, data);
    if (response.status !== 200) {
        dispatch(getError(response, ERROR))
        return;
    }
    dispatch(getAuthorizedUserThunk())
}

export const patchToUserFavouritesThunk = (userId, articleId) => async (dispatch) => {
    let response = await usersApi.patchToUserFavorites(userId, articleId);
    if (response.status !== 200) {
        dispatch(getError(response, ERROR))
        return;
    }
    dispatch(getUserFavouritesThunk(userId));
}

export const deleteFromUserFavouritesThunk = (userId, articleId) => async (dispatch) => {
    let response = await usersApi.deleteFromUserFavourites(userId, articleId);
    if (response.status !== 200) {
        dispatch(getError(response, ERROR));
        return;
    }
    dispatch(getUserFavouritesThunk(userId));
}

export default usersReducer;
