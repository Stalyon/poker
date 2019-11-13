import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { IGameHistory, GameHistory } from 'app/shared/model/game-history.model';
import { GameHistoryService } from './game-history.service';
import { ISitAndGo } from 'app/shared/model/sit-and-go.model';
import { SitAndGoService } from 'app/entities/sit-and-go/sit-and-go.service';
import { ITournoi } from 'app/shared/model/tournoi.model';
import { TournoiService } from 'app/entities/tournoi/tournoi.service';
import { ICashGame } from 'app/shared/model/cash-game.model';
import { CashGameService } from 'app/entities/cash-game/cash-game.service';

@Component({
  selector: 'jhi-game-history-update',
  templateUrl: './game-history-update.component.html'
})
export class GameHistoryUpdateComponent implements OnInit {
  isSaving: boolean;

  sitandgos: ISitAndGo[];

  tournois: ITournoi[];

  cashgames: ICashGame[];

  editForm = this.fb.group({
    id: [],
    startDate: [null, [Validators.required]],
    name: [null, [Validators.required]],
    type: [],
    netResult: [],
    sitAndGo: [],
    tournoi: [],
    cashGame: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected gameHistoryService: GameHistoryService,
    protected sitAndGoService: SitAndGoService,
    protected tournoiService: TournoiService,
    protected cashGameService: CashGameService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ gameHistory }) => {
      this.updateForm(gameHistory);
    });
    this.sitAndGoService.query({ filter: 'gamehistory-is-null' }).subscribe(
      (res: HttpResponse<ISitAndGo[]>) => {
        if (!this.editForm.get('sitAndGo').value || !this.editForm.get('sitAndGo').value.id) {
          this.sitandgos = res.body;
        } else {
          this.sitAndGoService
            .find(this.editForm.get('sitAndGo').value.id)
            .subscribe(
              (subRes: HttpResponse<ISitAndGo>) => (this.sitandgos = [subRes.body].concat(res.body)),
              (subRes: HttpErrorResponse) => this.onError(subRes.message)
            );
        }
      },
      (res: HttpErrorResponse) => this.onError(res.message)
    );
    this.tournoiService.query({ filter: 'gamehistory-is-null' }).subscribe(
      (res: HttpResponse<ITournoi[]>) => {
        if (!this.editForm.get('tournoi').value || !this.editForm.get('tournoi').value.id) {
          this.tournois = res.body;
        } else {
          this.tournoiService
            .find(this.editForm.get('tournoi').value.id)
            .subscribe(
              (subRes: HttpResponse<ITournoi>) => (this.tournois = [subRes.body].concat(res.body)),
              (subRes: HttpErrorResponse) => this.onError(subRes.message)
            );
        }
      },
      (res: HttpErrorResponse) => this.onError(res.message)
    );
    this.cashGameService.query({ filter: 'gamehistory-is-null' }).subscribe(
      (res: HttpResponse<ICashGame[]>) => {
        if (!this.editForm.get('cashGame').value || !this.editForm.get('cashGame').value.id) {
          this.cashgames = res.body;
        } else {
          this.cashGameService
            .find(this.editForm.get('cashGame').value.id)
            .subscribe(
              (subRes: HttpResponse<ICashGame>) => (this.cashgames = [subRes.body].concat(res.body)),
              (subRes: HttpErrorResponse) => this.onError(subRes.message)
            );
        }
      },
      (res: HttpErrorResponse) => this.onError(res.message)
    );
  }

  updateForm(gameHistory: IGameHistory) {
    this.editForm.patchValue({
      id: gameHistory.id,
      startDate: gameHistory.startDate != null ? gameHistory.startDate.format(DATE_TIME_FORMAT) : null,
      name: gameHistory.name,
      type: gameHistory.type,
      netResult: gameHistory.netResult,
      sitAndGo: gameHistory.sitAndGo,
      tournoi: gameHistory.tournoi,
      cashGame: gameHistory.cashGame
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const gameHistory = this.createFromForm();
    if (gameHistory.id !== undefined) {
      this.subscribeToSaveResponse(this.gameHistoryService.update(gameHistory));
    } else {
      this.subscribeToSaveResponse(this.gameHistoryService.create(gameHistory));
    }
  }

  private createFromForm(): IGameHistory {
    return {
      ...new GameHistory(),
      id: this.editForm.get(['id']).value,
      startDate:
        this.editForm.get(['startDate']).value != null ? moment(this.editForm.get(['startDate']).value, DATE_TIME_FORMAT) : undefined,
      name: this.editForm.get(['name']).value,
      type: this.editForm.get(['type']).value,
      netResult: this.editForm.get(['netResult']).value,
      sitAndGo: this.editForm.get(['sitAndGo']).value,
      tournoi: this.editForm.get(['tournoi']).value,
      cashGame: this.editForm.get(['cashGame']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGameHistory>>) {
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

  trackSitAndGoById(index: number, item: ISitAndGo) {
    return item.id;
  }

  trackTournoiById(index: number, item: ITournoi) {
    return item.id;
  }

  trackCashGameById(index: number, item: ICashGame) {
    return item.id;
  }
}
