import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { ParseHistory } from 'app/shared/model/parse-history.model';
import { ParseHistoryService } from './parse-history.service';
import { ParseHistoryComponent } from './parse-history.component';
import { ParseHistoryDetailComponent } from './parse-history-detail.component';
import { ParseHistoryUpdateComponent } from './parse-history-update.component';
import { IParseHistory } from 'app/shared/model/parse-history.model';

@Injectable({ providedIn: 'root' })
export class ParseHistoryResolve implements Resolve<IParseHistory> {
  constructor(private service: ParseHistoryService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IParseHistory> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(map((parseHistory: HttpResponse<ParseHistory>) => parseHistory.body));
    }
    return of(new ParseHistory());
  }
}

export const parseHistoryRoute: Routes = [
  {
    path: '',
    component: ParseHistoryComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ParseHistories'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ParseHistoryDetailComponent,
    resolve: {
      parseHistory: ParseHistoryResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ParseHistories'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ParseHistoryUpdateComponent,
    resolve: {
      parseHistory: ParseHistoryResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ParseHistories'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ParseHistoryUpdateComponent,
    resolve: {
      parseHistory: ParseHistoryResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ParseHistories'
    },
    canActivate: [UserRouteAccessService]
  }
];
