import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { CashGame } from 'app/shared/model/cash-game.model';
import { CashGameService } from './cash-game.service';
import { CashGameComponent } from './cash-game.component';
import { CashGameDetailComponent } from './cash-game-detail.component';
import { CashGameUpdateComponent } from './cash-game-update.component';
import { CashGameDeletePopupComponent } from './cash-game-delete-dialog.component';
import { ICashGame } from 'app/shared/model/cash-game.model';

@Injectable({ providedIn: 'root' })
export class CashGameResolve implements Resolve<ICashGame> {
  constructor(private service: CashGameService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICashGame> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(map((cashGame: HttpResponse<CashGame>) => cashGame.body));
    }
    return of(new CashGame());
  }
}

export const cashGameRoute: Routes = [
  {
    path: '',
    component: CashGameComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CashGames'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: CashGameDetailComponent,
    resolve: {
      cashGame: CashGameResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CashGames'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: CashGameUpdateComponent,
    resolve: {
      cashGame: CashGameResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CashGames'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: CashGameUpdateComponent,
    resolve: {
      cashGame: CashGameResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CashGames'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const cashGamePopupRoute: Routes = [
  {
    path: ':id/delete',
    component: CashGameDeletePopupComponent,
    resolve: {
      cashGame: CashGameResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CashGames'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
