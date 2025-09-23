import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  OnDestroy,
  OnInit,
  inject,
} from '@angular/core';
import { RouterLink } from '@angular/router';
import { Subject, finalize, takeUntil } from 'rxjs';

import { Account } from '../../types/account.type';
import { AccountListComponent } from '../../components/account/account-list/account-list.component';
import { AccountSearchComponent } from '../../components/account/account-search/account-search.component';
import { AccountService } from '../../services/account.service';
import { AlertService } from '../../services/alert.service';

@Component({
  selector: 'app-accounts-view',
  standalone: true,
  imports: [RouterLink, AccountListComponent, AccountSearchComponent],
  templateUrl: './accounts-view.component.html',
  styleUrl: './accounts-view.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AccountsViewComponent implements OnInit, OnDestroy {
  private readonly accountService = inject(AccountService);
  private readonly alertService = inject(AlertService);
  private readonly cdr = inject(ChangeDetectorRef);
  private readonly destroy$ = new Subject<void>();

  protected accounts: Account[] = [];
  protected filteredAccounts: Account[] = [];

  ngOnInit(): void {
    this.loadAccounts();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  protected async onDeleteAccount(account: Account): Promise<void> {
    if (!(await this.alertService.confirmDelete(`cuenta ${account.number}`)))
      return;

    this.accountService
      .delete(account.accountId)
      .pipe(
        takeUntil(this.destroy$),
        finalize(() => this.cdr.markForCheck()),
      )
      .subscribe({
        next: () => {
          this.alertService.toast('Cuenta eliminada exitosamente');
          this.loadAccounts();
        },
        error: (error) =>
          this.alertService.toast(
            error?.message || 'No se pudo eliminar la cuenta',
            'error',
          ),
      });
  }

  protected onFilteredAccountsChange(accounts: Account[]): void {
    this.filteredAccounts = accounts;
    this.cdr.markForCheck();
  }

  private loadAccounts(): void {
    this.cdr.markForCheck();

    this.accountService
      .getAll()
      .pipe(
        takeUntil(this.destroy$),
        finalize(() => {
          this.cdr.markForCheck();
        }),
      )
      .subscribe({
        next: (accounts) => {
          this.accounts = accounts;
          this.filteredAccounts = accounts;
        },
        error: (error) =>
          this.alertService.toast(
            error?.message || 'Error al cargar cuentas',
            'error',
          ),
      });
  }
}
