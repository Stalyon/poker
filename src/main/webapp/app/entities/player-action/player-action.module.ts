import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PokerSharedModule } from 'app/shared/shared.module';
import { PlayerActionComponent } from './player-action.component';
import { PlayerActionDetailComponent } from './player-action-detail.component';
import { PlayerActionUpdateComponent } from './player-action-update.component';
import { PlayerActionDeleteDialogComponent } from './player-action-delete-dialog.component';
import { playerActionRoute } from './player-action.route';

@NgModule({
  imports: [PokerSharedModule, RouterModule.forChild(playerActionRoute)],
  declarations: [PlayerActionComponent, PlayerActionDetailComponent, PlayerActionUpdateComponent, PlayerActionDeleteDialogComponent],
  entryComponents: [PlayerActionDeleteDialogComponent]
})
export class PokerPlayerActionModule {}
