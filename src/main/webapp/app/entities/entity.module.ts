import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'parse-history',
        loadChildren: () => import('./parse-history/parse-history.module').then(m => m.PokerParseHistoryModule)
      },
      {
        path: 'game',
        loadChildren: () => import('./game/game.module').then(m => m.PokerGameModule)
      },
      {
        path: 'player',
        loadChildren: () => import('./player/player.module').then(m => m.PokerPlayerModule)
      },
      {
        path: 'hand',
        loadChildren: () => import('./hand/hand.module').then(m => m.PokerHandModule)
      },
      {
        path: 'player-action',
        loadChildren: () => import('./player-action/player-action.module').then(m => m.PokerPlayerActionModule)
      },
      {
        path: 'show-down',
        loadChildren: () => import('./show-down/show-down.module').then(m => m.PokerShowDownModule)
      },
      {
        path: 'tournoi',
        loadChildren: () => import('./tournoi/tournoi.module').then(m => m.PokerTournoiModule)
      },
      {
        path: 'sit-and-go',
        loadChildren: () => import('./sit-and-go/sit-and-go.module').then(m => m.PokerSitAndGoModule)
      },
      {
        path: 'cash-game',
        loadChildren: () => import('./cash-game/cash-game.module').then(m => m.PokerCashGameModule)
      },
      {
        path: 'game-history',
        loadChildren: () => import('./game-history/game-history.module').then(m => m.PokerGameHistoryModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class PokerEntityModule {}
