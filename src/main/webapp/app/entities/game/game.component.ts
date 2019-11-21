import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IGame } from 'app/shared/model/game.model';
import { GameService } from './game.service';
import { GameDeleteDialogComponent } from './game-delete-dialog.component';

@Component({
  selector: 'jhi-game',
  templateUrl: './game.component.html'
})
export class GameComponent implements OnInit, OnDestroy {
  games: IGame[];
  eventSubscriber: Subscription;

  constructor(protected gameService: GameService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll() {
    this.gameService.query().subscribe((res: HttpResponse<IGame[]>) => {
      this.games = res.body;
    });
  }

  ngOnInit() {
    this.loadAll();
    this.registerChangeInGames();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IGame) {
    return item.id;
  }

  registerChangeInGames() {
    this.eventSubscriber = this.eventManager.subscribe('gameListModification', () => this.loadAll());
  }

  delete(game: IGame) {
    const modalRef = this.modalService.open(GameDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.game = game;
  }
}
