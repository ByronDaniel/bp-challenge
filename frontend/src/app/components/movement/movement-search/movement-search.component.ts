import { CommonModule } from '@angular/common';
import { Component, EventEmitter, inject, OnInit, Output } from '@angular/core';
import { FormControl, ReactiveFormsModule } from '@angular/forms';

import { BaseComponent } from '../../base/base.component';
import { Movement } from '../../../types/movement.type';
import { MovementService } from '../../../services/movement.service';

import {
  catchError,
  debounceTime,
  distinctUntilChanged,
  switchMap,
  takeUntil,
} from 'rxjs/operators';
import { of } from 'rxjs';

@Component({
  selector: 'app-movement-search',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './movement-search.component.html',
  styleUrl: './movement-search.component.scss',
})
export class MovementSearchComponent extends BaseComponent implements OnInit {
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
          const numberAccount = trimmedTerm;
          if (numberAccount !== '') {
            return this.movementService.getByNumberAccount(numberAccount);
          }

          // Si no es un número válido, retornar array vacío
          return of([]);
        }),
        catchError((error) => {
          this.searchError.emit('Error al buscar movimientos');
          return of([]);
        }),
        takeUntil(this.destroy$),
      )
      .subscribe((movements) => this.filteredMovementsChange.emit(movements));
  }
}
