import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

export const passwordMatchValidator: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
  const password = control.get('password');
  const confirmPassword = control.get('confirmPassword');

  if (!password || !confirmPassword) {
    return null;
  }

  // If they don't match, attach an error object to the parent FormGroup
  return password.value === confirmPassword.value ? null : { passwordMismatch: true };
};

export const passwordValidator: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
  if (!control.value) return null;

  const hasNumber = /\d/.test(control.value);
  const hasLetter = /[a-z]/.test(control.value);

  const isValid = hasNumber && hasLetter;

  return isValid ? null : { invalidPassword: true };
}
