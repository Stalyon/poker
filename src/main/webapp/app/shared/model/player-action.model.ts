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
  callsPf?: boolean;
  raisesPf?: boolean;
  threeBetPf?: boolean;
  callsFlop?: boolean;
  betsFlop?: boolean;
  raisesFlop?: boolean;
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
    public callsPf?: boolean,
    public raisesPf?: boolean,
    public threeBetPf?: boolean,
    public callsFlop?: boolean,
    public betsFlop?: boolean,
    public raisesFlop?: boolean,
    public player?: IPlayer,
    public game?: IGame,
    public hand?: IHand
  ) {
    this.callsPf = this.callsPf || false;
    this.raisesPf = this.raisesPf || false;
    this.threeBetPf = this.threeBetPf || false;
    this.callsFlop = this.callsFlop || false;
    this.betsFlop = this.betsFlop || false;
    this.raisesFlop = this.raisesFlop || false;
  }
}
