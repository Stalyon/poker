import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { PokerTestModule } from '../../../test.module';
import { BettingRoundUpdateComponent } from 'app/entities/betting-round/betting-round-update.component';
import { BettingRoundService } from 'app/entities/betting-round/betting-round.service';
import { BettingRound } from 'app/shared/model/betting-round.model';

describe('Component Tests', () => {
  describe('BettingRound Management Update Component', () => {
    let comp: BettingRoundUpdateComponent;
    let fixture: ComponentFixture<BettingRoundUpdateComponent>;
    let service: BettingRoundService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PokerTestModule],
        declarations: [BettingRoundUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(BettingRoundUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BettingRoundUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(BettingRoundService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new BettingRound(123);
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
        const entity = new BettingRound();
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
