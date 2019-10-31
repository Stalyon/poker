import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { BettingRound } from 'app/shared/model/betting-round.model';
import { BettingRoundService } from './betting-round.service';
import { BettingRoundComponent } from './betting-round.component';
import { BettingRoundDetailComponent } from './betting-round-detail.component';
import { BettingRoundUpdateComponent } from './betting-round-update.component';
import { BettingRoundDeletePopupComponent } from './betting-round-delete-dialog.component';
import { IBettingRound } from 'app/shared/model/betting-round.model';

@Injectable({ providedIn: 'root' })
export class BettingRoundResolve implements Resolve<IBettingRound> {
  constructor(private service: BettingRoundService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IBettingRound> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<BettingRound>) => response.ok),
        map((bettingRound: HttpResponse<BettingRound>) => bettingRound.body)
      );
    }
    return of(new BettingRound());
  }
}

export const bettingRoundRoute: Routes = [
  {
    path: '',
    component: BettingRoundComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'BettingRounds'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: BettingRoundDetailComponent,
    resolve: {
      bettingRound: BettingRoundResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'BettingRounds'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: BettingRoundUpdateComponent,
    resolve: {
      bettingRound: BettingRoundResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'BettingRounds'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: BettingRoundUpdateComponent,
    resolve: {
      bettingRound: BettingRoundResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'BettingRounds'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const bettingRoundPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: BettingRoundDeletePopupComponent,
    resolve: {
      bettingRound: BettingRoundResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'BettingRounds'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
