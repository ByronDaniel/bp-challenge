import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, catchError, retry, throwError } from 'rxjs';
import { Movement } from '../types/movement.type';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class MovementService {
  private readonly endpoint = 'movimientos';
  private readonly apiUrl = `https://9e7043da-4f4b-431d-afdf-d300eafc5e90.mock.pstmn.io/${this.endpoint}`;

  constructor(private readonly http: HttpClient) {}

  getAll(): Observable<Movement[]> {
    return this.http
      .get<Movement[]>(this.apiUrl)
      .pipe(retry(2), catchError(this.handleError));
  }

  getByAccountId(accountId: number): Observable<Movement[]> {
    return this.http
      .get<Movement[]>(`${this.apiUrl}?accountId=${accountId}`)
      .pipe(retry(2), catchError(this.handleError));
  }

  private handleError(error: HttpErrorResponse) {
    console.error('An error occurred:', error);
    return throwError(
      () => error.error?.message || 'No se pudo conectar con el servidor',
    );
  }

  create(
    movement: Omit<Movement, 'movementId' | 'date' | 'balance'>,
  ): Observable<Movement> {
    return this.http.post<Movement>(this.apiUrl, movement);
  }
}
