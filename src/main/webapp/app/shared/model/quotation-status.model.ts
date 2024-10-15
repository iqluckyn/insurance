export interface IQuotationStatus {
  id?: number;
  statusName?: string;
  statusCode?: string;
  description?: string | null;
  isActive?: boolean | null;
}

export const defaultValue: Readonly<IQuotationStatus> = {
  isActive: false,
};
