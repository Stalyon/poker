import { Moment } from 'moment';

export interface IRequestStats {
  gameTypes?: string[];
  sitAndGoFormats?: string[];
  beforeDate?: Moment;
  afterDate?: Moment;
}

export class RequestStats implements IRequestStats {
  constructor(
    public gameTypes?: string[],
    public sitAndGoFormats?: string[],
    public beforeDate?: Moment,
    public afterDate?: Moment
  ) {}
}
