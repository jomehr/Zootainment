import React from "react"
import "../main.css"

export default class Home extends React.Component {

    render() {
        return (
            <div className={"container"}>
                <h1 className={"h1-title"}>Zootainment</h1>
                <p className={"paragraph-text"}>
                    Welcome to Zootainment! The most interactive
                    and fun visitor-animal interaction system worldwide.
                    Install our free android application and try it yourself!
                </p>
            </div>
        );
    }
}