import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IHand } from 'app/shared/model/hand.model';
import { HandService } from './hand.service';

@Component({
  selector: 'jhi-hand-delete-dialog',
  templateUrl: './hand-delete-dialog.component.html'
})
export class HandDeleteDialogComponent {
  hand: IHand;

  constructor(protected handService: HandService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.handService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'handListModification',
        content: 'Deleted an hand'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-hand-delete-popup',
  template: ''
})
export class HandDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ hand }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(HandDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.hand = hand;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/hand', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/hand', { outlets: { popup: null } }]);
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
