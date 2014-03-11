//
//  MAIAppDelegate.h
//  MAInfo
//
//  Created by Geoffrey Owens on 3/11/14.
//  Copyright (c) 2014 Milton Academy Programming. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface MAIAppDelegate : UIResponder <UIApplicationDelegate>

@property (strong, nonatomic) UIWindow *window;

@property (readonly, strong, nonatomic) NSManagedObjectContext *managedObjectContext;
@property (readonly, strong, nonatomic) NSManagedObjectModel *managedObjectModel;
@property (readonly, strong, nonatomic) NSPersistentStoreCoordinator *persistentStoreCoordinator;

- (void)saveContext;
- (NSURL *)applicationDocumentsDirectory;

@end
