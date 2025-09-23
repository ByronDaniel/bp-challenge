import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormControl, ReactiveFormsModule } from '@angular/forms';

import { BaseComponent } from '../../base/base.component';
import { Report } from '../../../types/report.type';

import {
  debounceTime,
  distinctUntilChanged,
  map,
  takeUntil,
} from 'rxjs/operators';

@Component({
  selector: 'app-report-search',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './report-search.component.html',
  styleUrl: './report-search.component.scss',
})
export class ReportSearchComponent extends BaseComponent implements OnInit {
  @Input() reports: Report[] = [];
  @Output() filteredReportsChange = new EventEmitter<Report[]>();

  searchControl = new FormControl('');

  ngOnInit(): void {
    this.setupSearch();
  }

  onClear(): void {
    this.searchControl.setValue('');
  }

  private setupSearch(): void {
    this.searchControl.valueChanges
      .pipe(
        debounceTime(300),
        distinctUntilChanged(),
        map((term) => this.filterReports(term || '')),
        takeUntil(this.destroy$),
      )
      .subscribe((filtered) => this.filteredReportsChange.emit(filtered));
  }

  private filterReports(term: string): Report[] {
    const normalizedTerm = term.toLowerCase().trim();
    return !normalizedTerm
      ? [...this.reports]
      : this.reports.filter((report) =>
          this.matchesSearchCriteria(report, normalizedTerm),
        );
  }

  private matchesSearchCriteria(report: Report, term: string): boolean {
    return [report.name, report.number, report.typeAccount]
      .map((value) => value.toLowerCase())
      .some((value) => value.includes(term));
  }
}
