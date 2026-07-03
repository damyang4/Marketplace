import { Component, inject } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router, ActivatedRoute } from '@angular/router';
import { environment } from '../../environment';
import { AuthService } from '../../services/auth-service'; // 1. CRUCIAL: Adjust this path to your actual AuthService location

interface AuthResponse {
  token: string;
  email: string;
}

@Component({
  selector: 'app-login',
  standalone: true, // Make sure this is present if you are using standalone components
  imports: [ReactiveFormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  private http = inject(HttpClient);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private authService = inject(AuthService); // 2. FIX: Injected the missing service reference here!

  loginForm = new FormGroup({
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', [Validators.required]),
  });

  onSubmit() {
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }

    const loginPayload = this.loginForm.value;

    this.http.post<AuthResponse>(`${environment.serverUrl}/auth/login`, loginPayload)
      .subscribe({
        next: (response) => {
          console.log('Login successful!', response);

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
          console.error('Login failed:', err);
          alert(err.error?.message || 'Invalid email or password');
        }
      });
  }
}
