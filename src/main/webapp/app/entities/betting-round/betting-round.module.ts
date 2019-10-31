import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PokerSharedModule } from 'app/shared/shared.module';
import { BettingRoundComponent } from './betting-round.component';
import { BettingRoundDetailComponent } from './betting-round-detail.component';
import { BettingRoundUpdateComponent } from './betting-round-update.component';
import { BettingRoundDeletePopupComponent, BettingRoundDeleteDialogComponent } from './betting-round-delete-dialog.component';
import { bettingRoundRoute, bettingRoundPopupRoute } from './betting-round.route';

const ENTITY_STATES = [...bettingRoundRoute, ...bettingRoundPopupRoute];

@NgModule({
  imports: [PokerSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    BettingRoundComponent,
    BettingRoundDetailComponent,
    BettingRoundUpdateComponent,
    BettingRoundDeleteDialogComponent,
    BettingRoundDeletePopupComponent
  ],
  entryComponents: [BettingRoundDeleteDialogComponent]
})
export class PokerBettingRoundModule {}
