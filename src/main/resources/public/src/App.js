import React, {Component} from 'react';
import './App.css';
import {Redirect, Route, Switch} from "react-router-dom";
import Main from "./components/Main";
import Login from "./components/Login";
import UploadImage from "./components/UploadImage";
import ViewImages from "./components/ViewImages";
import Admin from "./components/Admin";

export default class App extends Component {
  state = {
    loggedInUser: null,
    loginError: null
  };

  setLoggedInUser(attemptedUserName, attemptedPassword, browserHistory, isAdminLogin) {
    let usersUrl = "http://ec2-3-20-35-99.us-east-2.compute.amazonaws.com:8888";
    if (isAdminLogin) {
      usersUrl += '/admin/users';
    } else {
      usersUrl += '/users';
    }

    fetch(usersUrl)
        .then(data => data.json())
        .then(data => {
          data.forEach(user => {
            const {username, password} = user;
            console.log(user);
            if (username === attemptedUserName && password === attemptedPassword) {
              this.setState(() => ({
                loggedInUser: username,
                loginError: null
              }));
              browserHistory.push('/');
            } else {
              this.setState(() => ({
                loginError: 'Failed to log in. Please verify your user name and password and try again.'
              }));
            }
          });
        });
  }

  renderComponentIfLoggedIn(component, isAdmin) {
    if (this.state.loggedInUser === null) {
      if (isAdmin) {
        return <Redirect to="/login/admin" />;
      } else {
        return <Redirect to="/login/non-admin" />;
      }
    }

    return component;
  }

  render() {
    return (
        <div className="App">
          <Switch>
            <Route exact path="/"
                   render={(props) => this.renderComponentIfLoggedIn(<Main {...props}/>, false)}/>)} } />
            <Route exact path="/login/:type"
                   render={(props) => <Login {...props}
                                             loginError={this.state.loginError}
                                             setLoggedInUser={this.setLoggedInUser.bind(this)}/>}/>
            <Route exact path="/upload-image"
                   render={(props) => this.renderComponentIfLoggedIn(<UploadImage loggedInUser={this.state.loggedInUser}/>, false)}/>)} />
            <Route exact path="/view-images"
                   render={(props) => this.renderComponentIfLoggedIn(<ViewImages loggedInUser={this.state.loggedInUser}/>, false)}/>)} />
            <Route exact path="/admin"
                   render={(props) => this.renderComponentIfLoggedIn(<Admin/>, true)}/>)} />
          </Switch>
        </div>
    );
  }
}
