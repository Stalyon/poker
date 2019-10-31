import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PokerSharedModule } from 'app/shared/shared.module';
import { HandComponent } from './hand.component';
import { HandDetailComponent } from './hand-detail.component';
import { HandUpdateComponent } from './hand-update.component';
import { HandDeletePopupComponent, HandDeleteDialogComponent } from './hand-delete-dialog.component';
import { handRoute, handPopupRoute } from './hand.route';

const ENTITY_STATES = [...handRoute, ...handPopupRoute];

@NgModule({
  imports: [PokerSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [HandComponent, HandDetailComponent, HandUpdateComponent, HandDeleteDialogComponent, HandDeletePopupComponent],
  entryComponents: [HandDeleteDialogComponent]
})
export class PokerHandModule {}
