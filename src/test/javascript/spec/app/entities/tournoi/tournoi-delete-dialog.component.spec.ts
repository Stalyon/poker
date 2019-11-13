import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { PokerTestModule } from '../../../test.module';
import { TournoiDeleteDialogComponent } from 'app/entities/tournoi/tournoi-delete-dialog.component';
import { TournoiService } from 'app/entities/tournoi/tournoi.service';

describe('Component Tests', () => {
  describe('Tournoi Management Delete Component', () => {
    let comp: TournoiDeleteDialogComponent;
    let fixture: ComponentFixture<TournoiDeleteDialogComponent>;
    let service: TournoiService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PokerTestModule],
        declarations: [TournoiDeleteDialogComponent]
      })
        .overrideTemplate(TournoiDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TournoiDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TournoiService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));
    });
  });
});
