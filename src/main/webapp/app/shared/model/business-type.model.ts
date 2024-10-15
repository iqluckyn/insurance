export interface IBusinessType {
  id?: number;
  name?: string;
  description?: string | null;
}

export const defaultValue: Readonly<IBusinessType> = {};
