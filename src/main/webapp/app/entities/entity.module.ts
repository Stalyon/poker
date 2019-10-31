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
        path: 'betting-round',
        loadChildren: () => import('./betting-round/betting-round.module').then(m => m.PokerBettingRoundModule)
      },
      {
        path: 'action',
        loadChildren: () => import('./action/action.module').then(m => m.PokerActionModule)
      },
      {
        path: 'show-down',
        loadChildren: () => import('./show-down/show-down.module').then(m => m.PokerShowDownModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class PokerEntityModule {}
