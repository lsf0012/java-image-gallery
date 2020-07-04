import React, {Component} from 'react';
import {Route, Switch} from "react-router-dom";
import {Link} from "react-router-dom";

export default class Main extends Component {
    render() {
        return (
            <div>
                Main menu page
                <div>
                    <Link to="/upload-image">Upload Image</Link>
                </div>
                <div>
                    <Link to="/view-images">View Images</Link>
                </div>
                <div>
                    <Link to="/admin">Admin</Link>
                </div>
            </div>
        );
    }
}
