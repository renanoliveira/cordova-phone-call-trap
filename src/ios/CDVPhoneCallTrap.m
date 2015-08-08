

#import "CDVPhoneCallTrap.h"
#import <CoreTelephony/CTCall.h>


@implementation CDVPhoneCallTrap

//Initialize the plugin
- (void)pluginInitialize
{
    self.callCenter = [[CTCallCenter alloc] init];
    [self handleCall];
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(callReceived:) name:CTCallStateIncoming object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(callEnded:) name:CTCallStateDisconnected object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(callConnected:) name:CTCallStateConnected object:nil];
}


//handle calls
-(void)handleCall
{
    self.callCenter.callEventHandler = ^(CTCall *call){
        
        if ([call.callState isEqualToString: CTCallStateConnected])
        {
            NSLog(@"call CTCallStateConnected");//Background task stopped
        }
        else if ([call.callState isEqualToString: CTCallStateDialing])
        {
            NSLog(@"call CTCallStateDialing");
        }
        else if ([call.callState isEqualToString: CTCallStateDisconnected])
        {
            NSLog(@"call CTCallStateDisconnected");//Background task started
        }
        else if ([call.callState isEqualToString: CTCallStateIncoming])
        {
            NSLog(@"call CTCallStateIncoming");
        }
        else  {
            NSLog(@"call NO");
        }
    };
}

@end
