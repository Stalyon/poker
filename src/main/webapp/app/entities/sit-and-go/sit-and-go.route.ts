import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { SitAndGo } from 'app/shared/model/sit-and-go.model';
import { SitAndGoService } from './sit-and-go.service';
import { SitAndGoComponent } from './sit-and-go.component';
import { SitAndGoDetailComponent } from './sit-and-go-detail.component';
import { SitAndGoUpdateComponent } from './sit-and-go-update.component';
import { SitAndGoDeletePopupComponent } from './sit-and-go-delete-dialog.component';
import { ISitAndGo } from 'app/shared/model/sit-and-go.model';

@Injectable({ providedIn: 'root' })
export class SitAndGoResolve implements Resolve<ISitAndGo> {
  constructor(private service: SitAndGoService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISitAndGo> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(map((sitAndGo: HttpResponse<SitAndGo>) => sitAndGo.body));
    }
    return of(new SitAndGo());
  }
}

export const sitAndGoRoute: Routes = [
  {
    path: '',
    component: SitAndGoComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'SitAndGos'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: SitAndGoDetailComponent,
    resolve: {
      sitAndGo: SitAndGoResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'SitAndGos'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: SitAndGoUpdateComponent,
    resolve: {
      sitAndGo: SitAndGoResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'SitAndGos'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: SitAndGoUpdateComponent,
    resolve: {
      sitAndGo: SitAndGoResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'SitAndGos'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const sitAndGoPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: SitAndGoDeletePopupComponent,
    resolve: {
      sitAndGo: SitAndGoResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'SitAndGos'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
