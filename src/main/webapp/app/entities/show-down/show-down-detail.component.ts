import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IShowDown } from 'app/shared/model/show-down.model';

@Component({
  selector: 'jhi-show-down-detail',
  templateUrl: './show-down-detail.component.html'
})
export class ShowDownDetailComponent implements OnInit {
  showDown: IShowDown;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ showDown }) => {
      this.showDown = showDown;
    });
  }

  previousState() {
    window.history.back();
  }
}
