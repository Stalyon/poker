import { Component } from '@angular/core';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPlayerHand } from 'app/shared/model/player-hand.model';
import { PlayerHandService } from './player-hand.service';

@Component({
  templateUrl: './player-hand-delete-dialog.component.html'
})
export class PlayerHandDeleteDialogComponent {
  playerHand: IPlayerHand;

  constructor(
    protected playerHandService: PlayerHandService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.playerHandService.delete(id).subscribe(() => {
      this.eventManager.broadcast({
        name: 'playerHandListModification',
        content: 'Deleted an playerHand'
      });
      this.activeModal.dismiss(true);
    });
  }
}
