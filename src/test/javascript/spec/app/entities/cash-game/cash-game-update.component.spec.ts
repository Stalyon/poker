import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { PokerTestModule } from '../../../test.module';
import { CashGameUpdateComponent } from 'app/entities/cash-game/cash-game-update.component';
import { CashGameService } from 'app/entities/cash-game/cash-game.service';
import { CashGame } from 'app/shared/model/cash-game.model';

describe('Component Tests', () => {
  describe('CashGame Management Update Component', () => {
    let comp: CashGameUpdateComponent;
    let fixture: ComponentFixture<CashGameUpdateComponent>;
    let service: CashGameService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PokerTestModule],
        declarations: [CashGameUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(CashGameUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CashGameUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CashGameService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new CashGame(123);
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
        const entity = new CashGame();
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
