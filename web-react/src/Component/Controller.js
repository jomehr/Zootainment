import React from "react"
import "../main.css"

import firebase from "firebase"

// Initialize Firebase
let config = {
    apiKey: "AIzaSyAGI4vn6fOQ_HPwMdkvCVR8X6LK5tAA1tk",
    authDomain: "zootainment-41365.firebaseapp.com",
    databaseURL: "https://zootainment-41365.firebaseio.com",
};
firebase.initializeApp(config);

let databaseRef = firebase.database().ref("enclosure_1/devices/cannon_1");

export default class Controller extends React.Component {

    render() {
        return (
            <div>
                <div className={"container"}>
                    <h1 className={"h1-title"}>Try the Feeding Cannon!</h1>
                    <p className={"paragraph-text"}>
                        Shoot the food pallets in the enclosure and watch the magic happen.
                        If you like it then download our app in the appstore!
                    </p>
                </div>
                <div className={"controller"}>
                    <h4 className={"h3-title"}>You have direct control over the cannon now!</h4>
                    <button className={"shoot left"} onClick={() => moveLeft()}>LEFT</button>
                    <button className={"shoot"} onClick={() => shoot()}>SHOOT</button>
                    <button className={"shoot right"} onClick={() => moveRight()}>RIGHT</button>
                </div>
            </div>
        );
    }
}

function moveLeft() {
    console.log("LEFT");
    databaseRef.update({
        move_right: false,
        move_left: true
    }, function (error) {
       if (error) {
           alert(error)
       } else {
           sleep(2000).then(() => {
               databaseRef.update({
                   move_left: false
               })
           })
       }
    })
}

function shoot() {
    console.log("SHOOT")
    databaseRef.update({
        shoot: true,
    }, function (error) {
        if (error) {
            alert(error)
        } else {
            sleep(1000).then(() => {
                databaseRef.update({
                    shoot: false
                })
            });
        }
    })
}

function moveRight() {
    console.log("RIGHT")
    databaseRef.update({
        move_right: true,
        move_left: false
    }, function (error) {
        if (error) {
            alert(error)
        } else {
            sleep(2000).then(() => {
                databaseRef.update({
                    move_right: false
                })
            });
        }
    })
}

function sleep (time) {
    return new Promise((resolve) => setTimeout(resolve, time));
}