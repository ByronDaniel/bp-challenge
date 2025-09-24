import { Injectable } from '@angular/core';
import Swal from 'sweetalert2';

@Injectable({
  providedIn: 'root',
})
export class AlertService {
  /**
   * Mostrar alerta de éxito
   */
  success(title: string, text?: string): Promise<any> {
    return Swal.fire({
      title,
      text,
      icon: 'success',
      confirmButtonText: 'OK',
      confirmButtonColor: '#28a745',
    });
  }

  /**
   * Mostrar alerta de error
   */
  error(title: string, text?: string): Promise<any> {
    return Swal.fire({
      title,
      text,
      icon: 'error',
      confirmButtonText: 'OK',
      confirmButtonColor: '#dc3545',
    });
  }

  /**
   * Mostrar alerta de advertencia
   */
  warning(title: string, text?: string): Promise<any> {
    return Swal.fire({
      title,
      text,
      icon: 'warning',
      confirmButtonText: 'OK',
      confirmButtonColor: '#ffc107',
    });
  }

  /**
   * Mostrar alerta de información
   */
  info(title: string, text?: string): Promise<any> {
    return Swal.fire({
      title,
      text,
      icon: 'info',
      confirmButtonText: 'OK',
      confirmButtonColor: '#17a2b8',
    });
  }

  /**
   * Mostrar confirmación (Sí/No)
   */
  confirm(title: string, text?: string): Promise<boolean> {
    return Swal.fire({
      title,
      text,
      icon: 'question',
      showCancelButton: true,
      confirmButtonText: 'Sí',
      cancelButtonText: 'No',
      confirmButtonColor: '#007bff',
      cancelButtonColor: '#6c757d',
    }).then((result) => {
      return result.isConfirmed;
    });
  }

  /**
   * Mostrar confirmación de eliminación
   */
  confirmDelete(itemName?: string): Promise<boolean> {
    const title = '¿Estás seguro?';
    const text = itemName
      ? `Se eliminará "${itemName}" permanentemente.`
      : 'Esta acción no se puede deshacer.';

    return Swal.fire({
      title,
      text,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'Cancelar',
      confirmButtonColor: '#dc3545',
      cancelButtonColor: '#6c757d',
      focusCancel: true,
    }).then((result) => {
      return result.isConfirmed;
    });
  }

  /**
   * Mostrar toast (notificación pequeña)
   */
  toast(
    title: string,
    icon: 'success' | 'error' | 'warning' | 'info' = 'success',
  ): void {
    const Toast = Swal.mixin({
      toast: true,
      position: 'top-end',
      showConfirmButton: false,
      timer: 3000,
      timerProgressBar: true,
      didOpen: (toast) => {
        toast.addEventListener('mouseenter', Swal.stopTimer);
        toast.addEventListener('mouseleave', Swal.resumeTimer);
      },
    });

    Toast.fire({
      icon,
      title,
    });
  }

  /**
   * Mostrar loading
   */
  loading(title: string = 'Cargando...'): void {
    Swal.fire({
      title,
      allowOutsideClick: false,
      didOpen: () => {
        Swal.showLoading();
      },
    });
  }

  /**
   * Cerrar cualquier alerta abierta
   */
  close(): void {
    Swal.close();
  }
}
