import { Moment } from 'moment';
import { IGame } from 'app/shared/model/game.model';

export interface IParseHistory {
  id?: number;
  fileName?: string;
  fileSize?: number;
  parsedDate?: Moment;
  game?: IGame;
}

export class ParseHistory implements IParseHistory {
  constructor(public id?: number, public fileName?: string, public fileSize?: number, public parsedDate?: Moment, public game?: IGame) {}
}
