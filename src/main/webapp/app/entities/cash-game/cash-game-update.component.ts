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
import { ICashGame, CashGame } from 'app/shared/model/cash-game.model';
import { CashGameService } from './cash-game.service';
import { IGameHistory } from 'app/shared/model/game-history.model';
import { GameHistoryService } from 'app/entities/game-history/game-history.service';

@Component({
  selector: 'jhi-cash-game-update',
  templateUrl: './cash-game-update.component.html'
})
export class CashGameUpdateComponent implements OnInit {
  isSaving: boolean;

  gamehistories: IGameHistory[];

  editForm = this.fb.group({
    id: [],
    endDate: [],
    profit: [],
    table: [],
    sbBb: [],
    nbHands: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected cashGameService: CashGameService,
    protected gameHistoryService: GameHistoryService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ cashGame }) => {
      this.updateForm(cashGame);
    });
    this.gameHistoryService
      .query()
      .subscribe(
        (res: HttpResponse<IGameHistory[]>) => (this.gamehistories = res.body),
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  updateForm(cashGame: ICashGame) {
    this.editForm.patchValue({
      id: cashGame.id,
      endDate: cashGame.endDate != null ? cashGame.endDate.format(DATE_TIME_FORMAT) : null,
      profit: cashGame.profit,
      table: cashGame.table,
      sbBb: cashGame.sbBb,
      nbHands: cashGame.nbHands
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const cashGame = this.createFromForm();
    if (cashGame.id !== undefined) {
      this.subscribeToSaveResponse(this.cashGameService.update(cashGame));
    } else {
      this.subscribeToSaveResponse(this.cashGameService.create(cashGame));
    }
  }

  private createFromForm(): ICashGame {
    return {
      ...new CashGame(),
      id: this.editForm.get(['id']).value,
      endDate: this.editForm.get(['endDate']).value != null ? moment(this.editForm.get(['endDate']).value, DATE_TIME_FORMAT) : undefined,
      profit: this.editForm.get(['profit']).value,
      table: this.editForm.get(['table']).value,
      sbBb: this.editForm.get(['sbBb']).value,
      nbHands: this.editForm.get(['nbHands']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICashGame>>) {
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
