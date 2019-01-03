import React from "react";
import {Link} from "react-router-dom";

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
                        <Link to={"/"}>Home</Link>
                    </li>
                    <li>
                        <Link to={"/sample"}>Sample</Link>
                    </li>
                    <li style={floatRight}>
                        <Link to={"/about"}>About</Link>
                    </li>
                </ul>
            </div>
        </nav>
    );
};

export default Navigation;