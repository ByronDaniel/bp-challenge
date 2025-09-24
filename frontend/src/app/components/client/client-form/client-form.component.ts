import { ChangeDetectionStrategy, Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { takeUntil } from 'rxjs';

import { BaseFormComponent } from '../../base/base-form.component';
import { Client } from '../../../types/client.type';
import { ClientService } from '../../../services/client.service';

@Component({
  selector: 'app-client-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './client-form.component.html',
  styleUrl: './client-form.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ClientFormComponent extends BaseFormComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly clientService = inject(ClientService);

  protected form = this.createForm();
  protected isEditing = false;
  private clientId: string | null = null;

  ngOnInit(): void {
    this.clientId = this.route.snapshot.paramMap.get('id');
    this.isEditing = Boolean(this.clientId);
    
    if (this.isEditing) this.loadClient();
  }

  onSubmit(): void {
    if (this.form.invalid) {
      this.markTouched();
      return;
    }

    const client = this.form.getRawValue();
    const request = this.isEditing 
      ? this.clientService.update(client)
      : this.clientService.create(client);

    request.pipe(takeUntil(this.destroy$)).subscribe({
      next: () => this.handleSuccess(
        `Cliente ${this.isEditing ? 'actualizado' : 'creado'} exitosamente`,
        '/clientes'
      ),
      error: (error) => this.handleError(error)
    });
  }

  onCancel(): void {
    this.router.navigate(['/clientes']);
  }

  protected getFieldError(fieldName: string): string {
    const control = this.form.get(fieldName);
    if (!control?.touched || !control?.errors) return '';
    
    const errors = control.errors;
    if (errors['required']) return 'Este campo es requerido';
    if (errors['min']) return 'Valor muy pequeño';
    if (errors['max']) return 'Valor muy grande';
    if (errors['minlength']) return 'Demasiado corto';
    return 'Campo inválido';
  }

  private createForm(): FormGroup {
    return this.fb.group({
      identification: ['', Validators.required],
      name: ['', Validators.required],
      gender: ['', Validators.required],
      age: [null, [Validators.required, Validators.min(18), Validators.max(100)]],
      phone: ['', Validators.required],
      address: ['', Validators.required],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  private loadClient(): void {
    const client = history.state?.client as Client;
    if (!client) {
      this.alert.toast('No se encontraron datos del cliente', 'error');
      this.router.navigate(['/clientes']);
      return;
    }

    this.form.patchValue({
      ...client,
      gender: client.gender.charAt(0).toUpperCase() + client.gender.slice(1).toLowerCase()
    });

    this.form.get('identification')?.disable();
  }
}
