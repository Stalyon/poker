import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IBettingRound } from 'app/shared/model/betting-round.model';
import { BettingRoundService } from './betting-round.service';

@Component({
  selector: 'jhi-betting-round-delete-dialog',
  templateUrl: './betting-round-delete-dialog.component.html'
})
export class BettingRoundDeleteDialogComponent {
  bettingRound: IBettingRound;

  constructor(
    protected bettingRoundService: BettingRoundService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.bettingRoundService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'bettingRoundListModification',
        content: 'Deleted an bettingRound'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-betting-round-delete-popup',
  template: ''
})
export class BettingRoundDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ bettingRound }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(BettingRoundDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.bettingRound = bettingRound;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/betting-round', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/betting-round', { outlets: { popup: null } }]);
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
