import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Game } from 'app/shared/model/game.model';
import { GameService } from './game.service';
import { GameComponent } from './game.component';
import { GameDetailComponent } from './game-detail.component';
import { GameUpdateComponent } from './game-update.component';
import { IGame } from 'app/shared/model/game.model';

@Injectable({ providedIn: 'root' })
export class GameResolve implements Resolve<IGame> {
  constructor(private service: GameService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IGame> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(map((game: HttpResponse<Game>) => game.body));
    }
    return of(new Game());
  }
}

export const gameRoute: Routes = [
  {
    path: '',
    component: GameComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Games'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: GameDetailComponent,
    resolve: {
      game: GameResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Games'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: GameUpdateComponent,
    resolve: {
      game: GameResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Games'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: GameUpdateComponent,
    resolve: {
      game: GameResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Games'
    },
    canActivate: [UserRouteAccessService]
  }
];
