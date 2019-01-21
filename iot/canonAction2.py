import pyrebase
import time
import serial
from grovepi import *

relay = 7
pinMode(relay,"OUTPUT")

ultrasonic_ranger = 4

distance = 1

config = {
  "apiKey": "AAAANy-Mmdo:APA91bGOKuaMgdvtaT3HghLheCX6Q92udrby-l9n7fjdxICUvl5hwUuN573WdqCUutCAI74ePTTaF5GodiOIZnbNQNaTn_LmnSqvcbppBB5P9O3XjEfemrvU94GAGCFfxTwZQqUW3J7x",
  "authDomain": "zootainment-41365.firebaseapp.com",
  "databaseURL": "https://zootainment-41365.firebaseio.com/",
  "storageBucket": "zootainment-41365.appspot.com"
}

left = False
right = False

verbindung = serial.Serial('/dev/ttyACM0', 9600)
verbindung.isOpen()
time.sleep(5)

firebase = pyrebase.initialize_app(config)

def move_left():
	verbindung.write('1')
	

def move_right():
	verbindung.write('0')


db = firebase.database()

def move_left_handler(x):
	global left
	if x["data"] == True:
    		left = True
	elif x["data"] == False:
		left = False

def move_right_handler(x):
	global right
    	if x["data"] == True:
		right = True
    	elif x["data"] == False:
		right = False

def shoot_handler(x):
    	if x["data"] == True:
		digitalWrite(relay,1)		
                time.sleep(4)
                digitalWrite(relay,0)		
		
my_stream_left = db.child("/elephants/devices/cannon_1/move_left").stream(move_left_handler)
my_stream_right = db.child("/elephants/devices/cannon_1/move_right").stream(move_right_handler)
my_stream_shoot = db.child("/elephants/devices/cannon_1/shoot").stream(shoot_handler)

try:
        while True:
                if left:
                        move_left()
                        time.sleep(0.5)
                        
                if right:
                        move_right()
                        time.sleep(0.5)
                        
                if grovepi.ultrasonicRead(ultrasonic_ranger)< distance:
                        db.child("/elephants/devices/cannon_1/active").update({"false"})
                else:
                        db.child("/elephants/devices/cannon_1/active").update({"true"})
                                                                               
except KeyboardInterrupt:
        verbindung.close()
        my_stream_left.close()
        my_stream_right.close()
        my_stream_shoot.close()
