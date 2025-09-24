import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, catchError, retry, throwError } from 'rxjs';
import { Account } from '../types/account.type';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class AccountService {
  private readonly endpoint = 'cuentas';
  private readonly apiUrl = `${environment.apiUrlAccount}/${this.endpoint}`;

  constructor(private readonly http: HttpClient) {}

  getAll(): Observable<Account[]> {
    return this.http
      .get<Account[]>(this.apiUrl)
      .pipe(retry(2), catchError(this.handleError));
  }

  private handleError(error: HttpErrorResponse) {
    console.error('An error occurred:', error);
    return throwError(
      () => error.error?.message || 'No se pudo conectar con el servidor',
    );
  }

  getById(id: number): Observable<Account> {
    return this.http.get<Account>(`${this.apiUrl}/${id}`);
  }

  create(account: Omit<Account, 'accountId' | 'number'>): Observable<Account> {
    return this.http.post<Account>(this.apiUrl, account);
  }

  update(id: number, account: Partial<Account>): Observable<Account> {
    return this.http.put<Account>(`${this.apiUrl}/${id}`, account);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
