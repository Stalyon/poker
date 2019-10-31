import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IParseHistory } from 'app/shared/model/parse-history.model';
import { ParseHistoryService } from './parse-history.service';

@Component({
  selector: 'jhi-parse-history-delete-dialog',
  templateUrl: './parse-history-delete-dialog.component.html'
})
export class ParseHistoryDeleteDialogComponent {
  parseHistory: IParseHistory;

  constructor(
    protected parseHistoryService: ParseHistoryService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.parseHistoryService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'parseHistoryListModification',
        content: 'Deleted an parseHistory'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-parse-history-delete-popup',
  template: ''
})
export class ParseHistoryDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ parseHistory }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(ParseHistoryDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.parseHistory = parseHistory;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/parse-history', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/parse-history', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          }
        );
      }, 0);
    });
  }

  ngOnDestroy() {
    this.ngbModalRef = null;
  }
}
