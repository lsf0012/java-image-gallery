import React, {Component} from 'react';

export default class Login extends Component {
    state = {
        currentUserName: '',
        currentPassword: ''
    };

    login() {
        this.props.setLoggedInUser(this.state.currentUserName,
            this.state.currentPassword,
            this.props.history,
            this.isAdminLogin());
    }

    isAdminLogin() {
        let isAdminLogin = this.props.match.params.type === 'admin';
        return isAdminLogin;
    }

    handleUserNameChange(userName) {
        this.setState({
            currentUserName: userName
        });
    }

    handlePasswordChange(password) {
        this.setState({
            currentPassword: password
        });
    }

    render() {
        let loginType = this.isAdminLogin() ? 'Admin' : 'Non-Admin';

        return (
            <div>
                <h1>Please log in to continue:</h1>
                <h3>Login type: {loginType}</h3>
                <div style={{color: 'red'}}>{this.props.loginError ? this.props.loginError : null}</div>
                <input type="text" placeholder="User name"onChange={(event) => this.handleUserNameChange(event.target.value)}/>
                <input type="text" placeholder="Password" onChange={(event) => this.handlePasswordChange(event.target.value)}/>
                <button onClick={() => this.login()}>Log in</button>
            </div>
        );
    }
}
