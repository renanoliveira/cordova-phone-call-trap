Cordova PhoneCall Trap
=======================

It is a Apache Cordova plugin to easealy work with phone call status and events with Android devices.


## Install

    $ cordova plugin add io.gvox.plugin.phonecalltrap


## Quick Example

    PhoneCallTrap.onCall(function(state) {
        console.log("CHANGE STATE: " + state);

        switch (state) {
            case "RINGING":
                console.log("Phone is ringing");
                break;
            case "OFFHOOK":
                console.log("Phone is off-hook");
                break;

            case "IDLE":
                console.log("Phone is idle");
                break;
        }
    });


## Supported platforms

- Android 2.3.3 or higher


## References

We have tryed PhoneListener but it was made to work with Phonegap 1.6 and does not work with new Apache Cordova versions. As well its deployment is not easy as a Apache Cordova plugin should be. Although we are thankful =D

https://github.com/devgeeks/PhoneListener


## License

Cordova PhoneCall Trap is released under the [MIT License](http://www.opensource.org/licenses/MIT).
