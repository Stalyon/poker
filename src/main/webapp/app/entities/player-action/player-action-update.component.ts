import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';
import { IPlayerAction, PlayerAction } from 'app/shared/model/player-action.model';
import { PlayerActionService } from './player-action.service';
import { IPlayer } from 'app/shared/model/player.model';
import { PlayerService } from 'app/entities/player/player.service';
import { IGame } from 'app/shared/model/game.model';
import { GameService } from 'app/entities/game/game.service';
import { IHand } from 'app/shared/model/hand.model';
import { HandService } from 'app/entities/hand/hand.service';

@Component({
  selector: 'jhi-player-action-update',
  templateUrl: './player-action-update.component.html'
})
export class PlayerActionUpdateComponent implements OnInit {
  isSaving: boolean;

  players: IPlayer[];

  games: IGame[];

  hands: IHand[];

  editForm = this.fb.group({
    id: [],
    amount: [],
    bettingRound: [],
    action: [],
    callsPf: [],
    raisesPf: [],
    threeBetPf: [],
    callsFlop: [],
    betsFlop: [],
    raisesFlop: [],
    player: [],
    game: [],
    hand: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected playerActionService: PlayerActionService,
    protected playerService: PlayerService,
    protected gameService: GameService,
    protected handService: HandService,
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
      .subscribe((res: HttpResponse<IPlayer[]>) => (this.players = res.body), (res: HttpErrorResponse) => this.onError(res.message));
    this.gameService
      .query()
      .subscribe((res: HttpResponse<IGame[]>) => (this.games = res.body), (res: HttpErrorResponse) => this.onError(res.message));
    this.handService
      .query()
      .subscribe((res: HttpResponse<IHand[]>) => (this.hands = res.body), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(playerAction: IPlayerAction) {
    this.editForm.patchValue({
      id: playerAction.id,
      amount: playerAction.amount,
      bettingRound: playerAction.bettingRound,
      action: playerAction.action,
      callsPf: playerAction.callsPf,
      raisesPf: playerAction.raisesPf,
      threeBetPf: playerAction.threeBetPf,
      callsFlop: playerAction.callsFlop,
      betsFlop: playerAction.betsFlop,
      raisesFlop: playerAction.raisesFlop,
      player: playerAction.player,
      game: playerAction.game,
      hand: playerAction.hand
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
      bettingRound: this.editForm.get(['bettingRound']).value,
      action: this.editForm.get(['action']).value,
      callsPf: this.editForm.get(['callsPf']).value,
      raisesPf: this.editForm.get(['raisesPf']).value,
      threeBetPf: this.editForm.get(['threeBetPf']).value,
      callsFlop: this.editForm.get(['callsFlop']).value,
      betsFlop: this.editForm.get(['betsFlop']).value,
      raisesFlop: this.editForm.get(['raisesFlop']).value,
      player: this.editForm.get(['player']).value,
      game: this.editForm.get(['game']).value,
      hand: this.editForm.get(['hand']).value
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
}
