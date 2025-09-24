import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  Output,
  inject,
} from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  ReactiveFormsModule,
  FormBuilder,
  FormGroup,
  Validators,
} from '@angular/forms';

import { BaseComponent } from '../../base/base.component';
import { ReportFilter } from '../../../types/report.type';

@Component({
  selector: 'app-report-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './report-form.component.html',
  styleUrl: './report-form.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ReportFormComponent extends BaseComponent {
  private readonly fb = inject(FormBuilder);

  @Output() generateReport = new EventEmitter<ReportFilter>();

  protected reportForm: FormGroup = this.initForm();

  protected onSubmit(): void {
    if (this.reportForm.invalid) {
      this.markFormGroupTouched();
      return;
    }

    const formValue = this.reportForm.value;
    this.generateReport.emit({
      clientIdentification: formValue.clientIdentification,
      startDate: formValue.startDate,
      endDate: formValue.endDate,
    });
  }

  protected onReset(): void {
    this.reportForm.reset();
  }

  protected getFieldError(fieldName: string): string {
    const control = this.reportForm.get(fieldName);
    if (!control?.touched || !control?.errors) return '';
    
    const errors = control.errors;
    if (errors['required']) return 'Este campo es requerido';
    if (errors['min']) return 'El ID debe ser mayor a 0';
    return 'Campo inválido';
  }

  private initForm(): FormGroup {
    return this.fb.group({
      clientIdentification: [null, [Validators.required, Validators.min(1)]],
      startDate: ['', [Validators.required]],
      endDate: ['', [Validators.required]],
    });
  }

  private markFormGroupTouched(): void {
    Object.values(this.reportForm.controls).forEach((control) => {
      control.markAsTouched();
    });
  }
}
