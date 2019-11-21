import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IPlayerHand } from 'app/shared/model/player-hand.model';

type EntityResponseType = HttpResponse<IPlayerHand>;
type EntityArrayResponseType = HttpResponse<IPlayerHand[]>;

@Injectable({ providedIn: 'root' })
export class PlayerHandService {
  public resourceUrl = SERVER_API_URL + 'api/player-hands';

  constructor(protected http: HttpClient) {}

  create(playerHand: IPlayerHand): Observable<EntityResponseType> {
    return this.http.post<IPlayerHand>(this.resourceUrl, playerHand, { observe: 'response' });
  }

  update(playerHand: IPlayerHand): Observable<EntityResponseType> {
    return this.http.put<IPlayerHand>(this.resourceUrl, playerHand, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPlayerHand>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPlayerHand[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
