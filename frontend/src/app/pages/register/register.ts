import { Component, inject } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router, ActivatedRoute, RouterLink } from '@angular/router';
import { environment } from '../../environment';
import { AuthService } from '../../services/auth-service';
import { AuthResponse } from '../login/login'

@Component({
  selector: 'app-register',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {
  private http = inject(HttpClient);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private authService = inject(AuthService);

  registerForm = new FormGroup({
    profileName: new FormControl('', [Validators.required]),
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', [Validators.required]),
    confirmPassword: new FormControl('', [Validators.required])
  });

  onSubmit() {
    debugger;
    if (this.registerForm.invalid) {
          this.registerForm.markAllAsTouched();
          return;
    }

    const registerPayload = this.registerForm.value;

    this.http.post<AuthResponse>(`${environment.serverUrl}/auth/register`, registerPayload)
      .subscribe({
        next: (response) => {
          debugger;
          console.log('Register successful!', response);

          // 3. FIX: Let your centralized authService update both localStorage and the signal concurrently
          this.authService.updateToken(response.token);

          // Clean up the guest footprint
          localStorage.removeItem('guest_token');

          // Extract 'returnUrl' from address bar parameters, fallback to root '/'
          const destination = this.route.snapshot.queryParams['returnUrl'] || '/';
          console.log('Login success! Passing user forward to:', destination);

          this.router.navigateByUrl(destination);
        },
        error: (err) => {
          debugger;
          console.error('Register failed:', err);
          alert(err.error?.message || 'Invalid email or password');
        }
      });
  }
}
