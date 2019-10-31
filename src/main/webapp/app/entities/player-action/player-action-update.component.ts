import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IPlayerAction, PlayerAction } from 'app/shared/model/player-action.model';
import { PlayerActionService } from './player-action.service';
import { IPlayer } from 'app/shared/model/player.model';
import { PlayerService } from 'app/entities/player/player.service';
import { IGame } from 'app/shared/model/game.model';
import { GameService } from 'app/entities/game/game.service';
import { IHand } from 'app/shared/model/hand.model';
import { HandService } from 'app/entities/hand/hand.service';
import { IAction } from 'app/shared/model/action.model';
import { ActionService } from 'app/entities/action/action.service';
import { IBettingRound } from 'app/shared/model/betting-round.model';
import { BettingRoundService } from 'app/entities/betting-round/betting-round.service';

@Component({
  selector: 'jhi-player-action-update',
  templateUrl: './player-action-update.component.html'
})
export class PlayerActionUpdateComponent implements OnInit {
  isSaving: boolean;

  players: IPlayer[];

  games: IGame[];

  hands: IHand[];

  actions: IAction[];

  bettingrounds: IBettingRound[];

  editForm = this.fb.group({
    id: [],
    amount: [],
    player: [],
    game: [],
    hand: [],
    action: [],
    bettingRound: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected playerActionService: PlayerActionService,
    protected playerService: PlayerService,
    protected gameService: GameService,
    protected handService: HandService,
    protected actionService: ActionService,
    protected bettingRoundService: BettingRoundService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ playerAction }) => {
      this.updateForm(playerAction);
    });
    this.playerService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IPlayer[]>) => mayBeOk.ok),
        map((response: HttpResponse<IPlayer[]>) => response.body)
      )
      .subscribe((res: IPlayer[]) => (this.players = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.gameService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IGame[]>) => mayBeOk.ok),
        map((response: HttpResponse<IGame[]>) => response.body)
      )
      .subscribe((res: IGame[]) => (this.games = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.handService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IHand[]>) => mayBeOk.ok),
        map((response: HttpResponse<IHand[]>) => response.body)
      )
      .subscribe((res: IHand[]) => (this.hands = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.actionService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IAction[]>) => mayBeOk.ok),
        map((response: HttpResponse<IAction[]>) => response.body)
      )
      .subscribe((res: IAction[]) => (this.actions = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.bettingRoundService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IBettingRound[]>) => mayBeOk.ok),
        map((response: HttpResponse<IBettingRound[]>) => response.body)
      )
      .subscribe((res: IBettingRound[]) => (this.bettingrounds = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(playerAction: IPlayerAction) {
    this.editForm.patchValue({
      id: playerAction.id,
      amount: playerAction.amount,
      player: playerAction.player,
      game: playerAction.game,
      hand: playerAction.hand,
      action: playerAction.action,
      bettingRound: playerAction.bettingRound
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const playerAction = this.createFromForm();
    if (playerAction.id !== undefined) {
      this.subscribeToSaveResponse(this.playerActionService.update(playerAction));
    } else {
      this.subscribeToSaveResponse(this.playerActionService.create(playerAction));
    }
  }

  private createFromForm(): IPlayerAction {
    return {
      ...new PlayerAction(),
      id: this.editForm.get(['id']).value,
      amount: this.editForm.get(['amount']).value,
      player: this.editForm.get(['player']).value,
      game: this.editForm.get(['game']).value,
      hand: this.editForm.get(['hand']).value,
      action: this.editForm.get(['action']).value,
      bettingRound: this.editForm.get(['bettingRound']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPlayerAction>>) {
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

  trackGameById(index: number, item: IGame) {
    return item.id;
  }

  trackHandById(index: number, item: IHand) {
    return item.id;
  }

  trackActionById(index: number, item: IAction) {
    return item.id;
  }

  trackBettingRoundById(index: number, item: IBettingRound) {
    return item.id;
  }
}
