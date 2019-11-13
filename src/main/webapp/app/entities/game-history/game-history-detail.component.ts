import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IGameHistory } from 'app/shared/model/game-history.model';

@Component({
  selector: 'jhi-game-history-detail',
  templateUrl: './game-history-detail.component.html'
})
export class GameHistoryDetailComponent implements OnInit {
  gameHistory: IGameHistory;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ gameHistory }) => {
      this.gameHistory = gameHistory;
    });
  }

  previousState() {
    window.history.back();
  }
}
