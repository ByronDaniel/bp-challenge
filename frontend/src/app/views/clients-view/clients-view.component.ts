import { 
  ChangeDetectionStrategy, 
  ChangeDetectorRef, 
  Component, 
  OnDestroy, 
  OnInit, 
  inject 
} from '@angular/core';
import { RouterLink } from '@angular/router';
import { Subject, finalize, takeUntil } from 'rxjs';

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
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ClientsViewComponent implements OnInit, OnDestroy {
  private readonly clientService = inject(ClientService);
  private readonly alertService = inject(AlertService);
  private readonly cdr = inject(ChangeDetectorRef);
  private readonly destroy$ = new Subject<void>();

  protected clients: Client[] = [];
  protected filteredClients: Client[] = [];

  ngOnInit(): void {
    this.loadClients();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  protected async onDeleteClient(client: Client): Promise<void> {
    if (!await this.alertService.confirmDelete(client.name)) return;

    this.clientService.delete(client.id)
      .pipe(
        takeUntil(this.destroy$),
        finalize(() => this.cdr.markForCheck())
      )
      .subscribe({
        next: () => {
          this.alertService.toast('Cliente eliminado exitosamente');
          this.loadClients();
        },
        error: (error) => this.alertService.toast(
          error?.message || 'No se pudo eliminar el cliente', 
          'error'
        )
      });
  }

  protected onFilteredClientsChange(clients: Client[]): void {
    this.filteredClients = clients;
    this.cdr.markForCheck();
  }

  private loadClients(): void {
    this.cdr.markForCheck();

    this.clientService.getAll().pipe(
      takeUntil(this.destroy$),
      finalize(() => {
        this.cdr.markForCheck();
      })
    ).subscribe({
      next: clients => {
        this.clients = clients;
        this.filteredClients = clients;
      },
      error: (error) => this.alertService.toast(
        error?.message || 'Error al cargar clientes', 
        'error'
      )
    });
  }
}

