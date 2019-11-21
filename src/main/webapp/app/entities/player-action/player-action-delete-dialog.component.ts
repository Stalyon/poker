import { Component } from '@angular/core';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPlayerAction } from 'app/shared/model/player-action.model';
import { PlayerActionService } from './player-action.service';

@Component({
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
    this.playerActionService.delete(id).subscribe(() => {
      this.eventManager.broadcast({
        name: 'playerActionListModification',
        content: 'Deleted an playerAction'
      });
      this.activeModal.dismiss(true);
    });
  }
}
