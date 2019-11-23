import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PokerSharedModule } from 'app/shared/shared.module';
import {STATS_ROUTE} from "app/stats/stats.route";
import {StatsComponent} from "app/stats/stats.component";
import {NgxEchartsModule} from "ngx-echarts";

@NgModule({
  imports: [PokerSharedModule, NgxEchartsModule, RouterModule.forChild([STATS_ROUTE])],
  declarations: [StatsComponent]
})
export class PokerStatsModule {}
