import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { PokerTestModule } from '../../../test.module';
import { ShowDownComponent } from 'app/entities/show-down/show-down.component';
import { ShowDownService } from 'app/entities/show-down/show-down.service';
import { ShowDown } from 'app/shared/model/show-down.model';

describe('Component Tests', () => {
  describe('ShowDown Management Component', () => {
    let comp: ShowDownComponent;
    let fixture: ComponentFixture<ShowDownComponent>;
    let service: ShowDownService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PokerTestModule],
        declarations: [ShowDownComponent],
        providers: []
      })
        .overrideTemplate(ShowDownComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ShowDownComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ShowDownService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new ShowDown(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.showDowns[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
