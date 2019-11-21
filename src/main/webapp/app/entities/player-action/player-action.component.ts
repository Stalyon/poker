import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPlayerAction } from 'app/shared/model/player-action.model';
import { PlayerActionService } from './player-action.service';
import { PlayerActionDeleteDialogComponent } from './player-action-delete-dialog.component';

@Component({
  selector: 'jhi-player-action',
  templateUrl: './player-action.component.html'
})
export class PlayerActionComponent implements OnInit, OnDestroy {
  playerActions: IPlayerAction[];
  eventSubscriber: Subscription;

  constructor(
    protected playerActionService: PlayerActionService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadAll() {
    this.playerActionService.query().subscribe((res: HttpResponse<IPlayerAction[]>) => {
      this.playerActions = res.body;
    });
  }

  ngOnInit() {
    this.loadAll();
    this.registerChangeInPlayerActions();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IPlayerAction) {
    return item.id;
  }

  registerChangeInPlayerActions() {
    this.eventSubscriber = this.eventManager.subscribe('playerActionListModification', () => this.loadAll());
  }

  delete(playerAction: IPlayerAction) {
    const modalRef = this.modalService.open(PlayerActionDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.playerAction = playerAction;
  }
}
