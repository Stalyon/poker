import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PokerSharedModule } from 'app/shared/shared.module';
import { HandComponent } from './hand.component';
import { HandDetailComponent } from './hand-detail.component';
import { HandUpdateComponent } from './hand-update.component';
import { HandDeleteDialogComponent } from './hand-delete-dialog.component';
import { handRoute } from './hand.route';

@NgModule({
  imports: [PokerSharedModule, RouterModule.forChild(handRoute)],
  declarations: [HandComponent, HandDetailComponent, HandUpdateComponent, HandDeleteDialogComponent],
  entryComponents: [HandDeleteDialogComponent]
})
export class PokerHandModule {}
