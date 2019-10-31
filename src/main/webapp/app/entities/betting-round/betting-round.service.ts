import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IBettingRound } from 'app/shared/model/betting-round.model';

type EntityResponseType = HttpResponse<IBettingRound>;
type EntityArrayResponseType = HttpResponse<IBettingRound[]>;

@Injectable({ providedIn: 'root' })
export class BettingRoundService {
  public resourceUrl = SERVER_API_URL + 'api/betting-rounds';

  constructor(protected http: HttpClient) {}

  create(bettingRound: IBettingRound): Observable<EntityResponseType> {
    return this.http.post<IBettingRound>(this.resourceUrl, bettingRound, { observe: 'response' });
  }

  update(bettingRound: IBettingRound): Observable<EntityResponseType> {
    return this.http.put<IBettingRound>(this.resourceUrl, bettingRound, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBettingRound>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBettingRound[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
