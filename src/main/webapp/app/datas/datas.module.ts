import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import {PokerSharedModule} from "app/shared/shared.module";
import {DatasComponent} from "app/datas/datas.component";
import {DATAS_ROUTE} from "app/datas/datas.route";


@NgModule({
  imports: [PokerSharedModule, RouterModule.forChild([DATAS_ROUTE])],
  declarations: [DatasComponent]
})
export class PokerDatasModule {}
