export interface IPlayerData {
  name?: string;
  nbHands?: number;
  isMe?: boolean;
  vPip?: number;
  pfr?: number;
}

export class PlayerData implements IPlayerData {
  constructor(
    public name?: string,
    public nbHands?: number,
    public isMe?: boolean,
    public vPip?: number,
    public pfr?: number
  ) {}
}
