import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PokerTestModule } from '../../../test.module';
import { TournoiDetailComponent } from 'app/entities/tournoi/tournoi-detail.component';
import { Tournoi } from 'app/shared/model/tournoi.model';

describe('Component Tests', () => {
  describe('Tournoi Management Detail Component', () => {
    let comp: TournoiDetailComponent;
    let fixture: ComponentFixture<TournoiDetailComponent>;
    const route = ({ data: of({ tournoi: new Tournoi(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PokerTestModule],
        declarations: [TournoiDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(TournoiDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TournoiDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.tournoi).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
