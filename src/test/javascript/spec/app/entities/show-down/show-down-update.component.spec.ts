import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { PokerTestModule } from '../../../test.module';
import { ShowDownUpdateComponent } from 'app/entities/show-down/show-down-update.component';
import { ShowDownService } from 'app/entities/show-down/show-down.service';
import { ShowDown } from 'app/shared/model/show-down.model';

describe('Component Tests', () => {
  describe('ShowDown Management Update Component', () => {
    let comp: ShowDownUpdateComponent;
    let fixture: ComponentFixture<ShowDownUpdateComponent>;
    let service: ShowDownService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PokerTestModule],
        declarations: [ShowDownUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ShowDownUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ShowDownUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ShowDownService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ShowDown(123);
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
        const entity = new ShowDown();
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
