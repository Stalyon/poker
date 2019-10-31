import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { PokerTestModule } from '../../../test.module';
import { ParseHistoryUpdateComponent } from 'app/entities/parse-history/parse-history-update.component';
import { ParseHistoryService } from 'app/entities/parse-history/parse-history.service';
import { ParseHistory } from 'app/shared/model/parse-history.model';

describe('Component Tests', () => {
  describe('ParseHistory Management Update Component', () => {
    let comp: ParseHistoryUpdateComponent;
    let fixture: ComponentFixture<ParseHistoryUpdateComponent>;
    let service: ParseHistoryService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PokerTestModule],
        declarations: [ParseHistoryUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ParseHistoryUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ParseHistoryUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ParseHistoryService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ParseHistory(123);
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
        const entity = new ParseHistory();
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
