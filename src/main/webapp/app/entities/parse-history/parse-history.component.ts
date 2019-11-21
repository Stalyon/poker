import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IParseHistory } from 'app/shared/model/parse-history.model';
import { ParseHistoryService } from './parse-history.service';
import { ParseHistoryDeleteDialogComponent } from './parse-history-delete-dialog.component';

@Component({
  selector: 'jhi-parse-history',
  templateUrl: './parse-history.component.html'
})
export class ParseHistoryComponent implements OnInit, OnDestroy {
  parseHistories: IParseHistory[];
  eventSubscriber: Subscription;

  constructor(
    protected parseHistoryService: ParseHistoryService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadAll() {
    this.parseHistoryService.query().subscribe((res: HttpResponse<IParseHistory[]>) => {
      this.parseHistories = res.body;
    });
  }

  ngOnInit() {
    this.loadAll();
    this.registerChangeInParseHistories();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IParseHistory) {
    return item.id;
  }

  registerChangeInParseHistories() {
    this.eventSubscriber = this.eventManager.subscribe('parseHistoryListModification', () => this.loadAll());
  }

  delete(parseHistory: IParseHistory) {
    const modalRef = this.modalService.open(ParseHistoryDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.parseHistory = parseHistory;
  }
}
