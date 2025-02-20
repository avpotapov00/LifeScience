import React from "react";
import './header.css'
import {connect} from "react-redux";
import Logged from "./logged";
import UnLogged from "./unlogged";
import {getAuthorizedUserThunk, logoutUser} from "../../redux/auth-reducer";

class Header extends React.Component {
    constructor(props) {
        super(props);
        this.getUserPart = this.getUserPart.bind(this);
    }

    getUserPart() {
        if (!this.props.user) {
            return <UnLogged/>;
        }
        return <Logged {...this.props} user={this.props.user}/>;
    }

    refreshUser() {
        this.props.getAuthorizedUserThunk();
    }

    componentDidMount() {
        this.refreshUser()
    }

    shouldComponentUpdate(nextProps, nextState, nextContext) {
        return nextProps.isAuthorized !== this.props.isAuthorized;
    }

    render() {
        return (
            <div className="d-flex align-items-center justify-content-between">
                <div className="header__logo_text">Life Science</div>
                {this.getUserPart()}
            </div>
        );
    }
}

const mapStateToProps = (state) => {
    return {
        user: state.auth.user,
        isAuthorized: state.auth.isAuthorized
    };
};

export default connect(mapStateToProps, {logoutUser, getAuthorizedUserThunk})(Header);
