import { ChangeDetectionStrategy, Component, OnInit, inject } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { CommonModule } from '@angular/common';

import { MenuService } from '../../services/menu.service';
import { MenuItem } from '../../types/menu-item.type';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [RouterLink, RouterLinkActive, CommonModule],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SidebarComponent implements OnInit {
  private readonly menuService = inject(MenuService);
  protected menuItems: MenuItem[] = [];

  ngOnInit(): void {
    this.menuItems = this.menuService.getMenuItems();
  }
}
