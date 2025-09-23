import { ChangeDetectionStrategy, Component, OnDestroy, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  ReactiveFormsModule,
  FormBuilder,
  FormGroup,
  Validators,
  AbstractControl
} from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { Subject, takeUntil } from 'rxjs';
import { Client } from '../../../types/client.type';
import { ClientService } from '../../../services/client.service';
import { AlertService } from '../../../services/alert.service';

interface ValidationError {
  type: 'required' | 'minlength' | 'min' | 'max';
  message: string;
}

@Component({
  selector: 'app-client-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './client-form.component.html',
  styleUrl: './client-form.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ClientFormComponent implements OnInit, OnDestroy {
  private readonly fb = inject(FormBuilder);
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);
  private readonly clientService = inject(ClientService);
  private readonly alertService = inject(AlertService);
  private readonly destroy$ = new Subject<void>();

  protected readonly clientForm = this.initForm();
  protected isEditing = false;
  protected clientId: string | null = null;

  private readonly validationMessages: Record<string, ValidationError[]> = {
    identification: [{ type: 'required', message: 'La identificación es requerida' }],
    name: [{ type: 'required', message: 'El nombre es requerido' }],
    gender: [{ type: 'required', message: 'El género es requerido' }],
    age: [
      { type: 'required', message: 'La edad es requerida' },
      { type: 'min', message: 'La edad debe ser mayor a 18 años' },
      { type: 'max', message: 'La edad debe ser menor a 100 años' }
    ],
    phone: [{ type: 'required', message: 'El teléfono es requerido' }],
    address: [{ type: 'required', message: 'La dirección es requerida' }],
    password: [
      { type: 'required', message: 'La contraseña es requerida' },
      { type: 'minlength', message: 'La contraseña debe tener al menos 6 caracteres' }
    ]
  };

  ngOnInit(): void {
    this.setupEditMode();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  protected onSave(): void {
    if (this.clientForm.invalid) {
      this.handleInvalidForm();
      return;
    }

    this.saveClient();
  }

  protected onCancel(): void {
    this.router.navigate(['/clientes']);
  }

  protected getFieldError(fieldName: string): string {
    const control = this.clientForm.get(fieldName);
    if (!control?.touched || !control?.errors) return '';

    const error = this.validationMessages[fieldName]?.find(
      msg => control.errors?.[msg.type]
    );
    
    return error?.message || '';
  }

  private initForm(): FormGroup {
    return this.fb.group({
      identification: ['', Validators.required],
      name: ['', Validators.required],
      gender: ['', Validators.required],
      age: [null, [Validators.required, Validators.min(18), Validators.max(100)]],
      phone: ['', Validators.required],
      address: ['', Validators.required],
      password: ['', [Validators.required, Validators.minLength(6)]],
      status: [true]
    });
  }

  private setupEditMode(): void {
    this.clientId = this.route.snapshot.paramMap.get('id');
    this.isEditing = Boolean(this.clientId);

    if (this.isEditing) {
      this.handleEditMode();
    }
  }

  private handleEditMode(): void {
    const clientFromState = history.state?.client as Client;
    if (!clientFromState) {
      this.alertService.error('Error', 'No se recibieron datos del cliente');
      this.router.navigate(['/clientes']);
      return;
    }

    this.populateForm(clientFromState);
  }

  private populateForm(client: Client): void {
    this.clientForm.patchValue({
      ...client,
      gender: this.capitalizeFirst(client.gender)
    });

    if (this.isEditing) {
      this.clientForm.get('identification')?.disable();
    }
  }

  private capitalizeFirst(text: string): string {
    return text.charAt(0).toUpperCase() + text.slice(1).toLowerCase();
  }

  private handleInvalidForm(): void {
    this.markFormGroupTouched(this.clientForm);
    this.alertService.warning('Formulario inválido', 'Completa todos los campos requeridos');
  }

  private markFormGroupTouched(formGroup: FormGroup): void {
    Object.values(formGroup.controls).forEach(
      (control: AbstractControl) => control.markAsTouched()
    );
  }

  private saveClient(): void {
    const clientData = this.clientForm.getRawValue();
    const operation = this.isEditing
      ? this.clientService.update(Number(this.clientId), clientData)
      : this.clientService.create(clientData);

    operation.pipe(
      takeUntil(this.destroy$)
    ).subscribe({
      next: () => this.handleSaveSuccess(),
      error: (error) => this.alertService.error('Error', `${error.error.detail}`)
    });
  }

  private handleSaveSuccess(): void {
    const action = this.isEditing ? 'actualizado' : 'creado';
    this.alertService.success('Éxito', `Cliente ${action}`);
    this.router.navigate(['/clientes']);
  }

}
