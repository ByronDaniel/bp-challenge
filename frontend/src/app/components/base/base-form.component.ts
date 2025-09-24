import { Directive, inject } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { AlertService } from '../../services/alert.service';
import { BaseComponent } from './base.component';

@Directive()
export abstract class BaseFormComponent extends BaseComponent {
  protected readonly fb = inject(FormBuilder);
  protected readonly router = inject(Router);
  protected readonly alert = inject(AlertService);

  protected abstract form: FormGroup;

  protected getError(field: string): string {
    const control = this.form.get(field);
    if (!control?.touched || !control?.errors) return '';

    const errors = control.errors;
    if (errors['required']) return 'Este campo es requerido';
    if (errors['email']) return 'Email inválido';
    if (errors['minlength']) return `Mínimo ${errors['minlength'].requiredLength} caracteres`;
    if (errors['maxlength']) return `Máximo ${errors['maxlength'].requiredLength} caracteres`;
    if (errors['min']) return `Valor mínimo: ${errors['min'].min}`;
    if (errors['max']) return `Valor máximo: ${errors['max'].max}`;
    if (errors['pattern']) return 'Formato inválido';

    return 'Campo inválido';
  }

  protected markTouched(): void {
    Object.values(this.form.controls).forEach(control => control.markAsTouched());
  }

  protected handleSuccess(message: string, route?: string): void {
    this.alert.toast(message);
    if (route) this.router.navigate([route]);
  }

  protected handleError(error: any): void {
    console.error(error);
    this.alert.toast(error?.error?.detail || 'Error inesperado', 'error');
  }
}
