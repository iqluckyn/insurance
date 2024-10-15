import dayjs from 'dayjs';

export interface IProduct {
  id?: number;
  productName?: string;
  productCode?: string;
  productDescription?: string | null;
  isActive?: boolean | null;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
}

export const defaultValue: Readonly<IProduct> = {
  isActive: false,
};
