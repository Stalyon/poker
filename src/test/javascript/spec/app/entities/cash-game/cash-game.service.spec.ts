import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { CashGameService } from 'app/entities/cash-game/cash-game.service';
import { ICashGame, CashGame } from 'app/shared/model/cash-game.model';

describe('Service Tests', () => {
  describe('CashGame Service', () => {
    let injector: TestBed;
    let service: CashGameService;
    let httpMock: HttpTestingController;
    let elemDefault: ICashGame;
    let expectedResult;
    let currentDate: moment.Moment;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = {};
      injector = getTestBed();
      service = injector.get(CashGameService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new CashGame(0, currentDate, 0, 'AAAAAAA', 'AAAAAAA', 0);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            endDate: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        service
          .find(123)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: elemDefault });
      });

      it('should create a CashGame', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            endDate: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            endDate: currentDate
          },
          returnedFromService
        );
        service
          .create(new CashGame(null))
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should update a CashGame', () => {
        const returnedFromService = Object.assign(
          {
            endDate: currentDate.format(DATE_TIME_FORMAT),
            profit: 1,
            table: 'BBBBBB',
            sbBb: 'BBBBBB',
            nbHands: 1
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            endDate: currentDate
          },
          returnedFromService
        );
        service
          .update(expected)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should return a list of CashGame', () => {
        const returnedFromService = Object.assign(
          {
            endDate: currentDate.format(DATE_TIME_FORMAT),
            profit: 1,
            table: 'BBBBBB',
            sbBb: 'BBBBBB',
            nbHands: 1
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            endDate: currentDate
          },
          returnedFromService
        );
        service
          .query(expected)
          .pipe(
            take(1),
            map(resp => resp.body)
          )
          .subscribe(body => (expectedResult = body));
        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a CashGame', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
