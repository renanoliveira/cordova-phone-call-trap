

#import <Cordova/CDVPlugin.h>
#import <CoreTelephony/CTCall.h>

@interface CDVPhoneCallTrap : CDVPlugin {
@protected NSString* _eventsCallbackId;
}

@property (nonatomic, strong) CTCallCenter* callCenter;

@end
