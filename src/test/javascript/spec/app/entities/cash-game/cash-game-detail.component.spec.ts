import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PokerTestModule } from '../../../test.module';
import { CashGameDetailComponent } from 'app/entities/cash-game/cash-game-detail.component';
import { CashGame } from 'app/shared/model/cash-game.model';

describe('Component Tests', () => {
  describe('CashGame Management Detail Component', () => {
    let comp: CashGameDetailComponent;
    let fixture: ComponentFixture<CashGameDetailComponent>;
    const route = ({ data: of({ cashGame: new CashGame(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PokerTestModule],
        declarations: [CashGameDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(CashGameDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CashGameDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.cashGame).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
