import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { PokerTestModule } from '../../../test.module';
import { HandUpdateComponent } from 'app/entities/hand/hand-update.component';
import { HandService } from 'app/entities/hand/hand.service';
import { Hand } from 'app/shared/model/hand.model';

describe('Component Tests', () => {
  describe('Hand Management Update Component', () => {
    let comp: HandUpdateComponent;
    let fixture: ComponentFixture<HandUpdateComponent>;
    let service: HandService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PokerTestModule],
        declarations: [HandUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(HandUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(HandUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(HandService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Hand(123);
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
        const entity = new Hand();
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
