import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IShowDown } from 'app/shared/model/show-down.model';

type EntityResponseType = HttpResponse<IShowDown>;
type EntityArrayResponseType = HttpResponse<IShowDown[]>;

@Injectable({ providedIn: 'root' })
export class ShowDownService {
  public resourceUrl = SERVER_API_URL + 'api/show-downs';

  constructor(protected http: HttpClient) {}

  create(showDown: IShowDown): Observable<EntityResponseType> {
    return this.http.post<IShowDown>(this.resourceUrl, showDown, { observe: 'response' });
  }

  update(showDown: IShowDown): Observable<EntityResponseType> {
    return this.http.put<IShowDown>(this.resourceUrl, showDown, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IShowDown>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IShowDown[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
