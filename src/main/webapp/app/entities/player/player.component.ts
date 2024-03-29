import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPlayer } from 'app/shared/model/player.model';
import { PlayerService } from './player.service';
import { PlayerDeleteDialogComponent } from './player-delete-dialog.component';

@Component({
  selector: 'jhi-player',
  templateUrl: './player.component.html'
})
export class PlayerComponent implements OnInit, OnDestroy {
  players: IPlayer[];
  eventSubscriber: Subscription;

  constructor(protected playerService: PlayerService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll() {
    this.playerService.query().subscribe((res: HttpResponse<IPlayer[]>) => {
      this.players = res.body;
    });
  }

  ngOnInit() {
    this.loadAll();
    this.registerChangeInPlayers();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IPlayer) {
    return item.id;
  }

  registerChangeInPlayers() {
    this.eventSubscriber = this.eventManager.subscribe('playerListModification', () => this.loadAll());
  }

  delete(player: IPlayer) {
    const modalRef = this.modalService.open(PlayerDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.player = player;
  }
}
