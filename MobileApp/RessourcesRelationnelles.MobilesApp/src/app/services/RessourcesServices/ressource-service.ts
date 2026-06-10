import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Ressource } from '../../models/ressource.model';
import { API_ENDPOINTS } from '../../constants/api.constants';

@Injectable({
  providedIn: 'root',
})
export class RessourceService {
  private httpClient: HttpClient = inject(HttpClient);

  public getRessources(): Observable<Ressource[]> {
    return this.httpClient.get<Ressource[]>(API_ENDPOINTS.RESSOURCES).pipe(
      catchError(this.handleError)
    );
  }

  public getRessourcesDetails(id: number): Observable<Ressource> {
    return this.httpClient.get<Ressource>(`${API_ENDPOINTS.RESSOURCES}/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  public create(ressource: Ressource): Observable<Ressource> {
    return this.httpClient.post<Ressource>(API_ENDPOINTS.RESSOURCES, ressource).pipe(
      catchError(this.handleError)
    );
  }

  public update(id: number, ressource: Ressource): Observable<Ressource> {
    return this.httpClient.put<Ressource>(`${API_ENDPOINTS.RESSOURCES}/${id}`, ressource).pipe(
      catchError(this.handleError)
    );
  }

  public delete(id: number): Observable<void> {
    return this.httpClient.delete<void>(`${API_ENDPOINTS.RESSOURCES}/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  private handleError(error: any) {
    console.error('Erreur RessourceService:', error);
    return throwError(() => new Error(error.message || 'Une erreur est survenue dans le service des ressources.'));
  }
}
