import { ChangeDetectionStrategy, Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Client } from '../../../types/client.type';

@Component({
  selector: 'app-client-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './client-list.component.html',
  styleUrl: './client-list.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ClientListComponent {
  @Input() clients: Client[] = [];
  @Output() deleteClient = new EventEmitter<Client>();

  protected readonly trackById = (_: number, client: Client): number => client.id;
}
