import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { PokerSharedModule } from 'app/shared/shared.module';
import { PokerCoreModule } from 'app/core/core.module';
import { PokerAppRoutingModule } from './app-routing.module';
import { PokerHomeModule } from './home/home.module';
import { PokerEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { JhiMainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ErrorComponent } from './layouts/error/error.component';
import {PokerLiveModule} from "app/live/live.module";
import {PokerPlayersDatasModule} from "app/players-datas/players-datas.module";

@NgModule({
  imports: [
    BrowserModule,
    PokerSharedModule,
    PokerCoreModule,
    PokerHomeModule,
    PokerLiveModule,
    PokerPlayersDatasModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    PokerEntityModule,
    PokerAppRoutingModule
  ],
  declarations: [JhiMainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, FooterComponent],
  bootstrap: [JhiMainComponent]
})
export class PokerAppModule {}
