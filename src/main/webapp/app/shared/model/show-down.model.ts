import { IHand } from 'app/shared/model/hand.model';
import { IPlayer } from 'app/shared/model/player.model';

export interface IShowDown {
  id?: number;
  cards?: string;
  wins?: boolean;
  hand?: IHand;
  player?: IPlayer;
}

export class ShowDown implements IShowDown {
  constructor(public id?: number, public cards?: string, public wins?: boolean, public hand?: IHand, public player?: IPlayer) {
    this.wins = this.wins || false;
  }
}
