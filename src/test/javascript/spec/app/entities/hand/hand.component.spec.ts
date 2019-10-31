import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { PokerTestModule } from '../../../test.module';
import { HandComponent } from 'app/entities/hand/hand.component';
import { HandService } from 'app/entities/hand/hand.service';
import { Hand } from 'app/shared/model/hand.model';

describe('Component Tests', () => {
  describe('Hand Management Component', () => {
    let comp: HandComponent;
    let fixture: ComponentFixture<HandComponent>;
    let service: HandService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PokerTestModule],
        declarations: [HandComponent],
        providers: []
      })
        .overrideTemplate(HandComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(HandComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(HandService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Hand(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.hands[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
