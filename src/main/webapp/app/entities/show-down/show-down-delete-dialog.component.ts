import { Component } from '@angular/core';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IShowDown } from 'app/shared/model/show-down.model';
import { ShowDownService } from './show-down.service';

@Component({
  templateUrl: './show-down-delete-dialog.component.html'
})
export class ShowDownDeleteDialogComponent {
  showDown: IShowDown;

  constructor(protected showDownService: ShowDownService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.showDownService.delete(id).subscribe(() => {
      this.eventManager.broadcast({
        name: 'showDownListModification',
        content: 'Deleted an showDown'
      });
      this.activeModal.dismiss(true);
    });
  }
}
