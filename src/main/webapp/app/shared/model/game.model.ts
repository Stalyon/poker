import { Moment } from 'moment';
import { IPlayer } from 'app/shared/model/player.model';
import { IParseHistory } from 'app/shared/model/parse-history.model';

export interface IGame {
  id?: number;
  name?: string;
  startDate?: Moment;
  endDate?: Moment;
  player?: IPlayer;
  parseHistory?: IParseHistory;
}

export class Game implements IGame {
  constructor(
    public id?: number,
    public name?: string,
    public startDate?: Moment,
    public endDate?: Moment,
    public player?: IPlayer,
    public parseHistory?: IParseHistory
  ) {}
}
