import { Component, ChangeDetectionStrategy } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-reports-view',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './reports-view.component.html',
  styleUrl: './reports-view.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ReportsViewComponent {}
