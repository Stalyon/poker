import {Injectable} from "@angular/core";
import {Observable} from "rxjs/index";
import {HttpClient, HttpResponse} from "@angular/common/http";
import {SERVER_API_URL} from "app/app.constants";
import {createRequestOption} from "app/shared/util/request-util";
import {PlayerData} from "app/shared/model/player-data.model";

@Injectable({ providedIn: 'root' })
export class PlayersDatasService {
  public resourceUrl = SERVER_API_URL + 'api/players-datas/search';

  constructor(private http: HttpClient) {}

  search(searchText: string): Observable<HttpResponse<PlayerData[]>> {
    const options = createRequestOption({searchText: searchText});
    return this.http.get<PlayerData[]>(this.resourceUrl, { params: options, observe: 'response' });
  }
}
