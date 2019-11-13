import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ITournoi } from 'app/shared/model/tournoi.model';
import { TournoiService } from './tournoi.service';

@Component({
  selector: 'jhi-tournoi-delete-dialog',
  templateUrl: './tournoi-delete-dialog.component.html'
})
export class TournoiDeleteDialogComponent {
  tournoi: ITournoi;

  constructor(protected tournoiService: TournoiService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.tournoiService.delete(id).subscribe(() => {
      this.eventManager.broadcast({
        name: 'tournoiListModification',
        content: 'Deleted an tournoi'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-tournoi-delete-popup',
  template: ''
})
export class TournoiDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ tournoi }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(TournoiDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.tournoi = tournoi;
        this.ngbModalRef.result.then(
          () => {
            this.router.navigate(['/tournoi', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          () => {
            this.router.navigate(['/tournoi', { outlets: { popup: null } }]);
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
