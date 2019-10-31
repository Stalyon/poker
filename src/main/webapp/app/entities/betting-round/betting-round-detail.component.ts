import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBettingRound } from 'app/shared/model/betting-round.model';

@Component({
  selector: 'jhi-betting-round-detail',
  templateUrl: './betting-round-detail.component.html'
})
export class BettingRoundDetailComponent implements OnInit {
  bettingRound: IBettingRound;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ bettingRound }) => {
      this.bettingRound = bettingRound;
    });
  }

  previousState() {
    window.history.back();
  }
}
