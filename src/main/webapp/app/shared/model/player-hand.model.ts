import { IPlayer } from 'app/shared/model/player.model';
import { IHand } from 'app/shared/model/hand.model';

export interface IPlayerHand {
  id?: number;
  callsPf?: boolean;
  raisesPf?: boolean;
  threeBetPf?: boolean;
  callsFlop?: boolean;
  betsFlop?: boolean;
  raisesFlop?: boolean;
  player?: IPlayer;
  hand?: IHand;
}

export class PlayerHand implements IPlayerHand {
  constructor(
    public id?: number,
    public callsPf?: boolean,
    public raisesPf?: boolean,
    public threeBetPf?: boolean,
    public callsFlop?: boolean,
    public betsFlop?: boolean,
    public raisesFlop?: boolean,
    public player?: IPlayer,
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
