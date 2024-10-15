export interface IPComponent {
  id?: number;
  name?: string;
  description?: string | null;
}

export const defaultValue: Readonly<IPComponent> = {};
