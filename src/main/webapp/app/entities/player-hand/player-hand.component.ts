import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPlayerHand } from 'app/shared/model/player-hand.model';
import { PlayerHandService } from './player-hand.service';
import { PlayerHandDeleteDialogComponent } from './player-hand-delete-dialog.component';

@Component({
  selector: 'jhi-player-hand',
  templateUrl: './player-hand.component.html'
})
export class PlayerHandComponent implements OnInit, OnDestroy {
  playerHands: IPlayerHand[];
  eventSubscriber: Subscription;

  constructor(protected playerHandService: PlayerHandService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll() {
    this.playerHandService.query().subscribe((res: HttpResponse<IPlayerHand[]>) => {
      this.playerHands = res.body;
    });
  }

  ngOnInit() {
    this.loadAll();
    this.registerChangeInPlayerHands();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IPlayerHand) {
    return item.id;
  }

  registerChangeInPlayerHands() {
    this.eventSubscriber = this.eventManager.subscribe('playerHandListModification', () => this.loadAll());
  }

  delete(playerHand: IPlayerHand) {
    const modalRef = this.modalService.open(PlayerHandDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.playerHand = playerHand;
  }
}
