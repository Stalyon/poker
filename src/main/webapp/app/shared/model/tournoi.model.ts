import { IGameHistory } from 'app/shared/model/game-history.model';

export interface ITournoi {
  id?: number;
  buyIn?: number;
  rebuy?: number;
  ranking?: number;
  profit?: number;
  bounty?: number;
  gameHistory?: IGameHistory;
}

export class Tournoi implements ITournoi {
  constructor(
    public id?: number,
    public buyIn?: number,
    public rebuy?: number,
    public ranking?: number,
    public profit?: number,
    public bounty?: number,
    public gameHistory?: IGameHistory
  ) {}
}
