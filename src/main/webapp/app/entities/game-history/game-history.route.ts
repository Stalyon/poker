import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { GameHistory } from 'app/shared/model/game-history.model';
import { GameHistoryService } from './game-history.service';
import { GameHistoryComponent } from './game-history.component';
import { GameHistoryDetailComponent } from './game-history-detail.component';
import { GameHistoryUpdateComponent } from './game-history-update.component';
import { GameHistoryDeletePopupComponent } from './game-history-delete-dialog.component';
import { IGameHistory } from 'app/shared/model/game-history.model';

@Injectable({ providedIn: 'root' })
export class GameHistoryResolve implements Resolve<IGameHistory> {
  constructor(private service: GameHistoryService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IGameHistory> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(map((gameHistory: HttpResponse<GameHistory>) => gameHistory.body));
    }
    return of(new GameHistory());
  }
}

export const gameHistoryRoute: Routes = [
  {
    path: '',
    component: GameHistoryComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'GameHistories'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: GameHistoryDetailComponent,
    resolve: {
      gameHistory: GameHistoryResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'GameHistories'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: GameHistoryUpdateComponent,
    resolve: {
      gameHistory: GameHistoryResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'GameHistories'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: GameHistoryUpdateComponent,
    resolve: {
      gameHistory: GameHistoryResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'GameHistories'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const gameHistoryPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: GameHistoryDeletePopupComponent,
    resolve: {
      gameHistory: GameHistoryResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'GameHistories'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
