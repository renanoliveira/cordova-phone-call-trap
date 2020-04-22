Cordova PhoneCall Trap
=======================

Forked from https://github.com/renanoliveira/cordova-phone-call-trap, It is a Apache Cordova plugin to simplify handling phone call status and events in Android devices.
The original dint have the capacity to receive the calling number and I was too lazy to ask for a PR so here we are. Enjoy


## Install

    $ cordova plugin add cozzbie.plugin.phonecalltrap


## Quick Example
    Angular 1: var PhoneCallTrap = cordova.plugins.PhoneCallTrap;
    Angular 2: declare var PhoneCallTrap:any;

    PhoneCallTrap.onCall(function(obj) {
        
        var callObj = JSON.parse(obj),
            state = callObj.state,
            callingNumber = callObj.incomingNumber;

        switch (state) {
            case "RINGING":
                console.log("Phone is ringing", callingNumber);
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


## License

Cordova PhoneCall Trap is released under the [MIT License](http://www.opensource.org/licenses/MIT).
