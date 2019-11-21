import { Route } from '@angular/router';
import {DatasComponent} from "app/datas/datas.component";

export const DATAS_ROUTE: Route = {
  path: 'datas',
  component: DatasComponent,
  data: {
    authorities: ['ROLE_USER'],
    pageTitle: 'Datas'
  }
};
