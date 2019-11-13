import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { PokerTestModule } from '../../../test.module';
import { CashGameComponent } from 'app/entities/cash-game/cash-game.component';
import { CashGameService } from 'app/entities/cash-game/cash-game.service';
import { CashGame } from 'app/shared/model/cash-game.model';

describe('Component Tests', () => {
  describe('CashGame Management Component', () => {
    let comp: CashGameComponent;
    let fixture: ComponentFixture<CashGameComponent>;
    let service: CashGameService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PokerTestModule],
        declarations: [CashGameComponent],
        providers: []
      })
        .overrideTemplate(CashGameComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CashGameComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CashGameService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new CashGame(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.cashGames[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
