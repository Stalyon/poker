import { Moment } from 'moment';
import { ISitAndGo } from 'app/shared/model/sit-and-go.model';
import { ITournoi } from 'app/shared/model/tournoi.model';
import { ICashGame } from 'app/shared/model/cash-game.model';
import { GameType } from 'app/shared/model/enumerations/game-type.model';

export interface IGameHistory {
  id?: number;
  startDate?: Moment;
  name?: string;
  type?: GameType;
  netResult?: number;
  sitAndGo?: ISitAndGo;
  tournoi?: ITournoi;
  cashGame?: ICashGame;
}

export class GameHistory implements IGameHistory {
  constructor(
    public id?: number,
    public startDate?: Moment,
    public name?: string,
    public type?: GameType,
    public netResult?: number,
    public sitAndGo?: ISitAndGo,
    public tournoi?: ITournoi,
    public cashGame?: ICashGame
  ) {}
}
