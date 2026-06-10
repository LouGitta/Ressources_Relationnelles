import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, throwError } from 'rxjs';
import { tap, map } from 'rxjs/operators';
import { User } from '../models/user.model';
import { API_ENDPOINTS } from '../constants/api.constants';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private httpClient = inject(HttpClient);
  private currentUserSubject = new BehaviorSubject<User | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor() {
    const storedUser = localStorage.getItem('currentUser');
    if (storedUser) {
      try {
        this.currentUserSubject.next(JSON.parse(storedUser));
      } catch (e) {
        localStorage.removeItem('currentUser');
      }
    }
  }

  public login(username: string, password: string): Observable<User> {
    return this.httpClient.get<User[]>(API_ENDPOINTS.USERS).pipe(
      map(users => {
        const user = users.find(u => u.username === username);
        if (user) {
          localStorage.setItem('currentUser', JSON.stringify(user));
          localStorage.setItem('authToken', 'mock-token-' + user.id);
          this.currentUserSubject.next(user);
          return user;
        } else {
          throw new Error('Nom d\'utilisateur incorrect');
        }
      })
    );
  }

  public logout(): void {
    localStorage.removeItem('currentUser');
    localStorage.removeItem('authToken');
    this.currentUserSubject.next(null);
  }

  public isAuthenticated(): boolean {
    return this.currentUserSubject.value !== null && localStorage.getItem('authToken') !== null;
  }

  public getCurrentUser(): User | null {
    return this.currentUserSubject.value;
  }

  public hasRole(role: 'CITIZEN' | 'MODERATOR' | 'ADMINISTRATOR' | 'SUPERADMIN'): boolean {
    const user = this.getCurrentUser();
    return user ? user.role === role : false;
  }
}
