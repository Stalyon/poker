import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager } from 'ng-jhipster';

import { IHand } from 'app/shared/model/hand.model';
import { AccountService } from 'app/core/auth/account.service';
import { HandService } from './hand.service';

@Component({
  selector: 'jhi-hand',
  templateUrl: './hand.component.html'
})
export class HandComponent implements OnInit, OnDestroy {
  hands: IHand[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(protected handService: HandService, protected eventManager: JhiEventManager, protected accountService: AccountService) {}

  loadAll() {
    this.handService
      .query()
      .pipe(
        filter((res: HttpResponse<IHand[]>) => res.ok),
        map((res: HttpResponse<IHand[]>) => res.body)
      )
      .subscribe((res: IHand[]) => {
        this.hands = res;
      });
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.registerChangeInHands();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IHand) {
    return item.id;
  }

  registerChangeInHands() {
    this.eventSubscriber = this.eventManager.subscribe('handListModification', response => this.loadAll());
  }
}
