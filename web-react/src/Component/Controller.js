import React from "react"

import {withFirebase} from "./Firebase"

let timer;

const INITIAL_STATE = {
    move_left: false,
    move_right: false,
    error: null
};

class Controller extends React.Component {
    constructor(props) {
        super(props);

        this.state = {...INITIAL_STATE}
    }

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
                    <button className={"shoot left"}
                            onMouseDown={() => moveLeft(this.props, true)}
                            onMouseUp={() => {
                                stopInterval();
                                this.props.firebase
                                    .dbCannonRef("elephants", "cannon_1")
                                    .update({
                                        move_left: false
                                    });
                            }}>LEFT
                    </button>
                    <button className={"shoot"} onClick={() => shoot(this.props)}>SHOOT</button>
                    <button className={"shoot right"}
                            onMouseDown={() => moveRight(this.props, true)}
                            onMouseUp={() => {
                                stopInterval();
                                this.props.firebase
                                    .dbCannonRef("elephants", "cannon_1")
                                    .update({
                                        move_right: false
                                    });
                            }}>RIGHT
                    </button>
                </div>
            </div>
        );
    }
}

function stopInterval() {
    console.log("stopping");
    clearInterval(timer)
}

function moveLeft(ref, left) {
    timer = setInterval(function fn() {
        console.log("LEFT");
        ref.firebase
            .dbCannonRef("elephants", "cannon_1")
            .update({
                move_left: left
            });
        return fn;
    }(), 500)
}

function moveRight(ref, right) {
    timer = setInterval(function fn() {
        console.log("RIGHT");
        ref.firebase
            .dbCannonRef("elephants", "cannon_1")
            .update({
                move_right: right
            });
        return fn;
    }(), 500)
}

function shoot(ref) {
    console.log("SHOOT");
    ref.firebase
        .dbCannonRef("elephants", "cannon_1")
        .update({
            shoot: true
        }, function (error) {
            if (error) alert(error);
            else {
                sleep(1000).then(() => {
                    ref.firebase
                        .dbCannonRef("elephants", "cannon_1")
                        .update({
                            shoot: false
                        })
                });
            }
        })
}

function sleep(time) {
    return new Promise((resolve) => setTimeout(resolve, time));
}

export default withFirebase(Controller);