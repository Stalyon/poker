import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PokerSharedModule } from 'app/shared/shared.module';
import { GameHistoryComponent } from './game-history.component';
import { GameHistoryDetailComponent } from './game-history-detail.component';
import { GameHistoryUpdateComponent } from './game-history-update.component';
import { GameHistoryDeletePopupComponent, GameHistoryDeleteDialogComponent } from './game-history-delete-dialog.component';
import { gameHistoryRoute, gameHistoryPopupRoute } from './game-history.route';

const ENTITY_STATES = [...gameHistoryRoute, ...gameHistoryPopupRoute];

@NgModule({
  imports: [PokerSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    GameHistoryComponent,
    GameHistoryDetailComponent,
    GameHistoryUpdateComponent,
    GameHistoryDeleteDialogComponent,
    GameHistoryDeletePopupComponent
  ],
  entryComponents: [GameHistoryDeleteDialogComponent]
})
export class PokerGameHistoryModule {}
