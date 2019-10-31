import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PokerTestModule } from '../../../test.module';
import { PlayerActionDetailComponent } from 'app/entities/player-action/player-action-detail.component';
import { PlayerAction } from 'app/shared/model/player-action.model';

describe('Component Tests', () => {
  describe('PlayerAction Management Detail Component', () => {
    let comp: PlayerActionDetailComponent;
    let fixture: ComponentFixture<PlayerActionDetailComponent>;
    const route = ({ data: of({ playerAction: new PlayerAction(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PokerTestModule],
        declarations: [PlayerActionDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(PlayerActionDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PlayerActionDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.playerAction).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
