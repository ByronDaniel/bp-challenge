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

import { Movement } from '../../types/movement.type';
import { MovementListComponent } from '../../components/movement/movement-list/movement-list.component';
import { MovementSearchComponent } from '../../components/movement/movement-search/movement-search.component';
import { MovementService } from '../../services/movement.service';
import { AlertService } from '../../services/alert.service';

@Component({
  selector: 'app-movements-view',
  standalone: true,
  imports: [RouterLink, MovementListComponent, MovementSearchComponent],
  templateUrl: './movements-view.component.html',
  styleUrl: './movements-view.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class MovementsViewComponent implements OnInit, OnDestroy {
  private readonly movementService = inject(MovementService);
  private readonly alertService = inject(AlertService);
  private readonly cdr = inject(ChangeDetectorRef);
  private readonly destroy$ = new Subject<void>();

  protected movements: Movement[] = [];
  protected filteredMovements: Movement[] = [];

  ngOnInit(): void {
    this.loadMovements();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  protected onFilteredMovementsChange(movements: Movement[]): void {
    this.filteredMovements = movements;
    this.cdr.markForCheck();
  }

  protected onSearchError(error: string): void {
    this.alertService.toast(error, 'error');
  }

  private loadMovements(): void {
    this.cdr.markForCheck();

    this.movementService
      .getAll()
      .pipe(
        takeUntil(this.destroy$),
        finalize(() => {
          this.cdr.markForCheck();
        }),
      )
      .subscribe({
        next: (movements) => {
          this.movements = movements;
          this.filteredMovements = movements;
        },
        error: (error) =>
          this.alertService.toast(
            error?.message || 'Error al cargar movimientos',
            'error',
          ),
      });
  }
}
