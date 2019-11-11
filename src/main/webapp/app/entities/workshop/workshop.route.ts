import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Workshop } from 'app/shared/model/workshop.model';
import { WorkshopService } from './workshop.service';
import { WorkshopComponent } from './workshop.component';
import { WorkshopDetailComponent } from './workshop-detail.component';
import { WorkshopUpdateComponent } from './workshop-update.component';
import { WorkshopDeletePopupComponent } from './workshop-delete-dialog.component';
import { IWorkshop } from 'app/shared/model/workshop.model';

@Injectable({ providedIn: 'root' })
export class WorkshopResolve implements Resolve<IWorkshop> {
  constructor(private service: WorkshopService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IWorkshop> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(map((workshop: HttpResponse<Workshop>) => workshop.body));
    }
    return of(new Workshop());
  }
}

export const workshopRoute: Routes = [
  {
    path: '',
    component: WorkshopComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'kranzenzoApp.workshop.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: WorkshopDetailComponent,
    resolve: {
      workshop: WorkshopResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'kranzenzoApp.workshop.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: WorkshopUpdateComponent,
    resolve: {
      workshop: WorkshopResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'kranzenzoApp.workshop.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: WorkshopUpdateComponent,
    resolve: {
      workshop: WorkshopResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'kranzenzoApp.workshop.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const workshopPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: WorkshopDeletePopupComponent,
    resolve: {
      workshop: WorkshopResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'kranzenzoApp.workshop.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
