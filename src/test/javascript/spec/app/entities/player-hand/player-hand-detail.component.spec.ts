import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PokerTestModule } from '../../../test.module';
import { PlayerHandDetailComponent } from 'app/entities/player-hand/player-hand-detail.component';
import { PlayerHand } from 'app/shared/model/player-hand.model';

describe('Component Tests', () => {
  describe('PlayerHand Management Detail Component', () => {
    let comp: PlayerHandDetailComponent;
    let fixture: ComponentFixture<PlayerHandDetailComponent>;
    const route = ({ data: of({ playerHand: new PlayerHand(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PokerTestModule],
        declarations: [PlayerHandDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(PlayerHandDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PlayerHandDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.playerHand).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
