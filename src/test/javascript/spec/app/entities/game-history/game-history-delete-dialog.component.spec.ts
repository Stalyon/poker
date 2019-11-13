import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { PokerTestModule } from '../../../test.module';
import { GameHistoryDeleteDialogComponent } from 'app/entities/game-history/game-history-delete-dialog.component';
import { GameHistoryService } from 'app/entities/game-history/game-history.service';

describe('Component Tests', () => {
  describe('GameHistory Management Delete Component', () => {
    let comp: GameHistoryDeleteDialogComponent;
    let fixture: ComponentFixture<GameHistoryDeleteDialogComponent>;
    let service: GameHistoryService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PokerTestModule],
        declarations: [GameHistoryDeleteDialogComponent]
      })
        .overrideTemplate(GameHistoryDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(GameHistoryDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(GameHistoryService);
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
