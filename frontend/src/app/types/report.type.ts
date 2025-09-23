export type Report = {
  date: string;
  name: string;
  number: string;
  typeAccount: string;
  status: boolean;
  type: string;
  initialBalance: number;
  value: number;
  balance: number;
};

export type ReportFilter = {
  clientId: number;
  startDate: string;
  endDate: string;
};
