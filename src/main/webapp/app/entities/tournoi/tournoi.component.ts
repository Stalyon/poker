import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { ITournoi } from 'app/shared/model/tournoi.model';
import { TournoiService } from './tournoi.service';

@Component({
  selector: 'jhi-tournoi',
  templateUrl: './tournoi.component.html'
})
export class TournoiComponent implements OnInit, OnDestroy {
  tournois: ITournoi[];
  eventSubscriber: Subscription;

  constructor(protected tournoiService: TournoiService, protected eventManager: JhiEventManager) {}

  loadAll() {
    this.tournoiService.query().subscribe((res: HttpResponse<ITournoi[]>) => {
      this.tournois = res.body;
    });
  }

  ngOnInit() {
    this.loadAll();
    this.registerChangeInTournois();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: ITournoi) {
    return item.id;
  }

  registerChangeInTournois() {
    this.eventSubscriber = this.eventManager.subscribe('tournoiListModification', () => this.loadAll());
  }
}
