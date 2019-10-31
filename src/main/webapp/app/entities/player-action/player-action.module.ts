import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PokerSharedModule } from 'app/shared/shared.module';
import { PlayerActionComponent } from './player-action.component';
import { PlayerActionDetailComponent } from './player-action-detail.component';
import { PlayerActionUpdateComponent } from './player-action-update.component';
import { PlayerActionDeletePopupComponent, PlayerActionDeleteDialogComponent } from './player-action-delete-dialog.component';
import { playerActionRoute, playerActionPopupRoute } from './player-action.route';

const ENTITY_STATES = [...playerActionRoute, ...playerActionPopupRoute];

@NgModule({
  imports: [PokerSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    PlayerActionComponent,
    PlayerActionDetailComponent,
    PlayerActionUpdateComponent,
    PlayerActionDeleteDialogComponent,
    PlayerActionDeletePopupComponent
  ],
  entryComponents: [PlayerActionDeleteDialogComponent]
})
export class PokerPlayerActionModule {}
