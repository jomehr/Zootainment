import React from "react";

export default class About extends React.Component {

    render() {
        return (
            <div className={"container"}>
                <h1 className={"h1-title"}>About Us</h1>
                <p className={"paragraph-text"}>
                    This is a interdisciplinary project of the TH Köln in the winter semester 2018/19.
                    The participating majors are: Design, Economics and IT.
                    The task is to create a Internet of Things application on the topic of zoo and wildlife.
                    This project is in cooperation with the cologne zoo.
                </p>
            </div>
        )
    }
}