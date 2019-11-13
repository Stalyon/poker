import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ITournoi } from 'app/shared/model/tournoi.model';

type EntityResponseType = HttpResponse<ITournoi>;
type EntityArrayResponseType = HttpResponse<ITournoi[]>;

@Injectable({ providedIn: 'root' })
export class TournoiService {
  public resourceUrl = SERVER_API_URL + 'api/tournois';

  constructor(protected http: HttpClient) {}

  create(tournoi: ITournoi): Observable<EntityResponseType> {
    return this.http.post<ITournoi>(this.resourceUrl, tournoi, { observe: 'response' });
  }

  update(tournoi: ITournoi): Observable<EntityResponseType> {
    return this.http.put<ITournoi>(this.resourceUrl, tournoi, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITournoi>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITournoi[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
