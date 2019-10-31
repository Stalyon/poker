import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager } from 'ng-jhipster';

import { IParseHistory } from 'app/shared/model/parse-history.model';
import { AccountService } from 'app/core/auth/account.service';
import { ParseHistoryService } from './parse-history.service';

@Component({
  selector: 'jhi-parse-history',
  templateUrl: './parse-history.component.html'
})
export class ParseHistoryComponent implements OnInit, OnDestroy {
  parseHistories: IParseHistory[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected parseHistoryService: ParseHistoryService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.parseHistoryService
      .query()
      .pipe(
        filter((res: HttpResponse<IParseHistory[]>) => res.ok),
        map((res: HttpResponse<IParseHistory[]>) => res.body)
      )
      .subscribe((res: IParseHistory[]) => {
        this.parseHistories = res;
      });
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.registerChangeInParseHistories();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IParseHistory) {
    return item.id;
  }

  registerChangeInParseHistories() {
    this.eventSubscriber = this.eventManager.subscribe('parseHistoryListModification', response => this.loadAll());
  }
}
