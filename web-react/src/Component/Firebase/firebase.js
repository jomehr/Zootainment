import app from 'firebase/app';
import "firebase/database"

// Initialize Firebase
let config = {
    apiKey: "xxxx",
    authDomain: "xxxx",
    databaseURL: "xxxx",
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
