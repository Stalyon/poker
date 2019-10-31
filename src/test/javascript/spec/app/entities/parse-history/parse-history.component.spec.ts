import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { PokerTestModule } from '../../../test.module';
import { ParseHistoryComponent } from 'app/entities/parse-history/parse-history.component';
import { ParseHistoryService } from 'app/entities/parse-history/parse-history.service';
import { ParseHistory } from 'app/shared/model/parse-history.model';

describe('Component Tests', () => {
  describe('ParseHistory Management Component', () => {
    let comp: ParseHistoryComponent;
    let fixture: ComponentFixture<ParseHistoryComponent>;
    let service: ParseHistoryService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PokerTestModule],
        declarations: [ParseHistoryComponent],
        providers: []
      })
        .overrideTemplate(ParseHistoryComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ParseHistoryComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ParseHistoryService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new ParseHistory(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.parseHistories[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
