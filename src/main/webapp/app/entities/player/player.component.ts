import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager } from 'ng-jhipster';

import { IPlayer } from 'app/shared/model/player.model';
import { AccountService } from 'app/core/auth/account.service';
import { PlayerService } from './player.service';

@Component({
  selector: 'jhi-player',
  templateUrl: './player.component.html'
})
export class PlayerComponent implements OnInit, OnDestroy {
  players: IPlayer[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(protected playerService: PlayerService, protected eventManager: JhiEventManager, protected accountService: AccountService) {}

  loadAll() {
    this.playerService
      .query()
      .pipe(
        filter((res: HttpResponse<IPlayer[]>) => res.ok),
        map((res: HttpResponse<IPlayer[]>) => res.body)
      )
      .subscribe((res: IPlayer[]) => {
        this.players = res;
      });
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.registerChangeInPlayers();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IPlayer) {
    return item.id;
  }

  registerChangeInPlayers() {
    this.eventSubscriber = this.eventManager.subscribe('playerListModification', response => this.loadAll());
  }
}
