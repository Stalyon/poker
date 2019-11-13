import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITournoi } from 'app/shared/model/tournoi.model';

@Component({
  selector: 'jhi-tournoi-detail',
  templateUrl: './tournoi-detail.component.html'
})
export class TournoiDetailComponent implements OnInit {
  tournoi: ITournoi;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ tournoi }) => {
      this.tournoi = tournoi;
    });
  }

  previousState() {
    window.history.back();
  }
}
