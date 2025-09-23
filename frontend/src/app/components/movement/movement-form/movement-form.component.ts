import {
  ChangeDetectionStrategy,
  Component,
  OnDestroy,
  OnInit,
  inject,
} from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  ReactiveFormsModule,
  FormBuilder,
  FormGroup,
  Validators,
  AbstractControl,
} from '@angular/forms';
import { Router } from '@angular/router';
import { Subject, takeUntil } from 'rxjs';
import { MovementService } from '../../../services/movement.service';
import { AlertService } from '../../../services/alert.service';

interface ValidationError {
  type: 'required' | 'min';
  message: string;
}

@Component({
  selector: 'app-movement-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './movement-form.component.html',
  styleUrl: './movement-form.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class MovementFormComponent implements OnInit, OnDestroy {
  private readonly fb = inject(FormBuilder);
  private readonly router = inject(Router);
  private readonly movementService = inject(MovementService);
  private readonly alertService = inject(AlertService);
  private readonly destroy$ = new Subject<void>();

  protected readonly movementForm = this.initForm();

  private readonly validationMessages: Record<string, ValidationError[]> = {
    type: [{ type: 'required', message: 'El tipo de movimiento es requerido' }],
    value: [
      { type: 'required', message: 'El valor es requerido' },
      { type: 'min', message: 'El valor debe ser mayor a 0' },
    ],
    accountId: [
      { type: 'required', message: 'El ID de la cuenta es requerido' },
      { type: 'min', message: 'El ID de la cuenta debe ser mayor a 0' },
    ],
  };

  ngOnInit(): void {
    // Los movimientos solo se crean, no se editan
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  protected onSave(): void {
    if (this.movementForm.invalid) {
      this.handleInvalidForm();
      return;
    }

    this.saveMovement();
  }

  protected onCancel(): void {
    this.router.navigate(['/movimientos']);
  }

  protected getFieldError(fieldName: string): string {
    const control = this.movementForm.get(fieldName);
    if (!control?.touched || !control?.errors) return '';

    const error = this.validationMessages[fieldName]?.find(
      (msg) => control.errors?.[msg.type],
    );

    return error?.message || '';
  }

  private initForm(): FormGroup {
    return this.fb.group({
      type: ['', Validators.required],
      value: [null, [Validators.required, Validators.min(0.01)]],
      accountId: [null, [Validators.required, Validators.min(1)]],
    });
  }

  private handleInvalidForm(): void {
    this.markFormGroupTouched(this.movementForm);
    this.alertService.warning(
      'Formulario inválido',
      'Completa todos los campos requeridos',
    );
  }

  private markFormGroupTouched(formGroup: FormGroup): void {
    Object.values(formGroup.controls).forEach((control: AbstractControl) =>
      control.markAsTouched(),
    );
  }

  private saveMovement(): void {
    const movementData = this.movementForm.getRawValue();

    this.movementService
      .create(movementData)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: () => this.handleSaveSuccess(),
        error: (error) =>
          this.alertService.error(
            'Error',
            `${error.error?.detail || 'Error al crear el movimiento'}`,
          ),
      });
  }

  private handleSaveSuccess(): void {
    this.alertService.success('Éxito', 'Movimiento creado exitosamente');
    this.router.navigate(['/movimientos']);
  }
}
