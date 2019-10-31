import { IPlayer } from 'app/shared/model/player.model';
import { IGame } from 'app/shared/model/game.model';
import { IHand } from 'app/shared/model/hand.model';
import { IAction } from 'app/shared/model/action.model';
import { IBettingRound } from 'app/shared/model/betting-round.model';

export interface IPlayerAction {
  id?: number;
  amount?: number;
  player?: IPlayer;
  game?: IGame;
  hand?: IHand;
  action?: IAction;
  bettingRound?: IBettingRound;
}

export class PlayerAction implements IPlayerAction {
  constructor(
    public id?: number,
    public amount?: number,
    public player?: IPlayer,
    public game?: IGame,
    public hand?: IHand,
    public action?: IAction,
    public bettingRound?: IBettingRound
  ) {}
}
