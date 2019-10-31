import { Moment } from 'moment';

export interface IParseHistory {
  id?: number;
  fileName?: string;
  fileSize?: number;
  parsedDate?: Moment;
}

export class ParseHistory implements IParseHistory {
  constructor(public id?: number, public fileName?: string, public fileSize?: number, public parsedDate?: Moment) {}
}
