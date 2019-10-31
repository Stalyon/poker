export interface IAction {
  id?: number;
  description?: string;
}

export class Action implements IAction {
  constructor(public id?: number, public description?: string) {}
}
