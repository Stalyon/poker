import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PokerSharedModule } from 'app/shared/shared.module';
import { TournoiComponent } from './tournoi.component';
import { TournoiDetailComponent } from './tournoi-detail.component';
import { TournoiUpdateComponent } from './tournoi-update.component';
import { TournoiDeletePopupComponent, TournoiDeleteDialogComponent } from './tournoi-delete-dialog.component';
import { tournoiRoute, tournoiPopupRoute } from './tournoi.route';

const ENTITY_STATES = [...tournoiRoute, ...tournoiPopupRoute];

@NgModule({
  imports: [PokerSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    TournoiComponent,
    TournoiDetailComponent,
    TournoiUpdateComponent,
    TournoiDeleteDialogComponent,
    TournoiDeletePopupComponent
  ],
  entryComponents: [TournoiDeleteDialogComponent]
})
export class PokerTournoiModule {}
