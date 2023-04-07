import dayjs from 'dayjs';
import { IEmployee } from 'app/shared/model/employee.model';
import { IStock } from 'app/shared/model/stock.model';
import { IStore } from 'app/shared/model/store.model';
import { ActionType } from 'app/shared/model/enumerations/action-type.model';

export interface IAction {
  id?: number;
  code?: string | null;
  datetime?: string | null;
  type?: ActionType | null;
  quantity?: number | null;
  employee?: IEmployee | null;
  stock?: IStock | null;
  store?: IStore | null;
}

export const defaultValue: Readonly<IAction> = {};

export interface IActionCriteria {
  datetime?: string | null;
  type?: ActionType | null;
  employee?: number | null;
  article?: number | null;
  store?: number | null;
}

export class ActionCriteria implements IActionCriteria {
  constructor(
    public type?: ActionType,
    public datetime?: string,
    public employee?: number,
    public article?: number,
    public store?: number
    ) {}
}