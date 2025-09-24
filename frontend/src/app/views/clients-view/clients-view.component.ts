import {
  ChangeDetectionStrategy,
  Component,
  OnInit,
  inject,
} from '@angular/core';
import { RouterLink } from '@angular/router';
import { finalize, takeUntil } from 'rxjs';

import { BaseComponent } from '../../components/base/base.component';
import { Client } from '../../types/client.type';
import { ClientListComponent } from '../../components/client/client-list/client-list.component';
import { ClientSearchComponent } from '../../components/client/client-search/client-search.component';
import { ClientService } from '../../services/client.service';
import { AlertService } from '../../services/alert.service';

@Component({
  selector: 'app-clients-view',
  standalone: true,
  imports: [RouterLink, ClientListComponent, ClientSearchComponent],
  templateUrl: './clients-view.component.html',
  styleUrl: './clients-view.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ClientsViewComponent extends BaseComponent implements OnInit {
  private readonly clientService = inject(ClientService);
  private readonly alertService = inject(AlertService);

  protected clients: Client[] = [];
  protected filteredClients: Client[] = [];

  ngOnInit(): void {
    this.loadClients();
  }

  protected async onDeleteClient(client: Client): Promise<void> {
    const confirmed = await this.alertService.confirmDelete(client.name);
    if (!confirmed) return;

    this.clientService
      .delete(client.identification)
      .pipe(
        takeUntil(this.destroy$),
        finalize(() => this.cdr.markForCheck()),
      )
      .subscribe({
        next: () => {
          this.alertService.toast('Cliente eliminado exitosamente');
          this.loadClients();
        },
        error: (error) =>
          this.alertService.toast(
            error?.message || 'No se pudo eliminar el cliente',
            'error',
          ),
      });
  }

  protected onFilteredClientsChange(clients: Client[]): void {
    this.filteredClients = clients;
    this.cdr.markForCheck();
  }

  private loadClients(): void {
    this.setLoading(true);

    this.clientService
      .getAll()
      .pipe(
        takeUntil(this.destroy$),
        finalize(() => this.setLoading(false)),
      )
      .subscribe({
        next: (clients) => {
          this.clients = clients;
          this.filteredClients = clients;
          this.setEmpty(clients.length === 0);
        },
        error: (error) => {
          this.setError(true);
          this.alertService.toast('Error al cargar clientes', 'error');
        },
      });
  }
}
