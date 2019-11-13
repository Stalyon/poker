import { Moment } from 'moment';
import { IGameHistory } from 'app/shared/model/game-history.model';

export interface ICashGame {
  id?: number;
  endDate?: Moment;
  profit?: number;
  table?: string;
  sbBb?: string;
  nbHands?: number;
  gameHistory?: IGameHistory;
}

export class CashGame implements ICashGame {
  constructor(
    public id?: number,
    public endDate?: Moment,
    public profit?: number,
    public table?: string,
    public sbBb?: string,
    public nbHands?: number,
    public gameHistory?: IGameHistory
  ) {}
}
