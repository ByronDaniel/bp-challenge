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
import { Router, ActivatedRoute } from '@angular/router';
import { Subject, takeUntil } from 'rxjs';

import { Account } from '../../../types/account.type';
import { AccountService } from '../../../services/account.service';
import { AlertService } from '../../../services/alert.service';

@Component({
  selector: 'app-account-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './account-form.component.html',
  styleUrl: './account-form.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AccountFormComponent implements OnInit, OnDestroy {
  private readonly fb = inject(FormBuilder);
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);
  private readonly accountService = inject(AccountService);
  private readonly alertService = inject(AlertService);
  private readonly destroy$ = new Subject<void>();

  protected readonly accountForm = this.initForm();
  protected isEditing = false;
  protected accountId: string | null = null;

  ngOnInit(): void {
    this.setupEditMode();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  protected onSave(): void {
    if (this.accountForm.invalid) {
      this.handleInvalidForm();
      return;
    }

    this.saveAccount();
  }

  protected onCancel(): void {
    this.router.navigate(['/cuentas']);
  }

  protected getFieldError(fieldName: string): string {
    const control = this.accountForm.get(fieldName);
    if (!control?.touched || !control?.errors) return '';
    
    const errors = control.errors;
    if (errors['required']) return 'Este campo es requerido';
    if (errors['min']) return 'Valor demasiado pequeño';
    return 'Campo inválido';
  }

  private initForm(): FormGroup {
    return this.fb.group({
      type: ['', Validators.required],
      balance: [0, [Validators.required, Validators.min(0)]],
      clientId: [null, [Validators.required, Validators.min(1)]],
      status: [true],
    });
  }

  private setupEditMode(): void {
    this.accountId = this.route.snapshot.paramMap.get('id');
    this.isEditing = Boolean(this.accountId);

    if (this.isEditing) {
      this.handleEditMode();
    }
  }

  private handleEditMode(): void {
    const accountFromState = history.state?.account as Account;
    if (!accountFromState) {
      this.alertService.error('Error', 'No se recibieron datos de la cuenta');
      this.router.navigate(['/cuentas']);
      return;
    }

    this.populateForm(accountFromState);
  }

  private populateForm(account: Account): void {
    this.accountForm.patchValue({
      type: account.type,
      balance: account.balance,
      clientId: account.clientId,
      status: account.status,
    });
  }

  private handleInvalidForm(): void {
    this.markFormGroupTouched(this.accountForm);
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

  private saveAccount(): void {
    const accountData = this.accountForm.getRawValue();
    const operation = this.isEditing
      ? this.accountService.update(Number(this.accountId), accountData)
      : this.accountService.create(accountData);

    operation.pipe(takeUntil(this.destroy$)).subscribe({
      next: () => this.handleSaveSuccess(),
      error: (error) =>
        this.alertService.error(
          'Error',
          `${error.error?.detail || 'Error al procesar la cuenta'}`,
        ),
    });
  }

  private handleSaveSuccess(): void {
    const action = this.isEditing ? 'actualizada' : 'creada';
    this.alertService.success('Éxito', `Cuenta ${action}`);
    this.router.navigate(['/cuentas']);
  }
}
