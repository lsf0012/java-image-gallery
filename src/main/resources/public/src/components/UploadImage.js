import React, {Component} from 'react';
import $ from "jquery";
import NavMenu from "./NavMenu";

export default class UploadImage extends Component {
    changeFile(e) {
        this.setState({csvFile: e.target.files[0]});
    }

    importFile() {
        var data = new FormData();
        data.append('file', this.state.csvFile);
        $.ajax({
            type: "POST",
            url: `http://ec2-3-20-35-99.us-east-2.compute.amazonaws.com:8888/images/upload/${this.props.loggedInUser}`,
            data: data,
            processData: false,
            contentType: false,
            success : () => {
                alert('Image uploaded successfully');
            }
        });
    }

    render() {
        return (
            <div>
                <NavMenu/>
                <h1>Upload image</h1>
                <div>
                    <input type="file" onChange={(event) => this.changeFile(event)}/>
                    <button onClick={() => this.importFile()}>Upload Image</button>
                </div>
            </div>
        );
    }
}
