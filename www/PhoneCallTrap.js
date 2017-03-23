var PhoneCallTrap = {
    onCall: function(successCallback, errorCallback) {
        errorCallback = errorCallback || this.errorCallback;
        cordova.exec(successCallback, errorCallback, 'PhoneCallTrap', 'onCall', []);
    },

    errorCallback: function() {
        console.log("WARNING: PhoneCallTrap errorCallback not implemented");
    },

    getCurrentState: function(successCallback, errorCallback) {
        errorCallback = errorCallback || this.errorCallback;
        cordova.exec(successCallback, errorCallback, 'PhoneCallTrap', 'getCurrentState', []);
    }
};

module.exports = PhoneCallTrap;
