import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IGameHistory } from 'app/shared/model/game-history.model';

type EntityResponseType = HttpResponse<IGameHistory>;
type EntityArrayResponseType = HttpResponse<IGameHistory[]>;

@Injectable({ providedIn: 'root' })
export class GameHistoryService {
  public resourceUrl = SERVER_API_URL + 'api/game-histories';

  constructor(protected http: HttpClient) {}

  create(gameHistory: IGameHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(gameHistory);
    return this.http
      .post<IGameHistory>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(gameHistory: IGameHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(gameHistory);
    return this.http
      .put<IGameHistory>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IGameHistory>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IGameHistory[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(gameHistory: IGameHistory): IGameHistory {
    const copy: IGameHistory = Object.assign({}, gameHistory, {
      startDate: gameHistory.startDate != null && gameHistory.startDate.isValid() ? gameHistory.startDate.toJSON() : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.startDate = res.body.startDate != null ? moment(res.body.startDate) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((gameHistory: IGameHistory) => {
        gameHistory.startDate = gameHistory.startDate != null ? moment(gameHistory.startDate) : null;
      });
    }
    return res;
  }
}
