import { CommonModule } from '@angular/common';
import {
  Component,
  EventEmitter,
  inject,
  Input,
  OnInit,
  Output,
} from '@angular/core';
import { FormControl, ReactiveFormsModule } from '@angular/forms';

import { BaseComponent } from '../../base/base.component';
import { Client } from '../../../types/client.type';

import {
  debounceTime,
  distinctUntilChanged,
  map,
  takeUntil,
} from 'rxjs/operators';

@Component({
  selector: 'app-client-search',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './client-search.component.html',
  styleUrl: './client-search.component.scss',
})
export class ClientSearchComponent extends BaseComponent implements OnInit {
  @Input() clients: Client[] = [];
  @Output() filteredClientsChange = new EventEmitter<Client[]>();

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
        map((term) => this.filterClients(term || '')),
        takeUntil(this.destroy$),
      )
      .subscribe((filtered) => this.filteredClientsChange.emit(filtered));
  }

  private filterClients(term: string): Client[] {
    const normalizedTerm = term.toLowerCase().trim();
    return !normalizedTerm
      ? [...this.clients]
      : this.clients.filter((client) =>
          this.matchesSearchCriteria(client, normalizedTerm),
        );
  }

  private matchesSearchCriteria(client: Client, term: string): boolean {
    return [client.identification, client.name]
      .map((value) => value.toLowerCase())
      .some((value) => value.includes(term));
  }
}
