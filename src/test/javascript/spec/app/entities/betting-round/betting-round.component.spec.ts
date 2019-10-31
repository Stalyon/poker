import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { PokerTestModule } from '../../../test.module';
import { BettingRoundComponent } from 'app/entities/betting-round/betting-round.component';
import { BettingRoundService } from 'app/entities/betting-round/betting-round.service';
import { BettingRound } from 'app/shared/model/betting-round.model';

describe('Component Tests', () => {
  describe('BettingRound Management Component', () => {
    let comp: BettingRoundComponent;
    let fixture: ComponentFixture<BettingRoundComponent>;
    let service: BettingRoundService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PokerTestModule],
        declarations: [BettingRoundComponent],
        providers: []
      })
        .overrideTemplate(BettingRoundComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BettingRoundComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(BettingRoundService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new BettingRound(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.bettingRounds[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
