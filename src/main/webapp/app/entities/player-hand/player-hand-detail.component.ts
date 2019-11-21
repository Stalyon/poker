import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPlayerHand } from 'app/shared/model/player-hand.model';

@Component({
  selector: 'jhi-player-hand-detail',
  templateUrl: './player-hand-detail.component.html'
})
export class PlayerHandDetailComponent implements OnInit {
  playerHand: IPlayerHand;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ playerHand }) => {
      this.playerHand = playerHand;
    });
  }

  previousState() {
    window.history.back();
  }
}
