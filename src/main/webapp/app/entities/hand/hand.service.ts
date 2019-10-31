import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IHand } from 'app/shared/model/hand.model';

type EntityResponseType = HttpResponse<IHand>;
type EntityArrayResponseType = HttpResponse<IHand[]>;

@Injectable({ providedIn: 'root' })
export class HandService {
  public resourceUrl = SERVER_API_URL + 'api/hands';

  constructor(protected http: HttpClient) {}

  create(hand: IHand): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(hand);
    return this.http
      .post<IHand>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(hand: IHand): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(hand);
    return this.http
      .put<IHand>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IHand>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IHand[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(hand: IHand): IHand {
    const copy: IHand = Object.assign({}, hand, {
      startDate: hand.startDate != null && hand.startDate.isValid() ? hand.startDate.toJSON() : null
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
      res.body.forEach((hand: IHand) => {
        hand.startDate = hand.startDate != null ? moment(hand.startDate) : null;
      });
    }
    return res;
  }
}
