export interface IPlayerData {
  name?: string;
  nbHands?: number;
  vPip?: number;
  pfr?: number;
  cBet?: number;
}

export class PlayerData implements IPlayerData {
  constructor(
    public name?: string,
    public nbHands?: number,
    public vPip?: number,
    public pfr?: number,
    public cBet?: number
  ) {}
}
