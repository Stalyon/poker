import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager } from 'ng-jhipster';

import { IPlayerAction } from 'app/shared/model/player-action.model';
import { AccountService } from 'app/core/auth/account.service';
import { PlayerActionService } from './player-action.service';

@Component({
  selector: 'jhi-player-action',
  templateUrl: './player-action.component.html'
})
export class PlayerActionComponent implements OnInit, OnDestroy {
  playerActions: IPlayerAction[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected playerActionService: PlayerActionService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.playerActionService
      .query()
      .pipe(
        filter((res: HttpResponse<IPlayerAction[]>) => res.ok),
        map((res: HttpResponse<IPlayerAction[]>) => res.body)
      )
      .subscribe((res: IPlayerAction[]) => {
        this.playerActions = res;
      });
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.registerChangeInPlayerActions();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IPlayerAction) {
    return item.id;
  }

  registerChangeInPlayerActions() {
    this.eventSubscriber = this.eventManager.subscribe('playerActionListModification', response => this.loadAll());
  }
}
