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
import { IHand, Hand } from 'app/shared/model/hand.model';
import { HandService } from './hand.service';
import { IGame } from 'app/shared/model/game.model';
import { GameService } from 'app/entities/game/game.service';
import { IPlayer } from 'app/shared/model/player.model';
import { PlayerService } from 'app/entities/player/player.service';

@Component({
  selector: 'jhi-hand-update',
  templateUrl: './hand-update.component.html'
})
export class HandUpdateComponent implements OnInit {
  isSaving: boolean;

  games: IGame[];

  players: IPlayer[];

  editForm = this.fb.group({
    id: [],
    startDate: [],
    buttonPosition: [],
    myCards: [],
    flopCards: [],
    riverCards: [],
    turnCards: [],
    game: [],
    winner: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected handService: HandService,
    protected gameService: GameService,
    protected playerService: PlayerService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ hand }) => {
      this.updateForm(hand);
    });
    this.gameService
      .query()
      .subscribe((res: HttpResponse<IGame[]>) => (this.games = res.body), (res: HttpErrorResponse) => this.onError(res.message));
    this.playerService
      .query()
      .subscribe((res: HttpResponse<IPlayer[]>) => (this.players = res.body), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(hand: IHand) {
    this.editForm.patchValue({
      id: hand.id,
      startDate: hand.startDate != null ? hand.startDate.format(DATE_TIME_FORMAT) : null,
      buttonPosition: hand.buttonPosition,
      myCards: hand.myCards,
      flopCards: hand.flopCards,
      riverCards: hand.riverCards,
      turnCards: hand.turnCards,
      game: hand.game,
      winner: hand.winner
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const hand = this.createFromForm();
    if (hand.id !== undefined) {
      this.subscribeToSaveResponse(this.handService.update(hand));
    } else {
      this.subscribeToSaveResponse(this.handService.create(hand));
    }
  }

  private createFromForm(): IHand {
    return {
      ...new Hand(),
      id: this.editForm.get(['id']).value,
      startDate:
        this.editForm.get(['startDate']).value != null ? moment(this.editForm.get(['startDate']).value, DATE_TIME_FORMAT) : undefined,
      buttonPosition: this.editForm.get(['buttonPosition']).value,
      myCards: this.editForm.get(['myCards']).value,
      flopCards: this.editForm.get(['flopCards']).value,
      riverCards: this.editForm.get(['riverCards']).value,
      turnCards: this.editForm.get(['turnCards']).value,
      game: this.editForm.get(['game']).value,
      winner: this.editForm.get(['winner']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IHand>>) {
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

  trackPlayerById(index: number, item: IPlayer) {
    return item.id;
  }
}
