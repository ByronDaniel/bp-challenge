import { ChangeDetectorRef, Directive, OnDestroy, inject } from '@angular/core';
import { Subject } from 'rxjs';

@Directive()
export abstract class BaseComponent implements OnDestroy {
  protected readonly cdr = inject(ChangeDetectorRef);
  protected readonly destroy$ = new Subject<void>();

  isLoading = false;
  hasError = false;
  isEmpty = false;

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  protected setLoading(loading: boolean): void {
    this.isLoading = loading;
    this.cdr.markForCheck();
  }

  protected setError(error: boolean): void {
    this.hasError = error;
    this.cdr.markForCheck();
  }

  protected setEmpty(empty: boolean): void {
    this.isEmpty = empty;
    this.cdr.markForCheck();
  }
}
