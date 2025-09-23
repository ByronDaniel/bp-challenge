import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  OnDestroy,
  inject,
} from '@angular/core';
import { Subject, finalize, takeUntil } from 'rxjs';

import { Report, ReportFilter } from '../../types/report.type';
import { ReportListComponent } from '../../components/report/report-list/report-list.component';
import { ReportFormComponent } from '../../components/report/report-form/report-form.component';
import { ReportService } from '../../services/report.service';
import { AlertService } from '../../services/alert.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-reports-view',
  standalone: true,
  imports: [CommonModule, ReportListComponent, ReportFormComponent],
  templateUrl: './reports-view.component.html',
  styleUrl: './reports-view.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ReportsViewComponent implements OnDestroy {
  private readonly reportService = inject(ReportService);
  private readonly alertService = inject(AlertService);
  private readonly cdr = inject(ChangeDetectorRef);
  private readonly destroy$ = new Subject<void>();

  protected reports: Report[] = [];
  protected isLoading = false;

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  protected onGenerateReport(filter: ReportFilter): void {
    this.isLoading = true;
    this.cdr.markForCheck();

    this.reportService
      .generateReport(filter)
      .pipe(
        takeUntil(this.destroy$),
        finalize(() => {
          this.isLoading = false;
          this.cdr.markForCheck();
        }),
      )
      .subscribe({
        next: (reports) => {
          this.reports = reports;
          this.alertService.toast('Reporte generado exitosamente');
        },
        error: (error) =>
          this.alertService.toast(
            error?.message || 'Error al generar reporte',
            'error',
          ),
      });
  }

  protected onDownloadPDF(): void {
    // PDF download logic would be implemented here
    this.alertService.toast(
      'Funcionalidad de descarga PDF pendiente de implementación',
    );
  }
}
