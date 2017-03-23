

#import "CDVPhoneCallTrap.h"
#import <CoreTelephony/CTCallCenter.h>
#import <CoreTelephony/CTCall.h>


@implementation CDVPhoneCallTrap

//handle calls
-(void)onCall:(CDVInvokedUrlCommand*)command
{
    self.callCenter = [[CTCallCenter alloc] init];

    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(callReceived:) name:CTCallStateIncoming object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(callEnded:) name:CTCallStateDisconnected object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(callConnected:) name:CTCallStateConnected object:nil];

    self.callCenter.callEventHandler = ^(CTCall *call){
        
        NSString *callState;
        
        if ([call.callState isEqualToString: CTCallStateConnected])
        {
            NSLog(@"call CTCallStateConnected - OFFHOOK");
            callState = @"OFFHOOK";
        }
        else if ([call.callState isEqualToString: CTCallStateDialing])
        {
            NSLog(@"call CTCallStateDialing - OFFHOOK");
            callState = @"OFFHOOK";
        }
        else if ([call.callState isEqualToString: CTCallStateDisconnected])
        {
            NSLog(@"call CTCallStateDisconnected - IDLE");
            callState = @"IDLE";
        }
        else if ([call.callState isEqualToString: CTCallStateIncoming])
        {
            NSLog(@"call CTCallStateIncoming - RINGING");
            callState = @"RINGING";
        }
        else  {
            NSLog(@"call NO - IDLE");
            callState = @"IDLE";
        }
        
        CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:callState];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    };
}

-(void)getCurrentState:(CDVInvokedUrlCommand*)command
{
    [self.commandDelegate runInBackground:^{
        NSString *callState;
        callState = @"IDLE";

        CTCallCenter *callCenter = [[CTCallCenter alloc] init];
        for (CTCall *call in callCenter.currentCalls) {
            if (![callState isEqual: @"OFFHOOK"]) {
                if ([call.callState isEqualToString: CTCallStateConnected])
                {
                    NSLog(@"getCurrentState: call CTCallStateConnected - OFFHOOK");
                    callState = @"OFFHOOK";
                }
                else if ([call.callState isEqualToString: CTCallStateDialing])
                {
                    NSLog(@"getCurrentState: call CTCallStateDialing - OFFHOOK");
                    callState = @"OFFHOOK";
                }
                else if ([call.callState isEqualToString: CTCallStateIncoming])
                {
                    NSLog(@"getCurrentState: call CTCallStateIncoming - RINGING");
                    callState = @"RINGING";
                }
            }
        }

        [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:callState] callbackId:command.callbackId];
    }];
}

@end
