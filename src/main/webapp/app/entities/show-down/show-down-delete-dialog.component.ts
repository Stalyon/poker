import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IShowDown } from 'app/shared/model/show-down.model';
import { ShowDownService } from './show-down.service';

@Component({
  selector: 'jhi-show-down-delete-dialog',
  templateUrl: './show-down-delete-dialog.component.html'
})
export class ShowDownDeleteDialogComponent {
  showDown: IShowDown;

  constructor(protected showDownService: ShowDownService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.showDownService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'showDownListModification',
        content: 'Deleted an showDown'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-show-down-delete-popup',
  template: ''
})
export class ShowDownDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ showDown }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(ShowDownDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.showDown = showDown;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/show-down', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/show-down', { outlets: { popup: null } }]);
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
