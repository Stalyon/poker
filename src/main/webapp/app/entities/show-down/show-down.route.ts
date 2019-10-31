import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { ShowDown } from 'app/shared/model/show-down.model';
import { ShowDownService } from './show-down.service';
import { ShowDownComponent } from './show-down.component';
import { ShowDownDetailComponent } from './show-down-detail.component';
import { ShowDownUpdateComponent } from './show-down-update.component';
import { ShowDownDeletePopupComponent } from './show-down-delete-dialog.component';
import { IShowDown } from 'app/shared/model/show-down.model';

@Injectable({ providedIn: 'root' })
export class ShowDownResolve implements Resolve<IShowDown> {
  constructor(private service: ShowDownService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IShowDown> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<ShowDown>) => response.ok),
        map((showDown: HttpResponse<ShowDown>) => showDown.body)
      );
    }
    return of(new ShowDown());
  }
}

export const showDownRoute: Routes = [
  {
    path: '',
    component: ShowDownComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ShowDowns'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ShowDownDetailComponent,
    resolve: {
      showDown: ShowDownResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ShowDowns'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ShowDownUpdateComponent,
    resolve: {
      showDown: ShowDownResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ShowDowns'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ShowDownUpdateComponent,
    resolve: {
      showDown: ShowDownResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ShowDowns'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const showDownPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: ShowDownDeletePopupComponent,
    resolve: {
      showDown: ShowDownResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ShowDowns'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
