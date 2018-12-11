import pyrebase

config = {
  "apiKey": "AAAANy-Mmdo:APA91bGOKuaMgdvtaT3HghLheCX6Q92udrby-l9n7fjdxICUvl5hwUuN573WdqCUutCAI74ePTTaF5GodiOIZnbNQNaTn_LmnSqvcbppBB5P9O3XjEfemrvU94GAGCFfxTwZQqUW3J7x",
  "authDomain": "zootainment-41365.firebaseapp.com",
  "databaseURL": "https://zootainment-41365.firebaseio.com/",
  "storageBucket": "zootainment-41365.appspot.com"
}

firebase = pyrebase.initialize_app(config)

db = firebase.database()

def move_left(x):
    	if x["data"] == True:
		print "left"
def move_right(x):
    	if x["data"] == True:
		print "right"
def shoot(x):
    	if x["data"] == True:
		print "shoot"
		
my_stream = db.child("/cannon_actions/movement_left").stream(move_left)
my_stream = db.child("/cannon_actions/movement_right").stream(move_right)
my_stream = db.child("/cannon_actions/shoot").stream(shoot)

