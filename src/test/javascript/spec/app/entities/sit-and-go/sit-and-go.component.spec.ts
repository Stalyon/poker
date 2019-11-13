import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { PokerTestModule } from '../../../test.module';
import { SitAndGoComponent } from 'app/entities/sit-and-go/sit-and-go.component';
import { SitAndGoService } from 'app/entities/sit-and-go/sit-and-go.service';
import { SitAndGo } from 'app/shared/model/sit-and-go.model';

describe('Component Tests', () => {
  describe('SitAndGo Management Component', () => {
    let comp: SitAndGoComponent;
    let fixture: ComponentFixture<SitAndGoComponent>;
    let service: SitAndGoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PokerTestModule],
        declarations: [SitAndGoComponent],
        providers: []
      })
        .overrideTemplate(SitAndGoComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SitAndGoComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(SitAndGoService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new SitAndGo(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.sitAndGos[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
