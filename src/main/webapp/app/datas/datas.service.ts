import {Injectable} from "@angular/core";
import {HttpClient, HttpResponse} from "@angular/common/http";
import {Observable} from "rxjs/index";
import {SERVER_API_URL} from "app/app.constants";

@Injectable({ providedIn: 'root' })
export class DatasService {
  public resourceUrl = SERVER_API_URL + 'api/datas';

  constructor(protected http: HttpClient) {}

  updateDatas(): Observable<HttpResponse<void>> {
    return this.http.get<void>(`${this.resourceUrl}`, { observe: 'response' });
  }
}
