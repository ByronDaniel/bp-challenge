import { Component, ChangeDetectionStrategy } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-movements-view',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './movements-view.component.html',
  styleUrl: './movements-view.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class MovementsViewComponent {}
