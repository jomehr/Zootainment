import React, {Component} from 'react';
import {Route} from "react-router-dom";

import Home from "./Component/Home";
import About from "./Component/About";
import Controller from "./Component/Controller"
import Navigation from "./Component/Navigation"

class App extends Component {

    render() {
        return (
            <div>
                <Navigation/>
                <Route exact={true} path={"/"} component={Home}/>
                <Route exact={true} path={"/sample"} component={Controller}/>
                <Route exact={true} path={"/about"} component={About}/>
            </div>
        );
    }
}

export default App;
