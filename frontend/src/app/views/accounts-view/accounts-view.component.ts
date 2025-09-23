import {
  ChangeDetectionStrategy,
  Component,
  inject,
  OnInit,
} from '@angular/core';
import { RouterLink } from '@angular/router';

import { BaseComponent } from '../../components/base/base.component';
import { Account } from '../../types/account.type';
import { AccountListComponent } from '../../components/account/account-list/account-list.component';
import { AccountSearchComponent } from '../../components/account/account-search/account-search.component';
import { AccountService } from '../../services/account.service';
import { AlertService } from '../../services/alert.service';

import { finalize, takeUntil } from 'rxjs';

@Component({
  selector: 'app-accounts-view',
  standalone: true,
  imports: [RouterLink, AccountListComponent, AccountSearchComponent],
  templateUrl: './accounts-view.component.html',
  styleUrl: './accounts-view.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AccountsViewComponent extends BaseComponent implements OnInit {
  private readonly accountService = inject(AccountService);
  private readonly alertService = inject(AlertService);

  protected accounts: Account[] = [];
  protected filteredAccounts: Account[] = [];

  ngOnInit(): void {
    this.loadAccounts();
  }

  protected async onDeleteAccount(account: Account): Promise<void> {
    if (!(await this.alertService.confirmDelete(`cuenta ${account.number}`)))
      return;

    this.accountService
      .delete(account.accountId)
      .pipe(
        takeUntil(this.destroy$),
        finalize(() => this.setLoading(false)),
      )
      .subscribe({
        next: () => {
          this.alertService.toast('Cuenta eliminada exitosamente');
          this.loadAccounts();
        },
        error: (error: any) =>
          this.alertService.toast(
            error?.message || 'No se pudo eliminar la cuenta',
            'error',
          ),
      });
  }

  protected onFilteredAccountsChange(accounts: Account[]): void {
    this.filteredAccounts = accounts;
  }

  private loadAccounts(): void {
    this.setLoading(true);

    this.accountService
      .getAll()
      .pipe(
        takeUntil(this.destroy$),
        finalize(() => this.setLoading(false)),
      )
      .subscribe({
        next: (accounts: Account[]) => {
          this.accounts = accounts;
          this.filteredAccounts = accounts;
        },
        error: (error: any) =>
          this.alertService.toast(
            error?.message || 'Error al cargar cuentas',
            'error',
          ),
      });
  }
}
