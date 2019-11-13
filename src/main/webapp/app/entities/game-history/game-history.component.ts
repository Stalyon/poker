import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { IGameHistory } from 'app/shared/model/game-history.model';
import { GameHistoryService } from './game-history.service';

@Component({
  selector: 'jhi-game-history',
  templateUrl: './game-history.component.html'
})
export class GameHistoryComponent implements OnInit, OnDestroy {
  gameHistories: IGameHistory[];
  eventSubscriber: Subscription;

  constructor(protected gameHistoryService: GameHistoryService, protected eventManager: JhiEventManager) {}

  loadAll() {
    this.gameHistoryService.query().subscribe((res: HttpResponse<IGameHistory[]>) => {
      this.gameHistories = res.body;
    });
  }

  ngOnInit() {
    this.loadAll();
    this.registerChangeInGameHistories();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IGameHistory) {
    return item.id;
  }

  registerChangeInGameHistories() {
    this.eventSubscriber = this.eventManager.subscribe('gameHistoryListModification', () => this.loadAll());
  }
}
