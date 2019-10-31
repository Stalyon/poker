import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPlayerAction } from 'app/shared/model/player-action.model';

@Component({
  selector: 'jhi-player-action-detail',
  templateUrl: './player-action-detail.component.html'
})
export class PlayerActionDetailComponent implements OnInit {
  playerAction: IPlayerAction;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ playerAction }) => {
      this.playerAction = playerAction;
    });
  }

  previousState() {
    window.history.back();
  }
}
