import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  Input,
  Output,
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Movement } from '../../../types/movement.type';

@Component({
  selector: 'app-movement-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './movement-list.component.html',
  styleUrl: './movement-list.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class MovementListComponent {
  @Input() movements: Movement[] = [];

  protected readonly trackById = (_: number, movement: Movement): number =>
    movement.movementId;
}
