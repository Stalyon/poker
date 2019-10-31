import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IParseHistory } from 'app/shared/model/parse-history.model';

type EntityResponseType = HttpResponse<IParseHistory>;
type EntityArrayResponseType = HttpResponse<IParseHistory[]>;

@Injectable({ providedIn: 'root' })
export class ParseHistoryService {
  public resourceUrl = SERVER_API_URL + 'api/parse-histories';

  constructor(protected http: HttpClient) {}

  create(parseHistory: IParseHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(parseHistory);
    return this.http
      .post<IParseHistory>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(parseHistory: IParseHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(parseHistory);
    return this.http
      .put<IParseHistory>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IParseHistory>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IParseHistory[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(parseHistory: IParseHistory): IParseHistory {
    const copy: IParseHistory = Object.assign({}, parseHistory, {
      parsedDate: parseHistory.parsedDate != null && parseHistory.parsedDate.isValid() ? parseHistory.parsedDate.toJSON() : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.parsedDate = res.body.parsedDate != null ? moment(res.body.parsedDate) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((parseHistory: IParseHistory) => {
        parseHistory.parsedDate = parseHistory.parsedDate != null ? moment(parseHistory.parsedDate) : null;
      });
    }
    return res;
  }
}
