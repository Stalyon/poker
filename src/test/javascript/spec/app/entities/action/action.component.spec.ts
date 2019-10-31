import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { PokerTestModule } from '../../../test.module';
import { ActionComponent } from 'app/entities/action/action.component';
import { ActionService } from 'app/entities/action/action.service';
import { Action } from 'app/shared/model/action.model';

describe('Component Tests', () => {
  describe('Action Management Component', () => {
    let comp: ActionComponent;
    let fixture: ComponentFixture<ActionComponent>;
    let service: ActionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PokerTestModule],
        declarations: [ActionComponent],
        providers: []
      })
        .overrideTemplate(ActionComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ActionComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ActionService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Action(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.actions[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
