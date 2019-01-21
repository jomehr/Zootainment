import pyrebase
import time
import RPi.GPIO as GPIO

servo = 22

GPIO.setmode(GPIO.BOARD)

GPIO.setup(servo,GPIO.OUT)

p=GPIO.PWM(servo,50)

config = {
  "apiKey": "AAAANy-Mmdo:APA91bGOKuaMgdvtaT3HghLheCX6Q92udrby-l9n7fjdxICUvl5hwUuN573WdqCUutCAI74ePTTaF5GodiOIZnbNQNaTn_LmnSqvcbppBB5P9O3XjEfemrvU94GAGCFfxTwZQqUW3J7x",
  "authDomain": "zootainment-41365.firebaseapp.com",
  "databaseURL": "https://zootainment-41365.firebaseio.com/",
  "storageBucket": "zootainment-41365.appspot.com"
}

firebase = pyrebase.initialize_app(config)

db = firebase.database()

def sprinkler_handler(x):
	if x["data"] == True:
            p.start(10)
            time.sleep(3)
            p.start(7.5)
    		

		
my_stream_sprinkler = db.child("/elephants/devices/sprinkler_1/shoot").stream(sprinkler_handler)

