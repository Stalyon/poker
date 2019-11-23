import {Injectable} from "@angular/core";
import {Observable } from "rxjs/index";
import {HttpClient, HttpResponse} from "@angular/common/http";
import {SERVER_API_URL} from "app/app.constants";
import {Stats} from "app/shared/model/stats.model";
import {RequestStats} from "app/shared/model/request-stats.model";

@Injectable({ providedIn: 'root' })
export class StatsService {
  public resourceUrl = SERVER_API_URL + 'api/stats';

  constructor(private http: HttpClient) {}

  getStats(requestStats: RequestStats): Observable<HttpResponse<Stats>> {
    return this.http.post<Stats>(this.resourceUrl, requestStats,{ observe: 'response' });
  }
}
