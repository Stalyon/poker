import { Route } from '@angular/router';
import {DatasComponent} from "app/datas/datas.component";
import {UserRouteAccessService} from "app/core/auth/user-route-access-service";

export const DATAS_ROUTE: Route = {
  path: 'datas',
  component: DatasComponent,
  data: {
    authorities: ['ROLE_USER'],
    pageTitle: 'Datas'
  },
  canActivate: [UserRouteAccessService]
};
