import { Route } from '@angular/router';
import {LiveComponent} from "app/live/live.component";
import {UserRouteAccessService} from "app/core/auth/user-route-access-service";

export const LIVE_ROUTE: Route = {
  path: 'live',
  component: LiveComponent,
  data: {
    authorities: ['ROLE_USER'],
    pageTitle: 'Live'
  },
  canActivate: [UserRouteAccessService]
};
