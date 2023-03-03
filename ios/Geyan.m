#import "Geyan.h"

@implementation Geyan

RCT_EXPORT_MODULE()

RCT_EXPORT_METHOD(init:(NSDictionary *)options resolver:(RCTPromiseResolveBlock)resolve rejector:(RCTPromiseRejectBlock) reject)
{
  NSString *appid = options[@"appid"];
  if (appid) {
    dispatch_async(dispatch_get_main_queue(), ^{
      @try {
        [GeYanSdk startWithAppId:appid withCallback:^(BOOL isSuccess, NSError * _Nullable error, NSString * _Nullable gyUid) {
          if (isSuccess) {
            NSLog(@"个验===>初始化成功 gyUid: %@", gyUid);
            resolve(@YES);
          } else {
            NSLog(@"个验===>初始化失败 error: %@", error);
            reject(@"geyan failed", error.description, nil);
          }
        }];
      } @catch (NSException *exception) {
        NSLog(@"个验===>初始化失败 exception: %@", exception);
        reject(@"geyan failed", exception.description, nil);
      }
    });
  } else {
    NSString *reason = [NSString stringWithFormat:@"appid is required, but get %@", appid];
    NSLog(@"个验===>初始化失败, %@", reason);
    reject(@"geyan failed", reason, nil);
  }
}

RCT_EXPORT_METHOD(open:(NSDictionary *)options resolver:(RCTPromiseResolveBlock)resolve rejector:(RCTPromiseRejectBlock) reject)
{
  [GeYanSdk preGetToken:^(NSDictionary * _Nullable verifyDictionary) {
    NSNumber *code = verifyDictionary[@"code"];
    if ([code isEqualToNumber:@30000]) {
      NSLog(@"个验===>预登录成功 result: %@", verifyDictionary);
      UIViewController *rootVC = [[[UIApplication sharedApplication] keyWindow] rootViewController];
      
      GyAuthViewModel *viewModel = [GyAuthViewModel new];
      viewModel.switchButtonHidden = @YES;
     
      if (options[@"logo"]) {
        NSURL *logoURL = [NSURL URLWithString: options[@"logo"]];
        NSData *logoData = [NSData dataWithContentsOfURL:logoURL];
        UIImage *logo = [UIImage imageWithData:logoData];
        viewModel.appLogo = logo;
      }
      
      [GeYanSdk oneTapLogin:rootVC withViewModel:viewModel andCallback:^(NSDictionary * _Nullable verifyDictionary) {
          NSNumber *code = verifyDictionary[@"code"];
          if ([code isEqualToNumber:@30000]) {
            NSLog(@"个验===>一键登录成功 result: %@", verifyDictionary);
            resolve(verifyDictionary);
          } else {
            NSLog(@"个验===>一键登录失败 result: %@", verifyDictionary);
            reject(@"geyan failed", verifyDictionary[@"msg"], nil);
          }
      }];
    } else {
      NSLog(@"个验===>预登录失败 result: %@", verifyDictionary);
      reject(@"geyan failed", verifyDictionary[@"msg"], nil);
    }
  }];
}

RCT_EXPORT_METHOD(close:(RCTPromiseResolveBlock)resolve rejector:(RCTPromiseRejectBlock) reject)
{
  @try {
    [GeYanSdk closeAuthVC:^{
      NSLog(@"个验===>关闭成功");
      resolve(nil);
    }];
  } @catch (NSException *exception) {
    NSLog(@"个验===>关闭失败 err: %@", exception);
    reject(@"geyan failed", exception.description, nil);
  }
}

-(dispatch_queue_t)methodQueue
{
  return dispatch_get_main_queue();
}

+(BOOL)requiresMainQueueSetup
{
  return YES;
}

@end
