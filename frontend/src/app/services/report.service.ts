import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, catchError, retry, throwError } from 'rxjs';
import { Report, ReportFilter } from '../types/report.type';
import { environment } from '../../environments/environment';
import { ReportPdf } from '../types/report-pdf';

@Injectable({
  providedIn: 'root',
})
export class ReportService {
  private readonly baseUrl = environment.apiUrlReport;

  constructor(private readonly http: HttpClient) {}

  generateReport(filter: ReportFilter): Observable<ReportPdf> {
    const url = `${this.baseUrl}/reporte?date=${filter.startDate},${filter.endDate}&clientId=${filter.clientId}`;

    return this.http
      .get<ReportPdf>(url)
      .pipe(retry(2), catchError(this.handleError));
  }

  private handleError(error: HttpErrorResponse) {
    console.error('An error occurred:', error);
    return throwError(
      () => error.error?.message || 'No se pudo conectar con el servidor',
    );
  }
}
