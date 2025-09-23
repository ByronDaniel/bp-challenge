import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  Output,
  inject,
} from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ReportFilter } from '../../../types/report.type';

@Component({
  selector: 'app-report-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './report-form.component.html',
  styleUrl: './report-form.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ReportFormComponent {
  private readonly fb = inject(FormBuilder);

  @Output() generateReport = new EventEmitter<ReportFilter>();

  protected reportForm: FormGroup = this.fb.group({
    clientId: [null, [Validators.required, Validators.min(1)]],
    startDate: ['', [Validators.required]],
    endDate: ['', [Validators.required]],
  });

  protected onSubmit(): void {
    if (this.reportForm.valid) {
      const formValue = this.reportForm.value;
      this.generateReport.emit({
        clientId: formValue.clientId,
        startDate: formValue.startDate,
        endDate: formValue.endDate,
      });
    }
  }

  protected onReset(): void {
    this.reportForm.reset();
  }
}
