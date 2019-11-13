import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICashGame } from 'app/shared/model/cash-game.model';
import { CashGameService } from './cash-game.service';

@Component({
  selector: 'jhi-cash-game-delete-dialog',
  templateUrl: './cash-game-delete-dialog.component.html'
})
export class CashGameDeleteDialogComponent {
  cashGame: ICashGame;

  constructor(protected cashGameService: CashGameService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.cashGameService.delete(id).subscribe(() => {
      this.eventManager.broadcast({
        name: 'cashGameListModification',
        content: 'Deleted an cashGame'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-cash-game-delete-popup',
  template: ''
})
export class CashGameDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ cashGame }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(CashGameDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.cashGame = cashGame;
        this.ngbModalRef.result.then(
          () => {
            this.router.navigate(['/cash-game', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          () => {
            this.router.navigate(['/cash-game', { outlets: { popup: null } }]);
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
