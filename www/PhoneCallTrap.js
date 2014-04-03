var PhoneCallTrap = {
	onCall: function() {
		cordova.exec(
			this.successCallback,
			this.errorCallback,
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

module.exports = PhoneCallTrap;