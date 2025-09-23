import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, catchError, retry, throwError } from 'rxjs';
import { Report, ReportFilter } from '../types/report.type';

@Injectable({
  providedIn: 'root',
})
export class ReportService {
  private readonly baseUrl =
    'https://9e7043da-4f4b-431d-afdf-d300eafc5e90.mock.pstmn.io';

  constructor(private readonly http: HttpClient) {}

  generateReport(filter: ReportFilter): Observable<Report[]> {
    const url = `${this.baseUrl}/reporte?date=${filter.startDate},${filter.endDate}&clientId=${filter.clientId}`;

    return this.http
      .get<Report[]>(url)
      .pipe(retry(2), catchError(this.handleError));
  }

  private handleError(error: HttpErrorResponse) {
    console.error('An error occurred:', error);
    return throwError(
      () => error.error?.message || 'No se pudo conectar con el servidor',
    );
  }
}
