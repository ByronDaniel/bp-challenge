import { Component, ChangeDetectionStrategy } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-accounts-view',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './accounts-view.component.html',
  styleUrl: './accounts-view.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AccountsViewComponent {}
