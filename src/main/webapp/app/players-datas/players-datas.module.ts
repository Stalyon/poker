import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PokerSharedModule } from 'app/shared/shared.module';
import {PLAYERS_DATAS_ROUTE} from "app/players-datas/players-datas.route";
import {PlayersDatasComponent} from "app/players-datas/players-datas.component";

@NgModule({
  imports: [PokerSharedModule, RouterModule.forChild([PLAYERS_DATAS_ROUTE])],
  declarations: [PlayersDatasComponent]
})
export class PokerPlayersDatasModule {}
