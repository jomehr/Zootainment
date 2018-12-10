#firebase-python-streaming
######A nice, easy Python Firebase integration

####Original Repo
https://github.com/shariq/firebase-python

Supports streaming data.

Requires `requests` and `sseclient`, which are on pip. If you don't know what that is, don't worry; just run `./setup.sh`. Made for Python 2.7.

First create a Firebase here:
https://www.firebase.com/signup/

(warning: The free level of Firebase only allows up to 50 concurrent connections. Don't hit this limit!)


## Firebase object

Instaniate the object with using your firebase URL as follows:

 ```python
 firebase = Firebase('https://<YOUR_FIREBASE>.firebaseio.com/')
 ```

Code from the upstream repo supports different URL formats; this functionality is deprecated.

 From here you can make `get`, `put`, and `patch` calls on the object. Additionally, you can navigate to child levels using `child`.  

##child

Use `firebase.child("child")` to navigate. This returns a reference to another firebase object with the URL of the child object.

```python

firebase = Firebase('https://myfirebase.firebaseio.com/')
users = firebase.child("users")

andrew = users.child("andrewsosa001")

```

Since children objects are Firebase objects, you can use all the normal get/put/patch commands.

##get and put

These commands have been object-oriented since the previous library. They are now called from the Firebase object, and do not require URLs to be provided.

`get` gets the value(s) of a Firebase it's given url, and `put` sets the value of a Firebase's url to some data.

```python
>>> from firebase_streaming import Firebase

# Firebase object
>>> myFirebase = Firebase('https://myfirebase.firebaseio.com/')
>>> print myFirebase.get()  # this is an empty Firebase
None

>>> myFirebase.put('This is my firebase!')  # can take a string
>>> print firebase.get(URL)
This is my firebase!

>>> myFirebase.put({"Who's firebase?": "My firebase!"})  # or a dictionary
>>> print myFirebase.get(URL)
{"Who's firebase?": "My firebase!"}

# Sub-firebase.
>>> myColors = myFirebase.child("colors")
>>> print myColors.get()

>>> myColors.put({'color': 'red'})
>>> print myColors.get()
{'color': 'red'}

```


##patch

`patch` adds new key value pairs to an existing Firebase, without deleting the old key value pairs.

```python
>>> firebase = Firebase('https://myfirebase.firebaseio.com/')
>>> print firebase.get(URL)
None

>>> firebase.patch({'taste': 'tibetan'})
>>> print firebase.get()
{u'taste': u'tibetan'}

>>> firebase.patch({'size': 'tumbly})  # patching does not overwrite
>>> print firebase.get()
{u'taste': u'tibetan', u'size': u'tumbly'}
```



##listener

The Firebase `listener` provides streaming functionality. It takes an optional callback function which it calls on every update to the Firebase object.

See `sample.py` for use. ``



##URLs

**This method has been deprecated in this library, and only exists to serve existing code from the previous version. Do not use these formats to instantiate your Firebase objects.**

All URLs are internally converted to a Firebase URL format. This is done by the `firebaseURL` method. This method supports the following inputs.

```python
>>> import firebase

>>> print firebase.firebaseURL('bony-badger')
https://bony-badger.firebaseio.com/.json

>>> print firebase.firebaseURL('bony-badger/bones/humerus')
https://bony-badger.firebaseio.com/bones/humerus.json

>>> print firebase.firebaseURL('bony-badger.firebaseio.com/')
https://bony-badger.firebaseio.com/.json
```

In this library version, the instantition name must end with `.com/`, as the `.child()` function appends the current Firebase's name with the child's directory. Because the name must end with `/`, the above formats are not supported by this library.
