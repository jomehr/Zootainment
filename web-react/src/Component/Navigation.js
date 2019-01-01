import React from "react";
import {Link} from "react-router-dom";
import "../main.css"

const floatRight = {
    cssFloat: "right",
    borderRight: "none"
};

const Navigation = ()=> {
    return (
        <nav className={"navigation-bar"}>
            <div className={"nav-wrapper"}>
                <ul>
                    <li>
                        <Link to={"/"}>
                            <a href={"#home"}>Home</a>
                        </Link>
                    </li>
                    <li>
                        <Link to={"/sample"}>
                            <a href={"#sample"}>Sample</a>
                        </Link>
                    </li>
                    <li style={floatRight}>
                        <Link to={"/about"}>
                            <a href={"about"}>About</a>
                        </Link>
                    </li>
                </ul>
            </div>
        </nav>
    );
};
export  default Navigation;