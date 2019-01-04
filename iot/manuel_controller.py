import time
import grovepi
from numpy import interp

#INPUT SENSORS
anglesensor = 2 #A2
button = 8      #D8

#OUTPUT
led = 5         #D5 (PWM)

grovepi.pinMode(led, "OUTPUT")
time.sleep(1)

analog = 0
digital = 0
angle = 0

while True:
    try:
        #read sensor data
        analog=grovepi.analogRead(anglesensor)
        digital=grovepi.digitalRead(button)

        #convert analog data to angle for servo
        angle=interp(analog, [0,1023], [-90,90])

        #test output
        print(digital, analog, angle)
        grovepi.analogWrite(led, analog//4)

    except IOError:
        print("ERROR")
