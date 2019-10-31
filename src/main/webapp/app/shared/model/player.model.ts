import { Moment } from 'moment';

export interface IPlayer {
  id?: number;
  name?: string;
  addedDate?: Moment;
  isMe?: boolean;
}

export class Player implements IPlayer {
  constructor(public id?: number, public name?: string, public addedDate?: Moment, public isMe?: boolean) {
    this.isMe = this.isMe || false;
  }
}
