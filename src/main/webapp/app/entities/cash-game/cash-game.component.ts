import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { ICashGame } from 'app/shared/model/cash-game.model';
import { CashGameService } from './cash-game.service';

@Component({
  selector: 'jhi-cash-game',
  templateUrl: './cash-game.component.html'
})
export class CashGameComponent implements OnInit, OnDestroy {
  cashGames: ICashGame[];
  eventSubscriber: Subscription;

  constructor(protected cashGameService: CashGameService, protected eventManager: JhiEventManager) {}

  loadAll() {
    this.cashGameService.query().subscribe((res: HttpResponse<ICashGame[]>) => {
      this.cashGames = res.body;
    });
  }

  ngOnInit() {
    this.loadAll();
    this.registerChangeInCashGames();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: ICashGame) {
    return item.id;
  }

  registerChangeInCashGames() {
    this.eventSubscriber = this.eventManager.subscribe('cashGameListModification', () => this.loadAll());
  }
}
