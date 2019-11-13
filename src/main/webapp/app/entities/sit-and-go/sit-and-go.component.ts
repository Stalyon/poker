import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { ISitAndGo } from 'app/shared/model/sit-and-go.model';
import { SitAndGoService } from './sit-and-go.service';

@Component({
  selector: 'jhi-sit-and-go',
  templateUrl: './sit-and-go.component.html'
})
export class SitAndGoComponent implements OnInit, OnDestroy {
  sitAndGos: ISitAndGo[];
  eventSubscriber: Subscription;

  constructor(protected sitAndGoService: SitAndGoService, protected eventManager: JhiEventManager) {}

  loadAll() {
    this.sitAndGoService.query().subscribe((res: HttpResponse<ISitAndGo[]>) => {
      this.sitAndGos = res.body;
    });
  }

  ngOnInit() {
    this.loadAll();
    this.registerChangeInSitAndGos();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: ISitAndGo) {
    return item.id;
  }

  registerChangeInSitAndGos() {
    this.eventSubscriber = this.eventManager.subscribe('sitAndGoListModification', () => this.loadAll());
  }
}
