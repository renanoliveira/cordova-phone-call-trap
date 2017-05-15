Cordova PhoneCall Trap
=======================

It is a Apache Cordova plugin to simplify handling phone call status and events in Android devices.


## Install

    $ cordova plugin add io.gvox.plugin.phonecalltrap


## Quick Example

    PhoneCallTrap.onCall(function(result) {
        console.log("CHANGE STATE: " + result.state);
        console.log("CALLER ID: " + result.number); // only in ringing state

        switch (result.state) {
            case "RINGING":
                console.log("Phone is ringing", result.number);
                break;
            case "OFFHOOK":
                console.log("Phone is off-hook");
                break;

            case "IDLE":
                console.log("Phone is idle");
                break;
        }
    });

NOTE: You must add these two permissions in order to receive caller id:

    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    <uses-permission android:name="android.permission.READ_PRIVILEGED_PHONE_STATE" />

## Supported platforms

- Android 2.3.3 or higher


## References

We have tried PhoneListener but it is only compatible with Phonegap 1.6 and does not work with new Apache Cordova versions. Also, its deployment isn't as easy as an Apache Cordova plugin should be. We are thankful for their work, though.

https://github.com/devgeeks/PhoneListener


## License

Cordova PhoneCall Trap is released under the [MIT License](http://www.opensource.org/licenses/MIT).
