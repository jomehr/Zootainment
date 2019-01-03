import app from 'firebase/app';
import "firebase/database"

// Initialize Firebase
//TODO put config data in .env-file if repository goes public
let config = {
    apiKey: "AIzaSyAGI4vn6fOQ_HPwMdkvCVR8X6LK5tAA1tk",
    authDomain: "zootainment-41365.firebaseapp.com",
    databaseURL: "https://zootainment-41365.firebaseio.com",
};

class Firebase {
    constructor() {
        app.initializeApp(config);

        this.db = app.database();
    }

    //firebase database references
    dbCannonRef = (enclosure, cannon) => {
        return this.db.ref(`${enclosure}/devices/${cannon}`);
    };
}

export default Firebase;