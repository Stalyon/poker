import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PokerTestModule } from '../../../test.module';
import { ShowDownDetailComponent } from 'app/entities/show-down/show-down-detail.component';
import { ShowDown } from 'app/shared/model/show-down.model';

describe('Component Tests', () => {
  describe('ShowDown Management Detail Component', () => {
    let comp: ShowDownDetailComponent;
    let fixture: ComponentFixture<ShowDownDetailComponent>;
    const route = ({ data: of({ showDown: new ShowDown(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PokerTestModule],
        declarations: [ShowDownDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ShowDownDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ShowDownDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.showDown).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
