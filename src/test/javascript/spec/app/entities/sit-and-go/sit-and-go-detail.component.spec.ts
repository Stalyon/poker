import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PokerTestModule } from '../../../test.module';
import { SitAndGoDetailComponent } from 'app/entities/sit-and-go/sit-and-go-detail.component';
import { SitAndGo } from 'app/shared/model/sit-and-go.model';

describe('Component Tests', () => {
  describe('SitAndGo Management Detail Component', () => {
    let comp: SitAndGoDetailComponent;
    let fixture: ComponentFixture<SitAndGoDetailComponent>;
    const route = ({ data: of({ sitAndGo: new SitAndGo(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PokerTestModule],
        declarations: [SitAndGoDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(SitAndGoDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SitAndGoDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.sitAndGo).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
