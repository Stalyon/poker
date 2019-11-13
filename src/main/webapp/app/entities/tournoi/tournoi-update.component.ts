import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';
import { ITournoi, Tournoi } from 'app/shared/model/tournoi.model';
import { TournoiService } from './tournoi.service';
import { IGameHistory } from 'app/shared/model/game-history.model';
import { GameHistoryService } from 'app/entities/game-history/game-history.service';

@Component({
  selector: 'jhi-tournoi-update',
  templateUrl: './tournoi-update.component.html'
})
export class TournoiUpdateComponent implements OnInit {
  isSaving: boolean;

  gamehistories: IGameHistory[];

  editForm = this.fb.group({
    id: [],
    buyIn: [],
    rebuy: [],
    ranking: [],
    profit: [],
    bounty: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected tournoiService: TournoiService,
    protected gameHistoryService: GameHistoryService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ tournoi }) => {
      this.updateForm(tournoi);
    });
    this.gameHistoryService
      .query()
      .subscribe(
        (res: HttpResponse<IGameHistory[]>) => (this.gamehistories = res.body),
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  updateForm(tournoi: ITournoi) {
    this.editForm.patchValue({
      id: tournoi.id,
      buyIn: tournoi.buyIn,
      rebuy: tournoi.rebuy,
      ranking: tournoi.ranking,
      profit: tournoi.profit,
      bounty: tournoi.bounty
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const tournoi = this.createFromForm();
    if (tournoi.id !== undefined) {
      this.subscribeToSaveResponse(this.tournoiService.update(tournoi));
    } else {
      this.subscribeToSaveResponse(this.tournoiService.create(tournoi));
    }
  }

  private createFromForm(): ITournoi {
    return {
      ...new Tournoi(),
      id: this.editForm.get(['id']).value,
      buyIn: this.editForm.get(['buyIn']).value,
      rebuy: this.editForm.get(['rebuy']).value,
      ranking: this.editForm.get(['ranking']).value,
      profit: this.editForm.get(['profit']).value,
      bounty: this.editForm.get(['bounty']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITournoi>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackGameHistoryById(index: number, item: IGameHistory) {
    return item.id;
  }
}
