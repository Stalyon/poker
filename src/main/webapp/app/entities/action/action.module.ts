import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PokerSharedModule } from 'app/shared/shared.module';
import { ActionComponent } from './action.component';
import { ActionDetailComponent } from './action-detail.component';
import { ActionUpdateComponent } from './action-update.component';
import { ActionDeletePopupComponent, ActionDeleteDialogComponent } from './action-delete-dialog.component';
import { actionRoute, actionPopupRoute } from './action.route';

const ENTITY_STATES = [...actionRoute, ...actionPopupRoute];

@NgModule({
  imports: [PokerSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [ActionComponent, ActionDetailComponent, ActionUpdateComponent, ActionDeleteDialogComponent, ActionDeletePopupComponent],
  entryComponents: [ActionDeleteDialogComponent]
})
export class PokerActionModule {}
