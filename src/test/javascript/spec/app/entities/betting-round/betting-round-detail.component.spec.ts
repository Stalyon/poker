import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PokerTestModule } from '../../../test.module';
import { BettingRoundDetailComponent } from 'app/entities/betting-round/betting-round-detail.component';
import { BettingRound } from 'app/shared/model/betting-round.model';

describe('Component Tests', () => {
  describe('BettingRound Management Detail Component', () => {
    let comp: BettingRoundDetailComponent;
    let fixture: ComponentFixture<BettingRoundDetailComponent>;
    const route = ({ data: of({ bettingRound: new BettingRound(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PokerTestModule],
        declarations: [BettingRoundDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(BettingRoundDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(BettingRoundDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.bettingRound).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
