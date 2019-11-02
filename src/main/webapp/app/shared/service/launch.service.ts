import {Injectable} from "@angular/core";
import {SERVER_API_URL} from "app/app.constants";
import {HttpClient, HttpResponse} from "@angular/common/http";
import {Observable} from "rxjs/index";

@Injectable({ providedIn: 'root' })
export class LaunchService {
  public resourceUrl = SERVER_API_URL + 'api/launch';

  constructor(protected http: HttpClient) {}

  proceed(): Observable<HttpResponse<any>> {
    return this.http.get<any>(`${this.resourceUrl}`, { observe: 'response' });
  }
}
