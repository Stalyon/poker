import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PokerSharedModule } from 'app/shared/shared.module';
import { ShowDownComponent } from './show-down.component';
import { ShowDownDetailComponent } from './show-down-detail.component';
import { ShowDownUpdateComponent } from './show-down-update.component';
import { ShowDownDeleteDialogComponent } from './show-down-delete-dialog.component';
import { showDownRoute } from './show-down.route';

@NgModule({
  imports: [PokerSharedModule, RouterModule.forChild(showDownRoute)],
  declarations: [ShowDownComponent, ShowDownDetailComponent, ShowDownUpdateComponent, ShowDownDeleteDialogComponent],
  entryComponents: [ShowDownDeleteDialogComponent]
})
export class PokerShowDownModule {}
