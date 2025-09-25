import { TestBed } from '@angular/core/testing';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';
import { MovementService } from './movement.service';
import { Movement } from '../types/movement.type';
import { environment } from '../../environments/environment';

describe('MovementService', () => {
  let service: MovementService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    service = TestBed.inject(MovementService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should create service', () => {
    expect(service).toBeTruthy();
  });

  describe('getByNumberAccount', () => {
    it('should build correct URL with account number parameter', () => {
      const accountNumber = '12345678';
      const mockMovements: Movement[] = [
        {
          movementId: 1,
          date: '2023-01-01',
          type: 'DEPOSITO',
          value: 100,
          balance: 1100,
          numberAccount: '12345678',
        },
      ];

      service.getByNumberAccount(accountNumber).subscribe((movements) => {
        expect(movements).toEqual(mockMovements);
      });

      const expectedUrl = `${environment.apiUrlMovement}/movimientos?numberAccount=12345678`;
      const req = httpMock.expectOne(expectedUrl);
      expect(req.request.method).toBe('GET');
      req.flush(mockMovements);
    });

    it('should handle special characters in account number', () => {
      const accountNumber = '123-456-78';

      service.getByNumberAccount(accountNumber).subscribe();

      const expectedUrl = `${environment.apiUrlMovement}/movimientos?numberAccount=123-456-78`;
      const req = httpMock.expectOne(expectedUrl);
      expect(req.request.method).toBe('GET');
      req.flush([]);
    });

    it('should retry on failure and handle errors', () => {
      const accountNumber = '12345678';

      service.getByNumberAccount(accountNumber).subscribe({
        next: () => expect(true).toBe(false),
        error: (error) => {
          expect(error).toBe('No se pudo conectar con el servidor');
        },
      });

      const expectedUrl = `${environment.apiUrlMovement}/movimientos?numberAccount=12345678`;

      const req1 = httpMock.expectOne(expectedUrl);
      req1.flush('Server Error', {
        status: 500,
        statusText: 'Internal Server Error',
      });

      const req2 = httpMock.expectOne(expectedUrl);
      req2.flush('Server Error', {
        status: 500,
        statusText: 'Internal Server Error',
      });

      const req3 = httpMock.expectOne(expectedUrl);
      req3.flush('Server Error', {
        status: 500,
        statusText: 'Internal Server Error',
      });
    });
  });

  describe('create', () => {
    it('should send movement data without calculated fields', () => {
      const movementData = {
        type: 'DEPOSITO',
        value: 100,
        numberAccount: '12345678',
      };

      const mockResponse: Movement = {
        movementId: 1,
        date: '2023-01-01',
        type: 'DEPOSITO',
        value: 100,
        balance: 1100,
        numberAccount: '12345678',
      };

      service.create(movementData).subscribe((movement) => {
        expect(movement).toEqual(mockResponse);
      });

      const req = httpMock.expectOne(
        `${environment.apiUrlMovement}/movimientos`,
      );
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual(movementData);
      req.flush(mockResponse);
    });
  });
});
