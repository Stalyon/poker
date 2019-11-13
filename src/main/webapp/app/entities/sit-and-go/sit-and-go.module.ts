import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PokerSharedModule } from 'app/shared/shared.module';
import { SitAndGoComponent } from './sit-and-go.component';
import { SitAndGoDetailComponent } from './sit-and-go-detail.component';
import { SitAndGoUpdateComponent } from './sit-and-go-update.component';
import { SitAndGoDeletePopupComponent, SitAndGoDeleteDialogComponent } from './sit-and-go-delete-dialog.component';
import { sitAndGoRoute, sitAndGoPopupRoute } from './sit-and-go.route';

const ENTITY_STATES = [...sitAndGoRoute, ...sitAndGoPopupRoute];

@NgModule({
  imports: [PokerSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    SitAndGoComponent,
    SitAndGoDetailComponent,
    SitAndGoUpdateComponent,
    SitAndGoDeleteDialogComponent,
    SitAndGoDeletePopupComponent
  ],
  entryComponents: [SitAndGoDeleteDialogComponent]
})
export class PokerSitAndGoModule {}
