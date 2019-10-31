import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { IPlayer, Player } from 'app/shared/model/player.model';
import { PlayerService } from './player.service';

@Component({
  selector: 'jhi-player-update',
  templateUrl: './player-update.component.html'
})
export class PlayerUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    name: [],
    addedDate: [],
    isMe: []
  });

  constructor(protected playerService: PlayerService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ player }) => {
      this.updateForm(player);
    });
  }

  updateForm(player: IPlayer) {
    this.editForm.patchValue({
      id: player.id,
      name: player.name,
      addedDate: player.addedDate != null ? player.addedDate.format(DATE_TIME_FORMAT) : null,
      isMe: player.isMe
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const player = this.createFromForm();
    if (player.id !== undefined) {
      this.subscribeToSaveResponse(this.playerService.update(player));
    } else {
      this.subscribeToSaveResponse(this.playerService.create(player));
    }
  }

  private createFromForm(): IPlayer {
    return {
      ...new Player(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      addedDate:
        this.editForm.get(['addedDate']).value != null ? moment(this.editForm.get(['addedDate']).value, DATE_TIME_FORMAT) : undefined,
      isMe: this.editForm.get(['isMe']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPlayer>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
}
