import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPlayerAction } from 'app/shared/model/player-action.model';
import { PlayerActionService } from './player-action.service';

@Component({
  selector: 'jhi-player-action-delete-dialog',
  templateUrl: './player-action-delete-dialog.component.html'
})
export class PlayerActionDeleteDialogComponent {
  playerAction: IPlayerAction;

  constructor(
    protected playerActionService: PlayerActionService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.playerActionService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'playerActionListModification',
        content: 'Deleted an playerAction'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-player-action-delete-popup',
  template: ''
})
export class PlayerActionDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ playerAction }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(PlayerActionDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.playerAction = playerAction;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/player-action', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/player-action', { outlets: { popup: null } }]);
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
