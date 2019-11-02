import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PokerSharedModule } from 'app/shared/shared.module';
import {LIVE_ROUTE} from './live.route';
import {LiveComponent} from "app/live/live.component";

@NgModule({
  imports: [PokerSharedModule, RouterModule.forChild([LIVE_ROUTE])],
  declarations: [LiveComponent]
})
export class PokerLiveModule {}
