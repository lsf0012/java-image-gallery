import React, {Component} from 'react';
import './ViewImages.css';
import NavMenu from "./NavMenu";

export default class ViewImages extends Component {
    state = {
        images: []
    };

    componentDidMount() {
        this.getImages();
    }

    getImages() {
        fetch(`http://ec2-3-20-35-99.us-east-2.compute.amazonaws.com:5000/images/${this.props.loggedInUser}`, {
            "headers": {
                "accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
                "accept-language": "en-US,en;q=0.9",
                "cache-control": "max-age=0",
                "upgrade-insecure-requests": "1"
            },
            "referrerPolicy": "no-referrer-when-downgrade",
            "body": null,
            "method": "GET",
            "mode": "cors",
            "credentials": "omit"
        })
            .then(response => response.json())
            .then(data => this.setState({
                images: data
            }));
    }

    deleteImage(image) {
        const imageFileName = /[^/]*$/.exec(image)[0];

        fetch(`http://ec2-3-20-35-99.us-east-2.compute.amazonaws.com:5000/images/${imageFileName}`, {
            method: 'DELETE',
            redirect: 'follow'
        })
            .then(response => response.text())
            .then(() => {
                alert('Image deleted');
                this.getImages();
            })
            .catch(error => console.log('error', error));
    }

    processImages() {
        return this.state.images
            .map(image => {
                return (
                    <div className="image-container">
                        <a href={image} target="_blank">
                            <img className="image"  key={image} alt="gallery-image" src={image}/>
                        </a>
                        <button onClick={() => this.deleteImage(image)}>Delete image</button>
                    </div>
                );
            });
    }

    render() {
        const processedImages = this.processImages();

        return (
            <div>
                <NavMenu/>
                <h1>Images</h1>
                <div className="images">
                    {processedImages}
                </div>
            </div>
        );
    }
}
