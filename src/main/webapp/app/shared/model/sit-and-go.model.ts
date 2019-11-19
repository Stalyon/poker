import { IGameHistory } from 'app/shared/model/game-history.model';
import { SitAndGoFormat } from 'app/shared/model/enumerations/sit-and-go-format.model';

export interface ISitAndGo {
  id?: number;
  format?: SitAndGoFormat;
  buyIn?: number;
  ranking?: number;
  profit?: number;
  bounty?: number;
  gameHistory?: IGameHistory;
}

export class SitAndGo implements ISitAndGo {
  constructor(
    public id?: number,
    public format?: SitAndGoFormat,
    public buyIn?: number,
    public ranking?: number,
    public profit?: number,
    public bounty?: number,
    public gameHistory?: IGameHistory
  ) {}
}
