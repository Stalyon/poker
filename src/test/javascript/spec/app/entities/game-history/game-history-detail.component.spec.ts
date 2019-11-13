import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PokerTestModule } from '../../../test.module';
import { GameHistoryDetailComponent } from 'app/entities/game-history/game-history-detail.component';
import { GameHistory } from 'app/shared/model/game-history.model';

describe('Component Tests', () => {
  describe('GameHistory Management Detail Component', () => {
    let comp: GameHistoryDetailComponent;
    let fixture: ComponentFixture<GameHistoryDetailComponent>;
    const route = ({ data: of({ gameHistory: new GameHistory(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PokerTestModule],
        declarations: [GameHistoryDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(GameHistoryDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(GameHistoryDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.gameHistory).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
