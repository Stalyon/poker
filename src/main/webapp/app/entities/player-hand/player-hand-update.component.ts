import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';
import { IPlayerHand, PlayerHand } from 'app/shared/model/player-hand.model';
import { PlayerHandService } from './player-hand.service';
import { IPlayer } from 'app/shared/model/player.model';
import { PlayerService } from 'app/entities/player/player.service';
import { IHand } from 'app/shared/model/hand.model';
import { HandService } from 'app/entities/hand/hand.service';

@Component({
  selector: 'jhi-player-hand-update',
  templateUrl: './player-hand-update.component.html'
})
export class PlayerHandUpdateComponent implements OnInit {
  isSaving: boolean;

  players: IPlayer[];

  hands: IHand[];

  editForm = this.fb.group({
    id: [],
    callsPf: [],
    raisesPf: [],
    threeBetPf: [],
    callsFlop: [],
    betsFlop: [],
    raisesFlop: [],
    player: [],
    player: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected playerHandService: PlayerHandService,
    protected playerService: PlayerService,
    protected handService: HandService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ playerHand }) => {
      this.updateForm(playerHand);
    });
    this.playerService
      .query()
      .subscribe((res: HttpResponse<IPlayer[]>) => (this.players = res.body), (res: HttpErrorResponse) => this.onError(res.message));
    this.handService
      .query()
      .subscribe((res: HttpResponse<IHand[]>) => (this.hands = res.body), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(playerHand: IPlayerHand) {
    this.editForm.patchValue({
      id: playerHand.id,
      callsPf: playerHand.callsPf,
      raisesPf: playerHand.raisesPf,
      threeBetPf: playerHand.threeBetPf,
      callsFlop: playerHand.callsFlop,
      betsFlop: playerHand.betsFlop,
      raisesFlop: playerHand.raisesFlop,
      player: playerHand.player,
      player: playerHand.player
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const playerHand = this.createFromForm();
    if (playerHand.id !== undefined) {
      this.subscribeToSaveResponse(this.playerHandService.update(playerHand));
    } else {
      this.subscribeToSaveResponse(this.playerHandService.create(playerHand));
    }
  }

  private createFromForm(): IPlayerHand {
    return {
      ...new PlayerHand(),
      id: this.editForm.get(['id']).value,
      callsPf: this.editForm.get(['callsPf']).value,
      raisesPf: this.editForm.get(['raisesPf']).value,
      threeBetPf: this.editForm.get(['threeBetPf']).value,
      callsFlop: this.editForm.get(['callsFlop']).value,
      betsFlop: this.editForm.get(['betsFlop']).value,
      raisesFlop: this.editForm.get(['raisesFlop']).value,
      player: this.editForm.get(['player']).value,
      player: this.editForm.get(['player']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPlayerHand>>) {
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

  trackHandById(index: number, item: IHand) {
    return item.id;
  }
}
