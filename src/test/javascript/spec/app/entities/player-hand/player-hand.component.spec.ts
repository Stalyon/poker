import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { PokerTestModule } from '../../../test.module';
import { PlayerHandComponent } from 'app/entities/player-hand/player-hand.component';
import { PlayerHandService } from 'app/entities/player-hand/player-hand.service';
import { PlayerHand } from 'app/shared/model/player-hand.model';

describe('Component Tests', () => {
  describe('PlayerHand Management Component', () => {
    let comp: PlayerHandComponent;
    let fixture: ComponentFixture<PlayerHandComponent>;
    let service: PlayerHandService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PokerTestModule],
        declarations: [PlayerHandComponent],
        providers: []
      })
        .overrideTemplate(PlayerHandComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PlayerHandComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PlayerHandService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new PlayerHand(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.playerHands[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
