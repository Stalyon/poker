import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { PokerTestModule } from '../../../test.module';
import { PlayerHandUpdateComponent } from 'app/entities/player-hand/player-hand-update.component';
import { PlayerHandService } from 'app/entities/player-hand/player-hand.service';
import { PlayerHand } from 'app/shared/model/player-hand.model';

describe('Component Tests', () => {
  describe('PlayerHand Management Update Component', () => {
    let comp: PlayerHandUpdateComponent;
    let fixture: ComponentFixture<PlayerHandUpdateComponent>;
    let service: PlayerHandService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PokerTestModule],
        declarations: [PlayerHandUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(PlayerHandUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PlayerHandUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PlayerHandService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new PlayerHand(123);
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
        const entity = new PlayerHand();
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
