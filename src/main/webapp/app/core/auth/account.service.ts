import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, Subject } from 'rxjs';
import { shareReplay, tap } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { Account } from 'app/core/user/account.model';
import { JhiTrackerService } from '../tracker/tracker.service';
import {LiveService} from "app/live/live.service";

@Injectable({ providedIn: 'root' })
export class AccountService {
  private userIdentity: Account;
  private authenticated = false;
  private authenticationState = new Subject<any>();
  private accountCache$: Observable<Account>;

  constructor(private http: HttpClient, private trackerService: JhiTrackerService, private liveService: LiveService) {}

  fetch(): Observable<Account> {
    return this.http.get<Account>(SERVER_API_URL + 'api/account');
  }

  save(account: Account): Observable<Account> {
    return this.http.post<Account>(SERVER_API_URL + 'api/account', account);
  }

  authenticate(identity) {
    this.userIdentity = identity;
    this.authenticated = identity !== null;
    this.authenticationState.next(this.userIdentity);
  }

  hasAnyAuthority(authorities: string[] | string): boolean {
    if (!this.authenticated || !this.userIdentity || !this.userIdentity.authorities) {
      return false;
    }

    if (!Array.isArray(authorities)) {
      authorities = [authorities];
    }

    return authorities.some((authority: string) => this.userIdentity.authorities.includes(authority));
  }

  identity(force?: boolean): Observable<Account> {
    if (force) {
      this.accountCache$ = null;
    }

    if (!this.accountCache$) {
      this.accountCache$ = this.fetch().pipe(
        tap(
          account => {
            if (account) {
              this.userIdentity = account;
              this.authenticated = true;
              this.trackerService.connect();
              this.liveService.connect();
            } else {
              this.userIdentity = null;
              this.authenticated = false;
            }
            this.authenticationState.next(this.userIdentity);
          },
          () => {
            if (this.trackerService.stompClient && this.trackerService.stompClient.connected) {
              this.trackerService.disconnect();
            }
            if (this.liveService.stompClient && this.liveService.stompClient.connected) {
              this.liveService.disconnect();
            }
            this.userIdentity = null;
            this.authenticated = false;
            this.authenticationState.next(this.userIdentity);
          }
        ),
        shareReplay()
      );
    }
    return this.accountCache$;
  }

  isAuthenticated(): boolean {
    return this.authenticated;
  }

  isIdentityResolved(): boolean {
    return this.userIdentity !== undefined;
  }

  getAuthenticationState(): Observable<any> {
    return this.authenticationState.asObservable();
  }

  getImageUrl(): string {
    return this.isIdentityResolved() ? this.userIdentity.imageUrl : null;
  }
}
