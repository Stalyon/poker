import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { IParseHistory, ParseHistory } from 'app/shared/model/parse-history.model';
import { ParseHistoryService } from './parse-history.service';

@Component({
  selector: 'jhi-parse-history-update',
  templateUrl: './parse-history-update.component.html'
})
export class ParseHistoryUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    fileName: [],
    fileSize: [],
    parsedDate: []
  });

  constructor(protected parseHistoryService: ParseHistoryService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ parseHistory }) => {
      this.updateForm(parseHistory);
    });
  }

  updateForm(parseHistory: IParseHistory) {
    this.editForm.patchValue({
      id: parseHistory.id,
      fileName: parseHistory.fileName,
      fileSize: parseHistory.fileSize,
      parsedDate: parseHistory.parsedDate != null ? parseHistory.parsedDate.format(DATE_TIME_FORMAT) : null
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
        this.editForm.get(['parsedDate']).value != null ? moment(this.editForm.get(['parsedDate']).value, DATE_TIME_FORMAT) : undefined
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
}
