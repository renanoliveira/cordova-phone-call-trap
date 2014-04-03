var PhoneCallTrap = {
	onCall: function() {
		cordova.exec(
			successCallback,
			errorCallback,
			'PhoneCallTrap',
			'onCall',
			[{
				'action': 'João'
			}]
		);
	},

	successCallback: function() {
		console.log("successCallback");
	},

	errorCallback: function() {
		console.log("errorCallback");	
	}
};

module.exports = new PhoneCallTrap;