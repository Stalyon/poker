import { Component } from '@angular/core';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IGame } from 'app/shared/model/game.model';
import { GameService } from './game.service';

@Component({
  templateUrl: './game-delete-dialog.component.html'
})
export class GameDeleteDialogComponent {
  game: IGame;

  constructor(protected gameService: GameService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.gameService.delete(id).subscribe(() => {
      this.eventManager.broadcast({
        name: 'gameListModification',
        content: 'Deleted an game'
      });
      this.activeModal.dismiss(true);
    });
  }
}
