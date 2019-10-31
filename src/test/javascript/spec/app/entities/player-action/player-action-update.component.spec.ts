import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { PokerTestModule } from '../../../test.module';
import { PlayerActionUpdateComponent } from 'app/entities/player-action/player-action-update.component';
import { PlayerActionService } from 'app/entities/player-action/player-action.service';
import { PlayerAction } from 'app/shared/model/player-action.model';

describe('Component Tests', () => {
  describe('PlayerAction Management Update Component', () => {
    let comp: PlayerActionUpdateComponent;
    let fixture: ComponentFixture<PlayerActionUpdateComponent>;
    let service: PlayerActionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PokerTestModule],
        declarations: [PlayerActionUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(PlayerActionUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PlayerActionUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PlayerActionService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new PlayerAction(123);
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
        const entity = new PlayerAction();
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
