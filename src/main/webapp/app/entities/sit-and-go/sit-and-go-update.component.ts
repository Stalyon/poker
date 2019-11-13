import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';
import { ISitAndGo, SitAndGo } from 'app/shared/model/sit-and-go.model';
import { SitAndGoService } from './sit-and-go.service';
import { IGameHistory } from 'app/shared/model/game-history.model';
import { GameHistoryService } from 'app/entities/game-history/game-history.service';

@Component({
  selector: 'jhi-sit-and-go-update',
  templateUrl: './sit-and-go-update.component.html'
})
export class SitAndGoUpdateComponent implements OnInit {
  isSaving: boolean;

  gamehistories: IGameHistory[];

  editForm = this.fb.group({
    id: [],
    format: [],
    ranking: [],
    profit: [],
    bounty: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected sitAndGoService: SitAndGoService,
    protected gameHistoryService: GameHistoryService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ sitAndGo }) => {
      this.updateForm(sitAndGo);
    });
    this.gameHistoryService
      .query()
      .subscribe(
        (res: HttpResponse<IGameHistory[]>) => (this.gamehistories = res.body),
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  updateForm(sitAndGo: ISitAndGo) {
    this.editForm.patchValue({
      id: sitAndGo.id,
      format: sitAndGo.format,
      ranking: sitAndGo.ranking,
      profit: sitAndGo.profit,
      bounty: sitAndGo.bounty
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const sitAndGo = this.createFromForm();
    if (sitAndGo.id !== undefined) {
      this.subscribeToSaveResponse(this.sitAndGoService.update(sitAndGo));
    } else {
      this.subscribeToSaveResponse(this.sitAndGoService.create(sitAndGo));
    }
  }

  private createFromForm(): ISitAndGo {
    return {
      ...new SitAndGo(),
      id: this.editForm.get(['id']).value,
      format: this.editForm.get(['format']).value,
      ranking: this.editForm.get(['ranking']).value,
      profit: this.editForm.get(['profit']).value,
      bounty: this.editForm.get(['bounty']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISitAndGo>>) {
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
