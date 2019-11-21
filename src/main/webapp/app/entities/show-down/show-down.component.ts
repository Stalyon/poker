import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IShowDown } from 'app/shared/model/show-down.model';
import { ShowDownService } from './show-down.service';
import { ShowDownDeleteDialogComponent } from './show-down-delete-dialog.component';

@Component({
  selector: 'jhi-show-down',
  templateUrl: './show-down.component.html'
})
export class ShowDownComponent implements OnInit, OnDestroy {
  showDowns: IShowDown[];
  eventSubscriber: Subscription;

  constructor(protected showDownService: ShowDownService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll() {
    this.showDownService.query().subscribe((res: HttpResponse<IShowDown[]>) => {
      this.showDowns = res.body;
    });
  }

  ngOnInit() {
    this.loadAll();
    this.registerChangeInShowDowns();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IShowDown) {
    return item.id;
  }

  registerChangeInShowDowns() {
    this.eventSubscriber = this.eventManager.subscribe('showDownListModification', () => this.loadAll());
  }

  delete(showDown: IShowDown) {
    const modalRef = this.modalService.open(ShowDownDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.showDown = showDown;
  }
}
