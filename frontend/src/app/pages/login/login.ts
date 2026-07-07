import { Component, inject, signal, OnInit } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router, ActivatedRoute, RouterLink } from '@angular/router';
import { environment } from '../../environment';
import { AuthService } from '../../services/auth-service'; // 1. CRUCIAL: Adjust this path to your actual AuthService location
import { ToastrService } from 'ngx-toastr';
import { passwordMatchValidator, passwordValidator } from '../../core/validators/auth.validators';

export interface AuthResponse {
  token: string;
  email: string;
  username: string
}

@Component({
  selector: 'app-login',
  standalone: true, // Make sure this is present if you are using standalone components
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login implements OnInit {
  private http = inject(HttpClient);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private authService = inject(AuthService); // 2. FIX: Injected the missing service reference here!
  private toastr = inject(ToastrService);

  isLoginOn = signal(true);

  responseErrorMessage = signal<string | null> (null);

  loginForm = new FormGroup({
    email: new FormControl('', {
      validators: [Validators.required, Validators.email],
      updateOn: 'blur' // Only validates when the user clicks out of the input box!
    }),
    password: new FormControl('', [Validators.required]),
  });

  registerForm = new FormGroup({
    profileName: new FormControl('', [Validators.required]),
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', [Validators.required, Validators.minLength(8), passwordValidator]),
    confirmPassword: new FormControl('', [Validators.required])
  }, { validators: passwordMatchValidator }); // applied to the whole group

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      if (params['tab'] === 'register') {
        this.isLoginOn.set(false);
      } else {
        this.isLoginOn.set(true);
      }
    });
  }

  onSubmit() {
    if (this.isLoginOn()) {
      // --- LOGIN SUBMISSION LOGIC ---
      if (this.loginForm.invalid) {
        this.loginForm.markAllAsTouched();
        return;
      }

      this.responseErrorMessage.set(null);
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

            this.toastr.success(`Welcome ${response.username}`)
          },
          error: (err) => {
            this.responseErrorMessage.set(err.error.message);
            console.error('Login failed:', err);
//             alert('Invalid email or password');
          }
        });
    } else {
      if (this.registerForm.invalid) {
          this.registerForm.markAllAsTouched();
          return;
      }

      const registerPayload = this.registerForm.value;

      this.http.post<AuthResponse>(`${environment.serverUrl}/auth/register`, registerPayload)
        .subscribe({
          next: (response) => {
            console.log('Register successful!', response);

            // 3. FIX: Let your centralized authService update both localStorage and the signal concurrently
            this.authService.updateToken(response.token);

            // Clean up the guest footprint
            localStorage.removeItem('guest_token');

            // Extract 'returnUrl' from address bar parameters, fallback to root '/'
            const destination = this.route.snapshot.queryParams['returnUrl'] || '/';
            console.log('Login success! Passing user forward to:', destination);

            this.router.navigateByUrl(destination);
            this.toastr.success(`Registration successful, welcome ${response.username}`)
          },
          error: (err) => {
            debugger;
            this.responseErrorMessage.set(err.error.message);
            console.error('Register failed:', err);
//             alert(err.error?.message || 'Invalid email or password');
          }
        });
    }
  }

  onToggleChange(event: Event) {
    const checkbox = event.target as HTMLInputElement;
    this.isLoginOn.set(!checkbox.checked);
  }


}
