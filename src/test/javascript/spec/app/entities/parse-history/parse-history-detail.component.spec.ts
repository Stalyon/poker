import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PokerTestModule } from '../../../test.module';
import { ParseHistoryDetailComponent } from 'app/entities/parse-history/parse-history-detail.component';
import { ParseHistory } from 'app/shared/model/parse-history.model';

describe('Component Tests', () => {
  describe('ParseHistory Management Detail Component', () => {
    let comp: ParseHistoryDetailComponent;
    let fixture: ComponentFixture<ParseHistoryDetailComponent>;
    const route = ({ data: of({ parseHistory: new ParseHistory(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PokerTestModule],
        declarations: [ParseHistoryDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ParseHistoryDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ParseHistoryDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.parseHistory).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
