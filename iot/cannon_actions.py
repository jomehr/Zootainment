from firebase_streaming import Firebase

# Sample callback function
def move_left(x):
    	if x == '{"path":"/","data":true}':
		print "left"
def move_right(x):
    	if x == '{"path":"/","data":true}':
		print "right"
def shoot(x):
    	if x == '{"path":"/","data":true}':
		print "shoot"
		
fb = Firebase('https://zootainment-41365.firebaseio.com/cannon_actions/')


movement_left = fb.child("movement_left").listener(move_left)
movement_right = fb.child("movement_right").listener(move_right)
shoot = fb.child("shoot").listener(shoot)

movement_left.start()
movement_right.start()
shoot.start()

raw_input("ENTER to stop...")

movement_left.stop()
movement_right.stop()
shoot.stop()


