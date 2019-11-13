import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { PokerTestModule } from '../../../test.module';
import { SitAndGoDeleteDialogComponent } from 'app/entities/sit-and-go/sit-and-go-delete-dialog.component';
import { SitAndGoService } from 'app/entities/sit-and-go/sit-and-go.service';

describe('Component Tests', () => {
  describe('SitAndGo Management Delete Component', () => {
    let comp: SitAndGoDeleteDialogComponent;
    let fixture: ComponentFixture<SitAndGoDeleteDialogComponent>;
    let service: SitAndGoService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PokerTestModule],
        declarations: [SitAndGoDeleteDialogComponent]
      })
        .overrideTemplate(SitAndGoDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SitAndGoDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(SitAndGoService);
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
