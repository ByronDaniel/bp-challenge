import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormControl, ReactiveFormsModule } from '@angular/forms';

import { BaseComponent } from '../../base/base.component';
import { Account } from '../../../types/account.type';

import {
  debounceTime,
  distinctUntilChanged,
  map,
  takeUntil,
} from 'rxjs/operators';

@Component({
  selector: 'app-account-search',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './account-search.component.html',
  styleUrl: './account-search.component.scss',
})
export class AccountSearchComponent extends BaseComponent implements OnInit {
  @Input() accounts: Account[] = [];
  @Output() filteredAccountsChange = new EventEmitter<Account[]>();

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
        map((term) => this.filterAccounts(term || '')),
        takeUntil(this.destroy$),
      )
      .subscribe((filtered) => this.filteredAccountsChange.emit(filtered));
  }

  private filterAccounts(term: string): Account[] {
    const normalizedTerm = term.toLowerCase().trim();
    return !normalizedTerm
      ? [...this.accounts]
      : this.accounts.filter((account) =>
          this.matchesSearchCriteria(account, normalizedTerm),
        );
  }

  private matchesSearchCriteria(account: Account, term: string): boolean {
    return [account.number, account.type, account.number.toString()]
      .map((value) => value.toLowerCase())
      .some((value) => value.includes(term));
  }
}
