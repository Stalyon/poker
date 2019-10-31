import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { PokerTestModule } from '../../../test.module';
import { ShowDownDeleteDialogComponent } from 'app/entities/show-down/show-down-delete-dialog.component';
import { ShowDownService } from 'app/entities/show-down/show-down.service';

describe('Component Tests', () => {
  describe('ShowDown Management Delete Component', () => {
    let comp: ShowDownDeleteDialogComponent;
    let fixture: ComponentFixture<ShowDownDeleteDialogComponent>;
    let service: ShowDownService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PokerTestModule],
        declarations: [ShowDownDeleteDialogComponent]
      })
        .overrideTemplate(ShowDownDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ShowDownDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ShowDownService);
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
