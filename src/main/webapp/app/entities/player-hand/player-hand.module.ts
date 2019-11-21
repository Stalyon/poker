import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PokerSharedModule } from 'app/shared/shared.module';
import { PlayerHandComponent } from './player-hand.component';
import { PlayerHandDetailComponent } from './player-hand-detail.component';
import { PlayerHandUpdateComponent } from './player-hand-update.component';
import { PlayerHandDeleteDialogComponent } from './player-hand-delete-dialog.component';
import { playerHandRoute } from './player-hand.route';

@NgModule({
  imports: [PokerSharedModule, RouterModule.forChild(playerHandRoute)],
  declarations: [PlayerHandComponent, PlayerHandDetailComponent, PlayerHandUpdateComponent, PlayerHandDeleteDialogComponent],
  entryComponents: [PlayerHandDeleteDialogComponent]
})
export class PokerPlayerHandModule {}
