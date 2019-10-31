import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { IBettingRound, BettingRound } from 'app/shared/model/betting-round.model';
import { BettingRoundService } from './betting-round.service';

@Component({
  selector: 'jhi-betting-round-update',
  templateUrl: './betting-round-update.component.html'
})
export class BettingRoundUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    description: []
  });

  constructor(protected bettingRoundService: BettingRoundService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ bettingRound }) => {
      this.updateForm(bettingRound);
    });
  }

  updateForm(bettingRound: IBettingRound) {
    this.editForm.patchValue({
      id: bettingRound.id,
      description: bettingRound.description
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const bettingRound = this.createFromForm();
    if (bettingRound.id !== undefined) {
      this.subscribeToSaveResponse(this.bettingRoundService.update(bettingRound));
    } else {
      this.subscribeToSaveResponse(this.bettingRoundService.create(bettingRound));
    }
  }

  private createFromForm(): IBettingRound {
    return {
      ...new BettingRound(),
      id: this.editForm.get(['id']).value,
      description: this.editForm.get(['description']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBettingRound>>) {
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
