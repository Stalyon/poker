import {PlayerData} from "app/shared/model/player-data.model";

export interface ILiveEvent {
  gameName?: string;
  gameId?: number;
  players?: PlayerData[];
}

export class LiveEvent implements ILiveEvent {
  constructor(
    public gameName?: string,
    public gameId?: number,
    public players?: PlayerData[]
  ) {}
}
