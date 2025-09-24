export interface SearchConfig {
  placeholder: string;
  debounce?: number;
}

export interface TableColumn {
  key: string;
  label: string;
  width?: string;
  type?: 'text' | 'number' | 'date' | 'currency' | 'badge';
}

export interface TableAction {
  icon: string;
  label: string;
  type: 'edit' | 'delete' | 'view';
  class?: string;
}
