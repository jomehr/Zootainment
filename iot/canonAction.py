import pyrebase

config = {
  "apiKey": "AAAANy-Mmdo:APA91bGOKuaMgdvtaT3HghLheCX6Q92udrby-l9n7fjdxICUvl5hwUuN573WdqCUutCAI74ePTTaF5GodiOIZnbNQNaTn_LmnSqvcbppBB5P9O3XjEfemrvU94GAGCFfxTwZQqUW3J7x",
  "authDomain": "zootainment-41365.firebaseapp.com",
  "databaseURL": "https://zootainment-41365.firebaseio.com/",
  "storageBucket": "zootainment-41365.appspot.com"
}

left = False
right = False

firebase = pyrebase.initialize_app(config)

def move_left():
	print "left"
	

def move_right():
	print "right"


db = firebase.database()

def move_left_handler(x):
	global left
	if x["data"] == True:
    		left = True
		move_left()
	elif x["data"] == False:
		left = False

def move_right_handler(x):
	global right
    	if x["data"] == True:
		right = True
		move_right()
    	elif x["data"] == False:
		right = False

def shoot_handler(x):
    	if x["data"] == True:
		print "shoot"
		
my_stream_left = db.child("/cannon_actions/movement_left").stream(move_left_handler)
my_stream_right = db.child("/cannon_actions/movement_right").stream(move_right_handler)
my_stream_shoot = db.child("/cannon_actions/shoot").stream(shoot_handler)

while True:
	if left:
		move_left()
	if right:
		move_right()


