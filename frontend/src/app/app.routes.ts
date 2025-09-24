import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    redirectTo: '/clientes',
    pathMatch: 'full',
  },
  {
    path: 'clientes',
    loadComponent: () =>
      import('./views/clients-view/clients-view.component').then(
        (m) => m.ClientsViewComponent,
      ),
    title: 'Clientes',
  },
  {
    path: 'clientes/crear',
    loadComponent: () =>
      import('./components/client/client-form/client-form.component').then(
        (m) => m.ClientFormComponent,
      ),
    title: 'Crear Cliente',
  },
  {
    path: 'clientes/editar/:id',
    loadComponent: () =>
      import('./components/client/client-form/client-form.component').then(
        (m) => m.ClientFormComponent,
      ),
    title: 'Editar Cliente',
  },
  {
    path: 'cuentas',
    loadComponent: () =>
      import('./views/accounts-view/accounts-view.component').then(
        (m) => m.AccountsViewComponent,
      ),
    title: 'Cuentas',
  },
  {
    path: 'cuentas/crear',
    loadComponent: () =>
      import('./components/account/account-form/account-form.component').then(
        (m) => m.AccountFormComponent,
      ),
    title: 'Crear Cuenta',
  },
  {
    path: 'cuentas/editar/:id',
    loadComponent: () =>
      import('./components/account/account-form/account-form.component').then(
        (m) => m.AccountFormComponent,
      ),
    title: 'Editar Cuenta',
  },
  {
    path: 'movimientos',
    loadComponent: () =>
      import('./views/movements-view/movements-view.component').then(
        (m) => m.MovementsViewComponent,
      ),
    title: 'Movimientos',
  },
  {
    path: 'movimientos/crear',
    loadComponent: () =>
      import(
        './components/movement/movement-form/movement-form.component'
      ).then((m) => m.MovementFormComponent),
    title: 'Crear Movimiento',
  },
  {
    path: 'reportes',
    loadComponent: () =>
      import('./views/reports-view/reports-view.component').then(
        (m) => m.ReportsViewComponent,
      ),
    title: 'Reportes',
  },
  {
    path: '**',
    redirectTo: '/clientes',
  },
];
