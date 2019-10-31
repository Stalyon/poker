import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { PlayerAction } from 'app/shared/model/player-action.model';
import { PlayerActionService } from './player-action.service';
import { PlayerActionComponent } from './player-action.component';
import { PlayerActionDetailComponent } from './player-action-detail.component';
import { PlayerActionUpdateComponent } from './player-action-update.component';
import { PlayerActionDeletePopupComponent } from './player-action-delete-dialog.component';
import { IPlayerAction } from 'app/shared/model/player-action.model';

@Injectable({ providedIn: 'root' })
export class PlayerActionResolve implements Resolve<IPlayerAction> {
  constructor(private service: PlayerActionService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IPlayerAction> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<PlayerAction>) => response.ok),
        map((playerAction: HttpResponse<PlayerAction>) => playerAction.body)
      );
    }
    return of(new PlayerAction());
  }
}

export const playerActionRoute: Routes = [
  {
    path: '',
    component: PlayerActionComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PlayerActions'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: PlayerActionDetailComponent,
    resolve: {
      playerAction: PlayerActionResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PlayerActions'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: PlayerActionUpdateComponent,
    resolve: {
      playerAction: PlayerActionResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PlayerActions'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: PlayerActionUpdateComponent,
    resolve: {
      playerAction: PlayerActionResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PlayerActions'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const playerActionPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: PlayerActionDeletePopupComponent,
    resolve: {
      playerAction: PlayerActionResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PlayerActions'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
