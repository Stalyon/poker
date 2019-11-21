import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { PokerTestModule } from '../../../test.module';
import { PlayerHandDeleteDialogComponent } from 'app/entities/player-hand/player-hand-delete-dialog.component';
import { PlayerHandService } from 'app/entities/player-hand/player-hand.service';

describe('Component Tests', () => {
  describe('PlayerHand Management Delete Component', () => {
    let comp: PlayerHandDeleteDialogComponent;
    let fixture: ComponentFixture<PlayerHandDeleteDialogComponent>;
    let service: PlayerHandService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PokerTestModule],
        declarations: [PlayerHandDeleteDialogComponent]
      })
        .overrideTemplate(PlayerHandDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PlayerHandDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PlayerHandService);
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
