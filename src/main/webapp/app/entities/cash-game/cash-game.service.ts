import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ICashGame } from 'app/shared/model/cash-game.model';

type EntityResponseType = HttpResponse<ICashGame>;
type EntityArrayResponseType = HttpResponse<ICashGame[]>;

@Injectable({ providedIn: 'root' })
export class CashGameService {
  public resourceUrl = SERVER_API_URL + 'api/cash-games';

  constructor(protected http: HttpClient) {}

  create(cashGame: ICashGame): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cashGame);
    return this.http
      .post<ICashGame>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(cashGame: ICashGame): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cashGame);
    return this.http
      .put<ICashGame>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICashGame>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICashGame[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(cashGame: ICashGame): ICashGame {
    const copy: ICashGame = Object.assign({}, cashGame, {
      endDate: cashGame.endDate != null && cashGame.endDate.isValid() ? cashGame.endDate.toJSON() : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.endDate = res.body.endDate != null ? moment(res.body.endDate) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((cashGame: ICashGame) => {
        cashGame.endDate = cashGame.endDate != null ? moment(cashGame.endDate) : null;
      });
    }
    return res;
  }
}
