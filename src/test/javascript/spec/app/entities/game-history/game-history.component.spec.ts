import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { PokerTestModule } from '../../../test.module';
import { GameHistoryComponent } from 'app/entities/game-history/game-history.component';
import { GameHistoryService } from 'app/entities/game-history/game-history.service';
import { GameHistory } from 'app/shared/model/game-history.model';

describe('Component Tests', () => {
  describe('GameHistory Management Component', () => {
    let comp: GameHistoryComponent;
    let fixture: ComponentFixture<GameHistoryComponent>;
    let service: GameHistoryService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PokerTestModule],
        declarations: [GameHistoryComponent],
        providers: []
      })
        .overrideTemplate(GameHistoryComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(GameHistoryComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(GameHistoryService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new GameHistory(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.gameHistories[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
