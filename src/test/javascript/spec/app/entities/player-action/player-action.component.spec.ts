import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { PokerTestModule } from '../../../test.module';
import { PlayerActionComponent } from 'app/entities/player-action/player-action.component';
import { PlayerActionService } from 'app/entities/player-action/player-action.service';
import { PlayerAction } from 'app/shared/model/player-action.model';

describe('Component Tests', () => {
  describe('PlayerAction Management Component', () => {
    let comp: PlayerActionComponent;
    let fixture: ComponentFixture<PlayerActionComponent>;
    let service: PlayerActionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PokerTestModule],
        declarations: [PlayerActionComponent],
        providers: []
      })
        .overrideTemplate(PlayerActionComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PlayerActionComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PlayerActionService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new PlayerAction(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.playerActions[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
