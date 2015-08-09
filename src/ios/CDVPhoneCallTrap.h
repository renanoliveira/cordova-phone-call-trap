

#import <Cordova/CDVPlugin.h>
#import <CoreTelephony/CTCallCenter.h>


@interface CDVPhoneCallTrap : CDVPlugin {
@protected NSString* _eventsCallbackId;
}

@property (nonatomic, strong) CTCallCenter *callCenter;

- (void)pluginInitialize:(CDVInvokedUrlCommand*)command;
- (void)onCall:(CDVInvokedUrlCommand*)command;

@end
