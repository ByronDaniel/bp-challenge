import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, inject } from '@angular/core';

import { BaseComponent } from '../../components/base/base.component';
import { Report, ReportFilter } from '../../types/report.type';
import { ReportListComponent } from '../../components/report/report-list/report-list.component';
import { ReportFormComponent } from '../../components/report/report-form/report-form.component';
import { ReportService } from '../../services/report.service';
import { AlertService } from '../../services/alert.service';

import { finalize, takeUntil } from 'rxjs';

@Component({
  selector: 'app-reports-view',
  standalone: true,
  imports: [CommonModule, ReportListComponent, ReportFormComponent],
  templateUrl: './reports-view.component.html',
  styleUrl: './reports-view.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ReportsViewComponent extends BaseComponent {
  private readonly reportService = inject(ReportService);
  private readonly alertService = inject(AlertService);

  protected reports: Report[] = [];

  protected onGenerateReport(filter: ReportFilter): void {
    this.setLoading(true);

    this.reportService
      .generateReport(filter)
      .pipe(
        takeUntil(this.destroy$),
        finalize(() => this.setLoading(false)),
      )
      .subscribe({
        next: (reports: Report[]) => {
          this.reports = reports;
          this.alertService.toast('Reporte generado exitosamente');
        },
        error: (error: any) =>
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
