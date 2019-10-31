import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PokerSharedModule } from 'app/shared/shared.module';
import { ParseHistoryComponent } from './parse-history.component';
import { ParseHistoryDetailComponent } from './parse-history-detail.component';
import { ParseHistoryUpdateComponent } from './parse-history-update.component';
import { ParseHistoryDeletePopupComponent, ParseHistoryDeleteDialogComponent } from './parse-history-delete-dialog.component';
import { parseHistoryRoute, parseHistoryPopupRoute } from './parse-history.route';

const ENTITY_STATES = [...parseHistoryRoute, ...parseHistoryPopupRoute];

@NgModule({
  imports: [PokerSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    ParseHistoryComponent,
    ParseHistoryDetailComponent,
    ParseHistoryUpdateComponent,
    ParseHistoryDeleteDialogComponent,
    ParseHistoryDeletePopupComponent
  ],
  entryComponents: [ParseHistoryDeleteDialogComponent]
})
export class PokerParseHistoryModule {}
