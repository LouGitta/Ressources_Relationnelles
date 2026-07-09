import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { AuthService } from '../services/auth.service';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);
  const authService = inject(AuthService);

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      if ([401, 403].includes(error.status)) {
        authService.logout();
        router.navigate(['/tabs/account']);
      }
      
      const errorMessage = error.error?.message || error.statusText || 'Erreur serveur inconnue';
      console.error(`[HTTP Error ${error.status}]: ${errorMessage}`);
      return throwError(() => error);
    })
  );
};
