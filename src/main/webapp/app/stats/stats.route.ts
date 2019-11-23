import { Route } from '@angular/router';
import {UserRouteAccessService} from "app/core/auth/user-route-access-service";
import {StatsComponent} from "app/stats/stats.component";

export const STATS_ROUTE: Route = {
  path: 'stats',
  component: StatsComponent,
  data: {
    authorities: ['ROLE_USER'],
    pageTitle: 'Stats'
  },
  canActivate: [UserRouteAccessService]
};
