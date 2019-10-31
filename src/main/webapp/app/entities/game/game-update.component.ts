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
import { IGame, Game } from 'app/shared/model/game.model';
import { GameService } from './game.service';
import { IPlayer } from 'app/shared/model/player.model';
import { PlayerService } from 'app/entities/player/player.service';

@Component({
  selector: 'jhi-game-update',
  templateUrl: './game-update.component.html'
})
export class GameUpdateComponent implements OnInit {
  isSaving: boolean;

  players: IPlayer[];

  editForm = this.fb.group({
    id: [],
    name: [],
    startDate: [],
    endDate: [],
    player: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected gameService: GameService,
    protected playerService: PlayerService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ game }) => {
      this.updateForm(game);
    });
    this.playerService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IPlayer[]>) => mayBeOk.ok),
        map((response: HttpResponse<IPlayer[]>) => response.body)
      )
      .subscribe((res: IPlayer[]) => (this.players = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(game: IGame) {
    this.editForm.patchValue({
      id: game.id,
      name: game.name,
      startDate: game.startDate != null ? game.startDate.format(DATE_TIME_FORMAT) : null,
      endDate: game.endDate != null ? game.endDate.format(DATE_TIME_FORMAT) : null,
      player: game.player
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const game = this.createFromForm();
    if (game.id !== undefined) {
      this.subscribeToSaveResponse(this.gameService.update(game));
    } else {
      this.subscribeToSaveResponse(this.gameService.create(game));
    }
  }

  private createFromForm(): IGame {
    return {
      ...new Game(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      startDate:
        this.editForm.get(['startDate']).value != null ? moment(this.editForm.get(['startDate']).value, DATE_TIME_FORMAT) : undefined,
      endDate: this.editForm.get(['endDate']).value != null ? moment(this.editForm.get(['endDate']).value, DATE_TIME_FORMAT) : undefined,
      player: this.editForm.get(['player']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGame>>) {
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

  trackPlayerById(index: number, item: IPlayer) {
    return item.id;
  }
}
