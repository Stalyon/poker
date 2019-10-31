import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IShowDown, ShowDown } from 'app/shared/model/show-down.model';
import { ShowDownService } from './show-down.service';
import { IHand } from 'app/shared/model/hand.model';
import { HandService } from 'app/entities/hand/hand.service';
import { IPlayer } from 'app/shared/model/player.model';
import { PlayerService } from 'app/entities/player/player.service';

@Component({
  selector: 'jhi-show-down-update',
  templateUrl: './show-down-update.component.html'
})
export class ShowDownUpdateComponent implements OnInit {
  isSaving: boolean;

  hands: IHand[];

  players: IPlayer[];

  editForm = this.fb.group({
    id: [],
    cards: [],
    wins: [],
    hand: [],
    player: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected showDownService: ShowDownService,
    protected handService: HandService,
    protected playerService: PlayerService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ showDown }) => {
      this.updateForm(showDown);
    });
    this.handService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IHand[]>) => mayBeOk.ok),
        map((response: HttpResponse<IHand[]>) => response.body)
      )
      .subscribe((res: IHand[]) => (this.hands = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.playerService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IPlayer[]>) => mayBeOk.ok),
        map((response: HttpResponse<IPlayer[]>) => response.body)
      )
      .subscribe((res: IPlayer[]) => (this.players = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(showDown: IShowDown) {
    this.editForm.patchValue({
      id: showDown.id,
      cards: showDown.cards,
      wins: showDown.wins,
      hand: showDown.hand,
      player: showDown.player
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const showDown = this.createFromForm();
    if (showDown.id !== undefined) {
      this.subscribeToSaveResponse(this.showDownService.update(showDown));
    } else {
      this.subscribeToSaveResponse(this.showDownService.create(showDown));
    }
  }

  private createFromForm(): IShowDown {
    return {
      ...new ShowDown(),
      id: this.editForm.get(['id']).value,
      cards: this.editForm.get(['cards']).value,
      wins: this.editForm.get(['wins']).value,
      hand: this.editForm.get(['hand']).value,
      player: this.editForm.get(['player']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IShowDown>>) {
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

  trackHandById(index: number, item: IHand) {
    return item.id;
  }

  trackPlayerById(index: number, item: IPlayer) {
    return item.id;
  }
}
