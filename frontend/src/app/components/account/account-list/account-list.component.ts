import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  Input,
  Output,
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Account } from '../../../types/account.type';

@Component({
  selector: 'app-account-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './account-list.component.html',
  styleUrl: './account-list.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AccountListComponent {
  @Input() accounts: Account[] = [];
  @Output() deleteAccount = new EventEmitter<Account>();

  protected readonly trackById = (_: number, account: Account): number =>
    account.accountId;
}
