import pyrebase

#removed login keys
config = {
  "apiKey": "xxxx",
  "authDomain": "xxxx",
  "databaseURL": "xxxx",
  "storageBucket": "xxxx"
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


