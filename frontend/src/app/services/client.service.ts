import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, catchError, retry, throwError } from 'rxjs';
import { Client } from '../types/client.type';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ClientService {
  private readonly endpoint = 'clientes';
  private readonly apiUrl = `${environment.apiUrlClient}/${this.endpoint}`;

  constructor(private readonly http: HttpClient) {}

  getAll(): Observable<Client[]> {
    return this.http.get<Client[]>(this.apiUrl).pipe(
      retry(2),
      catchError(this.handleError)
    );
  }

  private handleError(error: HttpErrorResponse) {
    console.error('An error occurred:', error);
    return throwError(() => 
      error.error?.message || 'No se pudo conectar con el servidor'
    );
  }

  getById(id: number): Observable<Client> {
    return this.http.get<Client>(`${this.apiUrl}/${id}`);
  }

  create(client: Omit<Client, 'id'>): Observable<Client> {
    return this.http.post<Client>(this.apiUrl, client);
  }

  update(client: Partial<Client>): Observable<Client> {
    return this.http.put<Client>(`${this.apiUrl}`, client);
  }

  delete(identification: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${identification}`);
  }
}
