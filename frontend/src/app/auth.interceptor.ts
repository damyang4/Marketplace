import { HttpInterceptorFn } from '@angular/common/http';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  // 1. Pull the token we saved in Step 3
  const token = localStorage.getItem('auth_token');
  const guestToken = localStorage.getItem('guest_token');

  // dynamic headers dictionary
  const headers: { [key: string]: string } = {};

  // Add the Bearer token if the user is logged in
  if (token) {
    headers['Authorization'] = `Bearer ${token}`;
  }

  // Add the Guest Token if it exists and isn't a corrupt string representation
  if (guestToken && guestToken !== 'null' && guestToken !== 'undefined') {
    headers['X-Guest-Token'] = guestToken;
  }

  // If we have any headers to append, clone the request. Otherwise, pass it through.
  if (Object.keys(headers).length > 0) {
    const authReq = req.clone({ setHeaders: headers });
    return next(authReq);
  }

  // Otherwise, let the request go through normally
  return next(req);
};
