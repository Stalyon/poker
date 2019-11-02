import { Route } from '@angular/router';
import {LiveComponent} from "app/live/live.component";

export const LIVE_ROUTE: Route = {
  path: 'live',
  component: LiveComponent,
  data: {
    authorities: [],
    pageTitle: 'Live'
  }
};
