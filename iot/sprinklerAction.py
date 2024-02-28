import pyrebase
import sys
import time
import RPi.GPIO as GPIO
from numpy import interp

servo = 17

GPIO.setmode(GPIO.BCM)

GPIO.setup(servo,GPIO.OUT)

p=GPIO.PWM(servo,50)

#removed login keys
config = {
  "apiKey": "xxxx",
  "authDomain": "xxxx",
  "databaseURL": "xxxx",
  "storageBucket": "xxxx"
}

firebase = pyrebase.initialize_app(config)

db = firebase.database()

p.start(5)
def sprinkler_handler1(x):
	if x == True:
            print "starting"
            print "on"
            #p.start(2.5)
            p.ChangeDutyCycle(5)
            time.sleep(0.05)
            p.ChangeDutyCycle(7.5)
            time.sleep(0.05)
            p.ChangeDutyCycle(10)
            #time.sleep(0.05)
            #p.ChangeDutyCycle(12.5)
            print "off"
            time.sleep(3)
            p.ChangeDutyCycle(10)
            time.sleep(0.05)
            p.ChangeDutyCycle(7.5)
            time.sleep(0.05)
            p.ChangeDutyCycle(5)
            time.sleep(0.05)
            p.ChangeDutyCycle(2.5)
            time.sleep(0.05)
            p.ChangeDutyCycle(1)
            p.stop()

def setAngle(p, angle):
    angle = max(min(angle, 90), 0)
    tmp = interp(angle, [0, 90], [50, 125])
    p.ChangeDutyCycle(round(tmp/10.0, 1))
    		
def sprinkler_handler2(x):
    if x["data"] == True:
        print "servo start"
        for y in range(0, 90):
            print y, " degree"
            setAngle(p,y)
            time.sleep(0.01)

        time.sleep(3)

        for y in range(90, 0, -1):
            print y, " degree"
            setAngle(p,y)
            time.sleep(0.01)

		
def my_stream_sprinkler():
    print "streaming"
    db.child("/elephants/devices/sprinkler_1/shoot").stream(sprinkler_handler2)

try:
    #sprinkler_handler2(True)
    my_stream_sprinkler()
except KeyboardInterrupt:
    print "finishing"
    GPIO.cleanup()
finally:
    print "cleaning ports"
    #GPIO.cleanup()
