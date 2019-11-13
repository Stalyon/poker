import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Tournoi } from 'app/shared/model/tournoi.model';
import { TournoiService } from './tournoi.service';
import { TournoiComponent } from './tournoi.component';
import { TournoiDetailComponent } from './tournoi-detail.component';
import { TournoiUpdateComponent } from './tournoi-update.component';
import { TournoiDeletePopupComponent } from './tournoi-delete-dialog.component';
import { ITournoi } from 'app/shared/model/tournoi.model';

@Injectable({ providedIn: 'root' })
export class TournoiResolve implements Resolve<ITournoi> {
  constructor(private service: TournoiService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITournoi> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(map((tournoi: HttpResponse<Tournoi>) => tournoi.body));
    }
    return of(new Tournoi());
  }
}

export const tournoiRoute: Routes = [
  {
    path: '',
    component: TournoiComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Tournois'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: TournoiDetailComponent,
    resolve: {
      tournoi: TournoiResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Tournois'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: TournoiUpdateComponent,
    resolve: {
      tournoi: TournoiResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Tournois'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: TournoiUpdateComponent,
    resolve: {
      tournoi: TournoiResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Tournois'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const tournoiPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: TournoiDeletePopupComponent,
    resolve: {
      tournoi: TournoiResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Tournois'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
