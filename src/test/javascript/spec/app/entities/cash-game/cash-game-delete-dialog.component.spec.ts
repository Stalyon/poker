import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { PokerTestModule } from '../../../test.module';
import { CashGameDeleteDialogComponent } from 'app/entities/cash-game/cash-game-delete-dialog.component';
import { CashGameService } from 'app/entities/cash-game/cash-game.service';

describe('Component Tests', () => {
  describe('CashGame Management Delete Component', () => {
    let comp: CashGameDeleteDialogComponent;
    let fixture: ComponentFixture<CashGameDeleteDialogComponent>;
    let service: CashGameService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PokerTestModule],
        declarations: [CashGameDeleteDialogComponent]
      })
        .overrideTemplate(CashGameDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CashGameDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CashGameService);
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
