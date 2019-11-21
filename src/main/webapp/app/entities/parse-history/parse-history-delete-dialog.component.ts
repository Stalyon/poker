import { Component } from '@angular/core';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IParseHistory } from 'app/shared/model/parse-history.model';
import { ParseHistoryService } from './parse-history.service';

@Component({
  templateUrl: './parse-history-delete-dialog.component.html'
})
export class ParseHistoryDeleteDialogComponent {
  parseHistory: IParseHistory;

  constructor(
    protected parseHistoryService: ParseHistoryService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.parseHistoryService.delete(id).subscribe(() => {
      this.eventManager.broadcast({
        name: 'parseHistoryListModification',
        content: 'Deleted an parseHistory'
      });
      this.activeModal.dismiss(true);
    });
  }
}
