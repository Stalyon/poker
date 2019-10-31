import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager } from 'ng-jhipster';

import { IGame } from 'app/shared/model/game.model';
import { AccountService } from 'app/core/auth/account.service';
import { GameService } from './game.service';

@Component({
  selector: 'jhi-game',
  templateUrl: './game.component.html'
})
export class GameComponent implements OnInit, OnDestroy {
  games: IGame[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(protected gameService: GameService, protected eventManager: JhiEventManager, protected accountService: AccountService) {}

  loadAll() {
    this.gameService
      .query()
      .pipe(
        filter((res: HttpResponse<IGame[]>) => res.ok),
        map((res: HttpResponse<IGame[]>) => res.body)
      )
      .subscribe((res: IGame[]) => {
        this.games = res;
      });
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.registerChangeInGames();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IGame) {
    return item.id;
  }

  registerChangeInGames() {
    this.eventSubscriber = this.eventManager.subscribe('gameListModification', response => this.loadAll());
  }
}
