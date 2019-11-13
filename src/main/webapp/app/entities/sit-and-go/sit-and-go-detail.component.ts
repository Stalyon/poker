import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISitAndGo } from 'app/shared/model/sit-and-go.model';

@Component({
  selector: 'jhi-sit-and-go-detail',
  templateUrl: './sit-and-go-detail.component.html'
})
export class SitAndGoDetailComponent implements OnInit {
  sitAndGo: ISitAndGo;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ sitAndGo }) => {
      this.sitAndGo = sitAndGo;
    });
  }

  previousState() {
    window.history.back();
  }
}
