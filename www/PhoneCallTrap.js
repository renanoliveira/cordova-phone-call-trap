var PhoneCallTrap = {
    onCall: function(successCallback, errorCallback) {
        errorCallback = errorCallback || this.errorCallback;
        cordova.exec(successCallback, errorCallback, 'PhoneCallTrap', 'onCall', []);
    },

    getLastCall: function(successCallback, errorCallback) {
        errorCallback = errorCallback || this.errorCallback;
        cordova.exec(successCallback, errorCallback, 'PhoneCallTrap', 'getLastCall', []);
    },

    getHistory: function(date, successCallback, errorCallback) {        
        if( typeof date == 'function' ){
            errorCallback = errorCallback || this.errorCallback;
            cordova.exec(date, errorCallback, 'PhoneCallTrap', 'getHistory', [0]);
        } else {
            errorCallback = errorCallback || this.errorCallback;
            cordova.exec(successCallback, errorCallback, 'PhoneCallTrap', 'getHistory', [ typeof date == 'function' ? 0 : date ]);
        }        
    },

    getCallData: function(number, successCallback, errorCallback) {
        errorCallback = errorCallback || this.errorCallback;
        cordova.exec(successCallback, errorCallback, 'PhoneCallTrap', 'getCallData', [number]);
    },

    minimise: function(successCallback, errorCallback) {
        errorCallback = errorCallback || this.errorCallback;
        cordova.exec(successCallback, errorCallback, 'PhoneCallTrap', 'minimise', [] );
    },

    errorCallback: function() {
        console.log("WARNING: PhoneCallTrap errorCallback not implemented");
    }
};

module.exports = PhoneCallTrap;
