import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICashGame } from 'app/shared/model/cash-game.model';

@Component({
  selector: 'jhi-cash-game-detail',
  templateUrl: './cash-game-detail.component.html'
})
export class CashGameDetailComponent implements OnInit {
  cashGame: ICashGame;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ cashGame }) => {
      this.cashGame = cashGame;
    });
  }

  previousState() {
    window.history.back();
  }
}
