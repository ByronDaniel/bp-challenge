import {
  Component,
  EventEmitter,
  Input,
  Output,
  OnInit,
  inject,
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { Movement } from '../../../types/movement.type';
import { MovementService } from '../../../services/movement.service';
import {
  debounceTime,
  distinctUntilChanged,
  switchMap,
  catchError,
} from 'rxjs/operators';
import { of } from 'rxjs';

@Component({
  selector: 'app-movement-search',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './movement-search.component.html',
  styleUrl: './movement-search.component.scss',
})
export class MovementSearchComponent implements OnInit {
  @Output() filteredMovementsChange = new EventEmitter<Movement[]>();
  @Output() searchError = new EventEmitter<string>();

  private readonly movementService = inject(MovementService);
  searchControl = new FormControl('');

  ngOnInit(): void {
    this.setupSearch();
  }

  onClear(): void {
    this.searchControl.setValue('');
    // Cuando se limpia, emitir array vacío para mostrar todos
    this.filteredMovementsChange.emit([]);
  }

  private setupSearch(): void {
    this.searchControl.valueChanges
      .pipe(
        debounceTime(300),
        distinctUntilChanged(),
        switchMap((term) => {
          const trimmedTerm = term?.trim();
          if (!trimmedTerm) {
            return this.movementService.getAll();
          }

          // Si es un número, buscar por accountId
          const accountId = Number(trimmedTerm);
          if (!isNaN(accountId) && accountId > 0) {
            return this.movementService.getByAccountId(accountId);
          }

          // Si no es un número válido, retornar array vacío
          return of([]);
        }),
        catchError((error) => {
          this.searchError.emit('Error al buscar movimientos');
          return of([]);
        }),
      )
      .subscribe((movements) => this.filteredMovementsChange.emit(movements));
  }
}
