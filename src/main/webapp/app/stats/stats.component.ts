import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {StatsService} from "app/stats/stats.service";
import { EChartOption } from 'echarts';
import {HttpResponse} from "@angular/common/http";
import {Stats} from "app/shared/model/stats.model";
import {JhiAlertService} from "ng-jhipster";

@Component({
  selector: 'jhi-stats',
  templateUrl: './stats.component.html',
  styleUrls: ['stats.scss']
})
export class StatsComponent implements OnInit {
  chartOption: EChartOption;

  constructor(private statsService: StatsService, private cdr: ChangeDetectorRef,
              private alertService: JhiAlertService) {}

  ngOnInit() {
    this.statsService.getStats()
      .subscribe((res: HttpResponse<Stats>) => this.onSuccess(res.body),
        (res: HttpResponse<any>) => this.onError(res.body));
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

  private onError(error) {
    this.alertService.error(error.error, error.message, null);
  }
}
