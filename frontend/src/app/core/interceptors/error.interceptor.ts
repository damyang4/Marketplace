import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status === 401) {
        // 1. Define URLs that are allowed to fail with a 401 without forcing a login redirect
        const bypassedUrls = ['/cart', '/products', '/register' ];

        // 2. Check if the current request URL matches any bypassed URLs
        const isBypassed = bypassedUrls.some(url => req.url.includes(url));

        if (!isBypassed) {
          console.warn('Session expired on a protected route. Redirecting to login...');
          localStorage.removeItem('auth_token');
          router.navigate(['/login']);
        }
      }

      // 3. Crucial: Pass the error back down to the ProductService so it doesn't freeze!
      return throwError(() => error);
    })
  );
};
