import { IPlayer } from 'app/shared/model/player.model';
import { IGame } from 'app/shared/model/game.model';
import { IHand } from 'app/shared/model/hand.model';
import { BettingRound } from 'app/shared/model/enumerations/betting-round.model';
import { Action } from 'app/shared/model/enumerations/action.model';

export interface IPlayerAction {
  id?: number;
  amount?: number;
  bettingRound?: BettingRound;
  action?: Action;
  player?: IPlayer;
  game?: IGame;
  hand?: IHand;
}

export class PlayerAction implements IPlayerAction {
  constructor(
    public id?: number,
    public amount?: number,
    public bettingRound?: BettingRound,
    public action?: Action,
    public player?: IPlayer,
    public game?: IGame,
    public hand?: IHand
  ) {}
}
