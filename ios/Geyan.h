
#ifdef RCT_NEW_ARCH_ENABLED
#import "RNGeyanSpec.h"

@interface Geyan : NSObject <NativeGeyanSpec>
#else
#import <React/RCTBridgeModule.h>

@interface Geyan : NSObject <RCTBridgeModule>
#endif

@end
