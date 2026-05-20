import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class RessourceService {
  private httpClient: HttpClient = inject(HttpClient);

  public getRessources(): Observable<any> {
    return this.httpClient.get(environment.apiUrl + '/api/ressources');
  };

  public getRessourcesDetails(id: any): Observable<any> {
    return this.httpClient.get(`${environment.apiUrl}/api/ressources/${id}`);
  };
}
