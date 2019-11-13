import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { PokerTestModule } from '../../../test.module';
import { SitAndGoUpdateComponent } from 'app/entities/sit-and-go/sit-and-go-update.component';
import { SitAndGoService } from 'app/entities/sit-and-go/sit-and-go.service';
import { SitAndGo } from 'app/shared/model/sit-and-go.model';

describe('Component Tests', () => {
  describe('SitAndGo Management Update Component', () => {
    let comp: SitAndGoUpdateComponent;
    let fixture: ComponentFixture<SitAndGoUpdateComponent>;
    let service: SitAndGoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PokerTestModule],
        declarations: [SitAndGoUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(SitAndGoUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SitAndGoUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(SitAndGoService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new SitAndGo(123);
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
        const entity = new SitAndGo();
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
