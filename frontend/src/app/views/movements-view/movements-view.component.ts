import {
  ChangeDetectionStrategy,
  Component,
  inject,
  OnInit,
} from '@angular/core';
import { RouterLink } from '@angular/router';

import { BaseComponent } from '../../components/base/base.component';
import { Movement } from '../../types/movement.type';
import { MovementListComponent } from '../../components/movement/movement-list/movement-list.component';
import { MovementSearchComponent } from '../../components/movement/movement-search/movement-search.component';
import { MovementService } from '../../services/movement.service';
import { AlertService } from '../../services/alert.service';

import { finalize, takeUntil } from 'rxjs';

@Component({
  selector: 'app-movements-view',
  standalone: true,
  imports: [RouterLink, MovementListComponent, MovementSearchComponent],
  templateUrl: './movements-view.component.html',
  styleUrl: './movements-view.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class MovementsViewComponent extends BaseComponent implements OnInit {
  private readonly movementService = inject(MovementService);
  private readonly alertService = inject(AlertService);

  protected movements: Movement[] = [];
  protected filteredMovements: Movement[] = [];

  ngOnInit(): void {
    this.loadMovements();
  }

  protected onFilteredMovementsChange(movements: Movement[]): void {
    this.filteredMovements = movements;
  }

  protected onSearchError(error: string): void {
    this.alertService.toast(error, 'error');
  }

  private loadMovements(): void {
    this.setLoading(true);

    this.movementService
      .getAll()
      .pipe(
        takeUntil(this.destroy$),
        finalize(() => this.setLoading(false)),
      )
      .subscribe({
        next: (movements: Movement[]) => {
          this.movements = movements;
          this.filteredMovements = movements;
        },
        error: (error: any) =>
          this.alertService.toast(
            error?.message || 'Error al cargar movimientos',
            'error',
          ),
      });
  }
}
