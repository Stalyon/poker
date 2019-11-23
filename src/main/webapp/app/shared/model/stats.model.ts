export interface IStats {
  chartOption?: any;
}

export class Stats implements IStats {
  constructor(
    public chartOption?: any
  ) {}
}
