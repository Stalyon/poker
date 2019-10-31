import { Moment } from 'moment';
import { IPlayer } from 'app/shared/model/player.model';

export interface IGame {
  id?: number;
  name?: string;
  startDate?: Moment;
  endDate?: Moment;
  player?: IPlayer;
}

export class Game implements IGame {
  constructor(public id?: number, public name?: string, public startDate?: Moment, public endDate?: Moment, public player?: IPlayer) {}
}
