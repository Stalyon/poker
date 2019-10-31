import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager } from 'ng-jhipster';

import { IBettingRound } from 'app/shared/model/betting-round.model';
import { AccountService } from 'app/core/auth/account.service';
import { BettingRoundService } from './betting-round.service';

@Component({
  selector: 'jhi-betting-round',
  templateUrl: './betting-round.component.html'
})
export class BettingRoundComponent implements OnInit, OnDestroy {
  bettingRounds: IBettingRound[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected bettingRoundService: BettingRoundService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.bettingRoundService
      .query()
      .pipe(
        filter((res: HttpResponse<IBettingRound[]>) => res.ok),
        map((res: HttpResponse<IBettingRound[]>) => res.body)
      )
      .subscribe((res: IBettingRound[]) => {
        this.bettingRounds = res;
      });
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.registerChangeInBettingRounds();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IBettingRound) {
    return item.id;
  }

  registerChangeInBettingRounds() {
    this.eventSubscriber = this.eventManager.subscribe('bettingRoundListModification', response => this.loadAll());
  }
}
