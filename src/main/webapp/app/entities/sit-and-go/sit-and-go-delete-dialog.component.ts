import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISitAndGo } from 'app/shared/model/sit-and-go.model';
import { SitAndGoService } from './sit-and-go.service';

@Component({
  selector: 'jhi-sit-and-go-delete-dialog',
  templateUrl: './sit-and-go-delete-dialog.component.html'
})
export class SitAndGoDeleteDialogComponent {
  sitAndGo: ISitAndGo;

  constructor(protected sitAndGoService: SitAndGoService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.sitAndGoService.delete(id).subscribe(() => {
      this.eventManager.broadcast({
        name: 'sitAndGoListModification',
        content: 'Deleted an sitAndGo'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-sit-and-go-delete-popup',
  template: ''
})
export class SitAndGoDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ sitAndGo }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(SitAndGoDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.sitAndGo = sitAndGo;
        this.ngbModalRef.result.then(
          () => {
            this.router.navigate(['/sit-and-go', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          () => {
            this.router.navigate(['/sit-and-go', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          }
        );
      }, 0);
    });
  }

  ngOnDestroy() {
    this.ngbModalRef = null;
  }
}
