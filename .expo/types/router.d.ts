/* eslint-disable */
import * as Router from 'expo-router';

export * from 'expo-router';

declare module 'expo-router' {
  export namespace ExpoRouter {
    export interface __routes<T extends string | object = string> {
      hrefInputParams: { pathname: Router.RelativePathString, params?: Router.UnknownInputParams } | { pathname: Router.ExternalPathString, params?: Router.UnknownInputParams } | { pathname: `/`; params?: Router.UnknownInputParams; } | { pathname: `/_sitemap`; params?: Router.UnknownInputParams; } | { pathname: `/navigation/AppNavigator`; params?: Router.UnknownInputParams; } | { pathname: `/screens/DashboardScreen`; params?: Router.UnknownInputParams; } | { pathname: `/screens/GuardDetailScreen`; params?: Router.UnknownInputParams; } | { pathname: `/screens/LoginScreens`; params?: Router.UnknownInputParams; } | { pathname: `/screens/MapMonitorScreen`; params?: Router.UnknownInputParams; };
      hrefOutputParams: { pathname: Router.RelativePathString, params?: Router.UnknownOutputParams } | { pathname: Router.ExternalPathString, params?: Router.UnknownOutputParams } | { pathname: `/`; params?: Router.UnknownOutputParams; } | { pathname: `/_sitemap`; params?: Router.UnknownOutputParams; } | { pathname: `/navigation/AppNavigator`; params?: Router.UnknownOutputParams; } | { pathname: `/screens/DashboardScreen`; params?: Router.UnknownOutputParams; } | { pathname: `/screens/GuardDetailScreen`; params?: Router.UnknownOutputParams; } | { pathname: `/screens/LoginScreens`; params?: Router.UnknownOutputParams; } | { pathname: `/screens/MapMonitorScreen`; params?: Router.UnknownOutputParams; };
      href: Router.RelativePathString | Router.ExternalPathString | `/${`?${string}` | `#${string}` | ''}` | `/_sitemap${`?${string}` | `#${string}` | ''}` | `/navigation/AppNavigator${`?${string}` | `#${string}` | ''}` | `/screens/DashboardScreen${`?${string}` | `#${string}` | ''}` | `/screens/GuardDetailScreen${`?${string}` | `#${string}` | ''}` | `/screens/LoginScreens${`?${string}` | `#${string}` | ''}` | `/screens/MapMonitorScreen${`?${string}` | `#${string}` | ''}` | { pathname: Router.RelativePathString, params?: Router.UnknownInputParams } | { pathname: Router.ExternalPathString, params?: Router.UnknownInputParams } | { pathname: `/`; params?: Router.UnknownInputParams; } | { pathname: `/_sitemap`; params?: Router.UnknownInputParams; } | { pathname: `/navigation/AppNavigator`; params?: Router.UnknownInputParams; } | { pathname: `/screens/DashboardScreen`; params?: Router.UnknownInputParams; } | { pathname: `/screens/GuardDetailScreen`; params?: Router.UnknownInputParams; } | { pathname: `/screens/LoginScreens`; params?: Router.UnknownInputParams; } | { pathname: `/screens/MapMonitorScreen`; params?: Router.UnknownInputParams; };
    }
  }
}
