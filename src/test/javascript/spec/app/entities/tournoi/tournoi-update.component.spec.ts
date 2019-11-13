import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { PokerTestModule } from '../../../test.module';
import { TournoiUpdateComponent } from 'app/entities/tournoi/tournoi-update.component';
import { TournoiService } from 'app/entities/tournoi/tournoi.service';
import { Tournoi } from 'app/shared/model/tournoi.model';

describe('Component Tests', () => {
  describe('Tournoi Management Update Component', () => {
    let comp: TournoiUpdateComponent;
    let fixture: ComponentFixture<TournoiUpdateComponent>;
    let service: TournoiService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PokerTestModule],
        declarations: [TournoiUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(TournoiUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TournoiUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TournoiService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Tournoi(123);
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
        const entity = new Tournoi();
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
