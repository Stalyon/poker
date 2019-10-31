import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PokerSharedModule } from 'app/shared/shared.module';
import { ShowDownComponent } from './show-down.component';
import { ShowDownDetailComponent } from './show-down-detail.component';
import { ShowDownUpdateComponent } from './show-down-update.component';
import { ShowDownDeletePopupComponent, ShowDownDeleteDialogComponent } from './show-down-delete-dialog.component';
import { showDownRoute, showDownPopupRoute } from './show-down.route';

const ENTITY_STATES = [...showDownRoute, ...showDownPopupRoute];

@NgModule({
  imports: [PokerSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    ShowDownComponent,
    ShowDownDetailComponent,
    ShowDownUpdateComponent,
    ShowDownDeleteDialogComponent,
    ShowDownDeletePopupComponent
  ],
  entryComponents: [ShowDownDeleteDialogComponent]
})
export class PokerShowDownModule {}
