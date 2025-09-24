import { Injectable } from '@angular/core';
import { MenuItem } from '../types/menu-item.type';
import { MENU_ITEMS } from '../constants/constants';

@Injectable({
  providedIn: 'root',
})
export class MenuService {
  getMenuItems(): MenuItem[] {
    return MENU_ITEMS;
  }
}
