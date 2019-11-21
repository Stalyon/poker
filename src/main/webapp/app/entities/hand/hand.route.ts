import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Hand } from 'app/shared/model/hand.model';
import { HandService } from './hand.service';
import { HandComponent } from './hand.component';
import { HandDetailComponent } from './hand-detail.component';
import { HandUpdateComponent } from './hand-update.component';
import { IHand } from 'app/shared/model/hand.model';

@Injectable({ providedIn: 'root' })
export class HandResolve implements Resolve<IHand> {
  constructor(private service: HandService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IHand> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(map((hand: HttpResponse<Hand>) => hand.body));
    }
    return of(new Hand());
  }
}

export const handRoute: Routes = [
  {
    path: '',
    component: HandComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Hands'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: HandDetailComponent,
    resolve: {
      hand: HandResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Hands'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: HandUpdateComponent,
    resolve: {
      hand: HandResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Hands'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: HandUpdateComponent,
    resolve: {
      hand: HandResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Hands'
    },
    canActivate: [UserRouteAccessService]
  }
];
