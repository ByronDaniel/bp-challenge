import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, inject } from '@angular/core';

import { BaseComponent } from '../../components/base/base.component';
import { Report, ReportFilter } from '../../types/report.type';
import { ReportListComponent } from '../../components/report/report-list/report-list.component';
import { ReportFormComponent } from '../../components/report/report-form/report-form.component';
import { ReportService } from '../../services/report.service';
import { AlertService } from '../../services/alert.service';

import { finalize, takeUntil } from 'rxjs';
import { ReportPdf } from '../../types/report-pdf';

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
  protected pdfBase64: string = '';
  protected onGenerateReport(filter: ReportFilter): void {
    this.setLoading(true);

    this.reportService
      .generateReport(filter)
      .pipe(
        takeUntil(this.destroy$),
        finalize(() => this.setLoading(false)),
      )
      .subscribe({
        next: (reports: ReportPdf) => {
          this.reports = reports.report;
          this.pdfBase64 = reports.pdf;
          this.onDownloadPDF();
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

      const byteCharacters = atob(this.pdfBase64);
      const byteNumbers = new Array(byteCharacters.length);
      for (let i = 0; i < byteCharacters.length; i++) {
        byteNumbers[i] = byteCharacters.charCodeAt(i);
      }
      const byteArray = new Uint8Array(byteNumbers);
      const blob = new Blob([byteArray], { type: 'application/pdf' });

      const link = document.createElement('a');
      link.href = window.URL.createObjectURL(blob);
      link.download = 'reporte.pdf';
      link.click();

      window.URL.revokeObjectURL(link.href);
  
    }
}
