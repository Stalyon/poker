import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { IParseHistory, ParseHistory } from 'app/shared/model/parse-history.model';
import { ParseHistoryService } from './parse-history.service';
import { IGame } from 'app/shared/model/game.model';
import { GameService } from 'app/entities/game/game.service';

@Component({
  selector: 'jhi-parse-history-update',
  templateUrl: './parse-history-update.component.html'
})
export class ParseHistoryUpdateComponent implements OnInit {
  isSaving: boolean;

  games: IGame[];

  editForm = this.fb.group({
    id: [],
    fileName: [],
    fileSize: [],
    parsedDate: [],
    game: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected parseHistoryService: ParseHistoryService,
    protected gameService: GameService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ parseHistory }) => {
      this.updateForm(parseHistory);
    });
    this.gameService
      .query({ filter: 'parsehistory-is-null' })
      .pipe(
        filter((mayBeOk: HttpResponse<IGame[]>) => mayBeOk.ok),
        map((response: HttpResponse<IGame[]>) => response.body)
      )
      .subscribe(
        (res: IGame[]) => {
          if (!this.editForm.get('game').value || !this.editForm.get('game').value.id) {
            this.games = res;
          } else {
            this.gameService
              .find(this.editForm.get('game').value.id)
              .pipe(
                filter((subResMayBeOk: HttpResponse<IGame>) => subResMayBeOk.ok),
                map((subResponse: HttpResponse<IGame>) => subResponse.body)
              )
              .subscribe(
                (subRes: IGame) => (this.games = [subRes].concat(res)),
                (subRes: HttpErrorResponse) => this.onError(subRes.message)
              );
          }
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  updateForm(parseHistory: IParseHistory) {
    this.editForm.patchValue({
      id: parseHistory.id,
      fileName: parseHistory.fileName,
      fileSize: parseHistory.fileSize,
      parsedDate: parseHistory.parsedDate != null ? parseHistory.parsedDate.format(DATE_TIME_FORMAT) : null,
      game: parseHistory.game
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const parseHistory = this.createFromForm();
    if (parseHistory.id !== undefined) {
      this.subscribeToSaveResponse(this.parseHistoryService.update(parseHistory));
    } else {
      this.subscribeToSaveResponse(this.parseHistoryService.create(parseHistory));
    }
  }

  private createFromForm(): IParseHistory {
    return {
      ...new ParseHistory(),
      id: this.editForm.get(['id']).value,
      fileName: this.editForm.get(['fileName']).value,
      fileSize: this.editForm.get(['fileSize']).value,
      parsedDate:
        this.editForm.get(['parsedDate']).value != null ? moment(this.editForm.get(['parsedDate']).value, DATE_TIME_FORMAT) : undefined,
      game: this.editForm.get(['game']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IParseHistory>>) {
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

  trackGameById(index: number, item: IGame) {
    return item.id;
  }
}
