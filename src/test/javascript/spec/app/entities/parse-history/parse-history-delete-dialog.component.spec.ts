import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { PokerTestModule } from '../../../test.module';
import { ParseHistoryDeleteDialogComponent } from 'app/entities/parse-history/parse-history-delete-dialog.component';
import { ParseHistoryService } from 'app/entities/parse-history/parse-history.service';

describe('Component Tests', () => {
  describe('ParseHistory Management Delete Component', () => {
    let comp: ParseHistoryDeleteDialogComponent;
    let fixture: ComponentFixture<ParseHistoryDeleteDialogComponent>;
    let service: ParseHistoryService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PokerTestModule],
        declarations: [ParseHistoryDeleteDialogComponent]
      })
        .overrideTemplate(ParseHistoryDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ParseHistoryDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ParseHistoryService);
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
