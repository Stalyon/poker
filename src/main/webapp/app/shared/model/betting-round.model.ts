export interface IBettingRound {
  id?: number;
  description?: string;
}

export class BettingRound implements IBettingRound {
  constructor(public id?: number, public description?: string) {}
}
