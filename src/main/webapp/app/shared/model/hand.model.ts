import { Moment } from 'moment';
import { IGame } from 'app/shared/model/game.model';
import { IPlayer } from 'app/shared/model/player.model';

export interface IHand {
  id?: number;
  startDate?: Moment;
  buttonPosition?: number;
  myCards?: string;
  flopCards?: string;
  riverCards?: string;
  turnCards?: string;
  game?: IGame;
  winner?: IPlayer;
}

export class Hand implements IHand {
  constructor(
    public id?: number,
    public startDate?: Moment,
    public buttonPosition?: number,
    public myCards?: string,
    public flopCards?: string,
    public riverCards?: string,
    public turnCards?: string,
    public game?: IGame,
    public winner?: IPlayer
  ) {}
}
