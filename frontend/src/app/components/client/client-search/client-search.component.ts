import { Component, EventEmitter, Input, Output, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { Client } from '../../../types/client.type';
import { debounceTime, distinctUntilChanged, map } from 'rxjs/operators';

@Component({
  selector: 'app-client-search',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './client-search.component.html',
  styleUrl: './client-search.component.scss'
})
export class ClientSearchComponent implements OnInit {
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
    this.searchControl.valueChanges.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      map(term => this.filterClients(term || ''))
    ).subscribe(filtered => this.filteredClientsChange.emit(filtered));
  }

  private filterClients(term: string): Client[] {
    const normalizedTerm = term.toLowerCase().trim();
    return !normalizedTerm ? [...this.clients] : this.clients.filter(
      client => this.matchesSearchCriteria(client, normalizedTerm)
    );
  }

  private matchesSearchCriteria(client: Client, term: string): boolean {
    return [client.identification, client.name]
      .map(value => value.toLowerCase())
      .some(value => value.includes(term));
  }
}
