import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { PokerTestModule } from '../../../test.module';
import { GameHistoryUpdateComponent } from 'app/entities/game-history/game-history-update.component';
import { GameHistoryService } from 'app/entities/game-history/game-history.service';
import { GameHistory } from 'app/shared/model/game-history.model';

describe('Component Tests', () => {
  describe('GameHistory Management Update Component', () => {
    let comp: GameHistoryUpdateComponent;
    let fixture: ComponentFixture<GameHistoryUpdateComponent>;
    let service: GameHistoryService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PokerTestModule],
        declarations: [GameHistoryUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(GameHistoryUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(GameHistoryUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(GameHistoryService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new GameHistory(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new GameHistory();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
