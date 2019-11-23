import { Route } from '@angular/router';
import {PlayersDatasComponent} from "app/players-datas/players-datas.component";
import {UserRouteAccessService} from "app/core/auth/user-route-access-service";

export const PLAYERS_DATAS_ROUTE: Route = {
  path: 'players-datas',
  component: PlayersDatasComponent,
  data: {
    authorities: ['ROLE_USER'],
    pageTitle: 'Players datas'
  },
  canActivate: [UserRouteAccessService]
};
