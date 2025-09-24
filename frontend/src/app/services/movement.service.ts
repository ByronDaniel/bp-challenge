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
  private readonly apiUrl = `${environment.apiUrlMovement}/${this.endpoint}`;

  constructor(private readonly http: HttpClient) {}

  getAll(): Observable<Movement[]> {
    return this.http
      .get<Movement[]>(this.apiUrl)
      .pipe(retry(2), catchError(this.handleError));
  }

  getByNumberAccount(numberAccount: string): Observable<Movement[]> {
    return this.http
      .get<Movement[]>(`${this.apiUrl}?numberAccount=${numberAccount}`)
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
