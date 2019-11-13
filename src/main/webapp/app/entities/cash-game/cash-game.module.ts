import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PokerSharedModule } from 'app/shared/shared.module';
import { CashGameComponent } from './cash-game.component';
import { CashGameDetailComponent } from './cash-game-detail.component';
import { CashGameUpdateComponent } from './cash-game-update.component';
import { CashGameDeletePopupComponent, CashGameDeleteDialogComponent } from './cash-game-delete-dialog.component';
import { cashGameRoute, cashGamePopupRoute } from './cash-game.route';

const ENTITY_STATES = [...cashGameRoute, ...cashGamePopupRoute];

@NgModule({
  imports: [PokerSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    CashGameComponent,
    CashGameDetailComponent,
    CashGameUpdateComponent,
    CashGameDeleteDialogComponent,
    CashGameDeletePopupComponent
  ],
  entryComponents: [CashGameDeleteDialogComponent]
})
export class PokerCashGameModule {}
