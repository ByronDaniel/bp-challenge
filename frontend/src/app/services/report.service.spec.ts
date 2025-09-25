import { TestBed } from '@angular/core/testing';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';
import { ReportService } from './report.service';
import { ReportFilter } from '../types/report.type';
import { ReportPdf } from '../types/report-pdf';
import { environment } from '../../environments/environment';

describe('ReportService', () => {
  let service: ReportService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    service = TestBed.inject(ReportService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should create service', () => {
    expect(service).toBeTruthy();
  });

  describe('generateReport', () => {
    it('should build correct URL with filter parameters', () => {
      const filter: ReportFilter = {
        startDate: '2023-01-01',
        endDate: '2023-01-31',
        clientIdentification: '12345678',
      };

      const mockResponse: ReportPdf = {
        pdf: 'base64content',
        report: [],
      };

      service.generateReport(filter).subscribe((response) => {
        expect(response).toEqual(mockResponse);
      });

      const expectedUrl = `${environment.apiUrlReport}/reporte?date=2023-01-01,2023-01-31&clientIdentification=12345678`;
      const req = httpMock.expectOne(expectedUrl);
      expect(req.request.method).toBe('GET');
      req.flush(mockResponse);
    });

    it('should handle different date formats correctly', () => {
      const filter: ReportFilter = {
        startDate: '2023-12-01',
        endDate: '2023-12-31',
        clientIdentification: '98765432',
      };

      service.generateReport(filter).subscribe();

      const expectedUrl = `${environment.apiUrlReport}/reporte?date=2023-12-01,2023-12-31&clientIdentification=98765432`;
      const req = httpMock.expectOne(expectedUrl);
      expect(req.request.method).toBe('GET');
      req.flush({ pdf: '', report: [] });
    });

    it('should retry on failure and handle errors', () => {
      const filter: ReportFilter = {
        startDate: '2023-01-01',
        endDate: '2023-01-31',
        clientIdentification: '12345678',
      };

      service.generateReport(filter).subscribe({
        next: () => expect(true).toBe(false),
        error: (error) => {
          expect(error).toBe('No se pudo conectar con el servidor');
        },
      });

      const expectedUrl = `${environment.apiUrlReport}/reporte?date=2023-01-01,2023-01-31&clientIdentification=12345678`;

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
});
