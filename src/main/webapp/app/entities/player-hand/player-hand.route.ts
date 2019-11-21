import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { PlayerHand } from 'app/shared/model/player-hand.model';
import { PlayerHandService } from './player-hand.service';
import { PlayerHandComponent } from './player-hand.component';
import { PlayerHandDetailComponent } from './player-hand-detail.component';
import { PlayerHandUpdateComponent } from './player-hand-update.component';
import { IPlayerHand } from 'app/shared/model/player-hand.model';

@Injectable({ providedIn: 'root' })
export class PlayerHandResolve implements Resolve<IPlayerHand> {
  constructor(private service: PlayerHandService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPlayerHand> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(map((playerHand: HttpResponse<PlayerHand>) => playerHand.body));
    }
    return of(new PlayerHand());
  }
}

export const playerHandRoute: Routes = [
  {
    path: '',
    component: PlayerHandComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PlayerHands'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: PlayerHandDetailComponent,
    resolve: {
      playerHand: PlayerHandResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PlayerHands'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: PlayerHandUpdateComponent,
    resolve: {
      playerHand: PlayerHandResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PlayerHands'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: PlayerHandUpdateComponent,
    resolve: {
      playerHand: PlayerHandResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PlayerHands'
    },
    canActivate: [UserRouteAccessService]
  }
];
