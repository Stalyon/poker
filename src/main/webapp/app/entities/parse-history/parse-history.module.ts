import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PokerSharedModule } from 'app/shared/shared.module';
import { ParseHistoryComponent } from './parse-history.component';
import { ParseHistoryDetailComponent } from './parse-history-detail.component';
import { ParseHistoryUpdateComponent } from './parse-history-update.component';
import { ParseHistoryDeleteDialogComponent } from './parse-history-delete-dialog.component';
import { parseHistoryRoute } from './parse-history.route';

@NgModule({
  imports: [PokerSharedModule, RouterModule.forChild(parseHistoryRoute)],
  declarations: [ParseHistoryComponent, ParseHistoryDetailComponent, ParseHistoryUpdateComponent, ParseHistoryDeleteDialogComponent],
  entryComponents: [ParseHistoryDeleteDialogComponent]
})
export class PokerParseHistoryModule {}
