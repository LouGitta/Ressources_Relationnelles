import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { User } from '../../models/user.model';
import { API_ENDPOINTS } from '../../constants/api.constants';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private httpClient = inject(HttpClient);

  public getAll(): Observable<User[]> {
    return this.httpClient.get<User[]>(API_ENDPOINTS.USERS).pipe(
      catchError(this.handleError)
    );
  }

  public getById(id: number): Observable<User> {
    return this.httpClient.get<User>(`${API_ENDPOINTS.USERS}/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  public create(user: User): Observable<User> {
    return this.httpClient.post<User>(API_ENDPOINTS.USERS, user).pipe(
      catchError(this.handleError)
    );
  }

  public update(id: number, user: User): Observable<User> {
    return this.httpClient.put<User>(`${API_ENDPOINTS.USERS}/${id}`, user).pipe(
      catchError(this.handleError)
    );
  }

  public delete(id: number): Observable<void> {
    return this.httpClient.delete<void>(`${API_ENDPOINTS.USERS}/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  public getDemoUserData(): User {
    return {
      id: 0,
      username: 'demo',
      email: 'demo@mail.com',
      role: 'CITIZEN',
      createdAt: '2026-04-05T16:54:26',
      active: true,
    };
  }

  private handleError(error: any) {
    console.error('Erreur UserService:', error);
    return throwError(() => new Error(error.message || 'Une erreur est survenue dans le service utilisateur.'));
  }
}
