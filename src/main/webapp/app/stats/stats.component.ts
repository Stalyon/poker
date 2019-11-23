import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {StatsService} from "app/stats/stats.service";
import { EChartOption } from 'echarts';
import {HttpResponse} from "@angular/common/http";
import {Stats} from "app/shared/model/stats.model";
import {JhiAlertService} from "ng-jhipster";
import {FormBuilder, Validators} from "@angular/forms";
import {DATE_TIME_FORMAT} from "app/shared/constants/input.constants";
import * as moment from 'moment';
import {IRequestStats, RequestStats} from "app/shared/model/request-stats.model";

@Component({
  selector: 'jhi-stats',
  templateUrl: './stats.component.html',
  styleUrls: ['stats.scss']
})
export class StatsComponent {
  chartOption: EChartOption;

  editForm = this.fb.group({
    beforeDate: [new Date(2010, 0, 0), [Validators.required]],
    afterDate: [new Date(), [Validators.required]],
    tournoi: [true],
    sitAndGo: [true],
    cashGame: [true],
  });

  constructor(private statsService: StatsService, private cdr: ChangeDetectorRef,
              private alertService: JhiAlertService, private fb: FormBuilder) {}

  save(): void {
    this.statsService.getStats(this.createFromForm())
      .subscribe((res: HttpResponse<Stats>) => this.onSuccess(res.body));
  }

  private createFromForm(): IRequestStats {
    let gameTypes: string[] = [];
    if (this.editForm.get(['tournoi']).value === true) {
      gameTypes.push('TOURNOI');
    }
    if (this.editForm.get(['sitAndGo']).value === true) {
      gameTypes.push('SIT_AND_GO');
    }
    if (this.editForm.get(['cashGame']).value === true) {
      gameTypes.push('CASH_GAME');
    }

    return {
      ...new RequestStats(),
      beforeDate:
        this.editForm.get(['beforeDate']).value != null ? moment(this.editForm.get(['beforeDate']).value, DATE_TIME_FORMAT) : undefined,
      afterDate:
        this.editForm.get(['afterDate']).value != null ? moment(this.editForm.get(['afterDate']).value, DATE_TIME_FORMAT) : undefined,
      gameTypes: gameTypes,
    };
  }

  private onSuccess(data) {
    // Ajout du tooltip
    data.chartOption.tooltip = {
      trigger: 'axis',
      position: (pt) => [pt[0], '10%']
    };

    // Ajout du symbôle euro
    data.chartOption.yAxis.axisLabel = {
      formatter: '{value}€'
    };

    this.chartOption = data.chartOption;
  }
}
