import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { PokerTestModule } from '../../../test.module';
import { TournoiComponent } from 'app/entities/tournoi/tournoi.component';
import { TournoiService } from 'app/entities/tournoi/tournoi.service';
import { Tournoi } from 'app/shared/model/tournoi.model';

describe('Component Tests', () => {
  describe('Tournoi Management Component', () => {
    let comp: TournoiComponent;
    let fixture: ComponentFixture<TournoiComponent>;
    let service: TournoiService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PokerTestModule],
        declarations: [TournoiComponent],
        providers: []
      })
        .overrideTemplate(TournoiComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TournoiComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TournoiService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Tournoi(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.tournois[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
