import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IPlayerAction } from 'app/shared/model/player-action.model';

type EntityResponseType = HttpResponse<IPlayerAction>;
type EntityArrayResponseType = HttpResponse<IPlayerAction[]>;

@Injectable({ providedIn: 'root' })
export class PlayerActionService {
  public resourceUrl = SERVER_API_URL + 'api/player-actions';

  constructor(protected http: HttpClient) {}

  create(playerAction: IPlayerAction): Observable<EntityResponseType> {
    return this.http.post<IPlayerAction>(this.resourceUrl, playerAction, { observe: 'response' });
  }

  update(playerAction: IPlayerAction): Observable<EntityResponseType> {
    return this.http.put<IPlayerAction>(this.resourceUrl, playerAction, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPlayerAction>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPlayerAction[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
