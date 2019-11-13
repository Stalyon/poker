import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IGameHistory } from 'app/shared/model/game-history.model';
import { GameHistoryService } from './game-history.service';

@Component({
  selector: 'jhi-game-history-delete-dialog',
  templateUrl: './game-history-delete-dialog.component.html'
})
export class GameHistoryDeleteDialogComponent {
  gameHistory: IGameHistory;

  constructor(
    protected gameHistoryService: GameHistoryService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.gameHistoryService.delete(id).subscribe(() => {
      this.eventManager.broadcast({
        name: 'gameHistoryListModification',
        content: 'Deleted an gameHistory'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-game-history-delete-popup',
  template: ''
})
export class GameHistoryDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ gameHistory }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(GameHistoryDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.gameHistory = gameHistory;
        this.ngbModalRef.result.then(
          () => {
            this.router.navigate(['/game-history', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          () => {
            this.router.navigate(['/game-history', { outlets: { popup: null } }]);
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
