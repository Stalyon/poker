import { Route } from '@angular/router';
import {PlayersDatasComponent} from "app/players-datas/players-datas.component";

export const PLAYERS_DATAS_ROUTE: Route = {
  path: 'players-datas',
  component: PlayersDatasComponent,
  data: {
    authorities: [],
    pageTitle: 'Players datas'
  }
};
