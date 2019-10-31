import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager } from 'ng-jhipster';

import { IShowDown } from 'app/shared/model/show-down.model';
import { AccountService } from 'app/core/auth/account.service';
import { ShowDownService } from './show-down.service';

@Component({
  selector: 'jhi-show-down',
  templateUrl: './show-down.component.html'
})
export class ShowDownComponent implements OnInit, OnDestroy {
  showDowns: IShowDown[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected showDownService: ShowDownService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.showDownService
      .query()
      .pipe(
        filter((res: HttpResponse<IShowDown[]>) => res.ok),
        map((res: HttpResponse<IShowDown[]>) => res.body)
      )
      .subscribe((res: IShowDown[]) => {
        this.showDowns = res;
      });
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.registerChangeInShowDowns();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IShowDown) {
    return item.id;
  }

  registerChangeInShowDowns() {
    this.eventSubscriber = this.eventManager.subscribe('showDownListModification', response => this.loadAll());
  }
}
